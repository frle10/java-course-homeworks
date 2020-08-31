package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.namegenerator.FilterResult;
import hr.fer.zemris.java.hw06.shell.commands.namegenerator.NameBuilder;
import hr.fer.zemris.java.hw06.shell.commands.nameparser.NameBuilderParser;

/**
 * General format of massrename command is:
 * <p>
 * <b>massrename DIR1 DIR2 MODIFIER MASK other</b>
 * <p>
 * This command is used for mass renaming/moving files (not directories!) that are directly
 * in DIR1. The files would be moved to DIR2 (which can be the same as DIR1).
 * <p>
 * MASK is a regular expression written in accordance with syntax supported by
 * {@link Pattern} class. The MASK selects/filters files from DIR1 that will be
 * renamed/moved.
 * <p>
 * MODIFIER is a sub-command of massrename commands. There are four supported modifiers:
 * <ul>
 * 	<li>filter</li>
 * 	<li>groups</li>
 * 	<li>show</li>
 * 	<li>execute</li>
 * </ul>
 * <p>
 * If MODIFIER is "filter", the command prints the names of files that are selected by the MASK.
 * <p>
 * If MODIFIER is "groups", the command prints all the groups for all MASK selected files
 * (including the implicit group 0).
 * <p>
 * If MODIFIER is "show", the command uses the "other" part which in this case is an expression
 * that defines how new file names should be generated. The command in this case prints all
 * MASK selected file names and their corresponding new names.
 * <p>
 * File name generation expression can contain special substitution commands such as {group Number}
 * and {groupNumber,format}.
 * <p>
 * If MODIFIER is "execute", the command will actually rename/move files and also print the same info
 * as "show" modifier to the shell.
 * 
 * @author Ivan Skorupan
 */
public class MassrenameShellCommand extends Command {

	/**
	 * String literal constant that represents the filter modifier.
	 */
	private static final String FILTER_MODIFIER = "filter";

	/**
	 * String literal constant that represents the groups modifier.
	 */
	private static final String GROUPS_MODIFIER = "groups";

	/**
	 * String literal constant that represents the show modifier.
	 */
	private static final String SHOW_MODIFIER = "show";

	/**
	 * String literal constant that represents the execute modifier.
	 */
	private static final String EXECUTE_MODIFIER = "execute";

	/**
	 * Constructs a new {@link MassrenameShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public MassrenameShellCommand() {
		super("massrename");
		addDescription("This command does a mass rename/moving of files (not directories!).");
		addDescription("General format is: massrename DIR1 DIR2 MODIFIER MASK other");
		addDescription("DIR1 and DIR2 must be existing directories in the file system.");
		addDescription("MODIFIER can be 'filter', 'groups', 'show' and 'execute'.");
		addDescription("MASK is a regular expression in accordance to Pattern class syntax.");
		addDescription("Other part is used with 'show' and 'execute' modifiers for new file name generation.");
		addDescription("Other part can contain substitution commands of format: {groupNumber} or {groupNumber,format}.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);

		if(!areValidArguments(args, env)) {
			return ShellStatus.CONTINUE;
		}

		Path sourceDir = null;
		Path destinationDir = null;
		try {
			sourceDir = env.getCurrentDirectory().resolve(Paths.get(CommandUtil.shedString(args[0])));
			destinationDir = env.getCurrentDirectory().resolve(Paths.get(CommandUtil.shedString(args[1])));
		} catch(InvalidPathException ex) {
			env.writeln("The first two arguments of massrename command are invalid directory paths!");
			return ShellStatus.CONTINUE;
		}

		if(!areValidDirectories(sourceDir, destinationDir, env)) {
			return ShellStatus.CONTINUE;
		}

		String modifier = args[2];
		String pattern = CommandUtil.shedString(args[3]);
		
		List<FilterResult> filteredFiles;
		try {
			filteredFiles = filter(sourceDir, pattern);
		} catch (IOException e) {
			env.writeln("There was a problem while filtering the files!");
			return ShellStatus.CONTINUE;
		}

		if(modifier.equals(FILTER_MODIFIER)) {
			filteredFiles.forEach((fr) -> env.writeln(fr.toString()));
			return ShellStatus.CONTINUE;
		} else if(modifier.equals(GROUPS_MODIFIER)) {
			filteredFiles.forEach((fr) -> env.writeln(printGroups(fr)));
			return ShellStatus.CONTINUE;
		}

		if(modifier.equals(SHOW_MODIFIER) || modifier.equals(EXECUTE_MODIFIER)) {
			String expression = args[4];
			NameBuilderParser parser = new NameBuilderParser(expression);
			NameBuilder builder = parser.getNameBuilder();
			
			for(FilterResult file : filteredFiles) {
				StringBuilder sb = new StringBuilder();
				builder.execute(file, sb);
				String newName = sb.toString();
				env.writeln(file.toString() + " => " + newName);
				
				if(modifier.equals(EXECUTE_MODIFIER)) {
					Path sourcePath = Paths.get(sourceDir.toString() + "/" + file.toString());
					Path destinationPath = Paths.get(destinationDir.toString() + "/" + newName);
					try {
						Files.move(sourcePath, destinationPath);
					} catch (IOException e) {
						env.writeln("There was a problem moving the file " + file.toString() + "!");
						return ShellStatus.CONTINUE;
					}
				}
			}
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Returns a string of following format:
	 * <p>
	 * <i>0: group0Content 1: group1Content ... N: groupNContent</i>
	 * <p>
	 * This is used when the MODIFIER for this command is "groups".
	 * 
	 * @param fr - an object representing one selected file that contains its {@link Matcher} group info
	 * @return a string with group information
	 */
	private String printGroups(FilterResult fr) {
		StringBuilder groups = new StringBuilder();
		groups.append(fr.toString() + " ");

		for(int i = 0; i < fr.numberOfGroups(); i++) {
			groups.append(i + ": " + fr.group(i) + " ");
		}

		return groups.toString().trim();
	}

