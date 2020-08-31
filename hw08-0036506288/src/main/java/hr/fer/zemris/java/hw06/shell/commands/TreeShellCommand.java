package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command tree expects a single argument: directory name and prints a tree
 * (each directory level shifts output two characters to the right).
 * <p>
 * Internally, a {@link TreeFileVisitor} object is used for walking the file
 * tree.
 * 
 * @author Ivan Skorupan
 */
public class TreeShellCommand extends Command {
	
	/**
	 * Constructs a new {@link TreeShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public TreeShellCommand() {
		super("tree");
		addDescription("Prints a directory tree.");
		addDescription("Expects a single argument - directory name.");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length != 1) {
			env.writeln("Wrong number of arguments for tree command!");
			return ShellStatus.CONTINUE;
		}
		
		Path directory = null;
		try {
			directory = env.getCurrentDirectory().resolve(Paths.get(CommandUtil.shedString(args[0])));
		} catch(InvalidPathException ex) {
			env.writeln("The provided path is invalid!");
			return ShellStatus.CONTINUE;
		}
		
		if(!Files.exists(directory)) {
			env.writeln("The provided path leads to a non-existing entity in the filesystem!");
			return ShellStatus.CONTINUE;
		}
		
		if(!Files.isDirectory(directory)) {
			env.writeln("You must provide a path to directory, not a file!");
			return ShellStatus.CONTINUE;
		}
		
		// create the file visitor and walk the file tree while generating the output to shell
		TreeFileVisitor visitor = new TreeFileVisitor(env);
		try {
			Files.walkFileTree(directory, visitor);
		} catch (IOException e) {
			throw new ShellIOException("There was an error while generating the tree!");
		}
		
		return ShellStatus.CONTINUE;
	}
	
}