	/**
	 * Runs through the given directory <code>dir</code> and returns
	 * a list of all files that match the given <code>pattern</code>.
	 * 
	 * @param dir - directory to run through
	 * @param pattern - pattern to check against for each file in <code>dir</code>
	 * @return list of all files in <code>dir</code> that match <code>pattern</code>
	 * @throws IOException if there is a problem reading the files from file system
	 */
	private static List<FilterResult> filter(Path dir, String pattern) throws IOException {
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		List<Path> files = Files.list(dir).filter((path) -> !Files.isDirectory(path)).collect(Collectors.toList());
		List<FilterResult> filteredFiles = new ArrayList<>();

		Matcher m = null;
		for(Path file : files) {
			m = p.matcher(file.getFileName().toString());
			if(m.matches()) {
				filteredFiles.add(new FilterResult(m));
			}
		}

		return filteredFiles;
	}

	/**
	 * Tests if the given source and destination directories are valid.
	 * <p>
	 * A directory is valid if it exists in the file system and if it is
	 * indeed a directory and not a file.
	 * 
	 * @param sourceDir - source directory
	 * @param destinationDir - destination directory
	 * @param env - environment through which the command communicates with the shell
	 * @return <code>true</code> if both <code>sourceDir</code> and <code>destinationDir</code> are valid, <code>false</code> otherwise
	 */
	private boolean areValidDirectories(Path sourceDir, Path destinationDir, Environment env) {
		boolean invalidDirectories = !Files.exists(sourceDir) || !Files.exists(destinationDir)
				|| !Files.isDirectory(sourceDir) || !Files.isDirectory(destinationDir);
		if(invalidDirectories == true) {
			env.writeln("The first two arguments of massrename command must be existing directories!");
			return false;
		}

		return true;
	}

	/**
	 * Checks if the arguments for this command are valid.
	 * <p>
	 * Arguments are valid if the number of arguments is exactly
	 * the number of expected arguments for corresponding
	 * MODIFIER of massrename command and if the MODIFIER
	 * is supported.
	 * 
	 * @param args - massrename command arguments
	 * @param env - environment through which the command communicates with the shell
	 * @return <code>true</code> if arguments are valid, <code>false</code> otherwise
	 */
	private boolean areValidArguments(String[] args, Environment env) {
		if(args.length < 4) {
			env.writeln("Not enough arguments for massrename command!");
			return false;
		}

		if(args.length > 5) {
			env.writeln("Too many arguments for massrename command!");
			return false;
		}

		String cmd = args[2];
		if(cmd.equals(FILTER_MODIFIER) || cmd.equals(GROUPS_MODIFIER)) {
			if(args.length != 4) {
				env.writeln("When using 'filter' or 'groups' modifiers with massrename command, there should be only 4 arguments!");
				return false;
			}

			return true;
		}

		if(cmd.equals(SHOW_MODIFIER) || cmd.equals(EXECUTE_MODIFIER)) {
			if(args.length != 5) {
				env.writeln("When using 'show' or 'execute' modifiers with massrename command, there should be exactly 5 arguments!");
				return false;
			}

			return true;
		}

		env.writeln("Unsupported modifier \"" + cmd + "\".");
		return false;
	}

}
