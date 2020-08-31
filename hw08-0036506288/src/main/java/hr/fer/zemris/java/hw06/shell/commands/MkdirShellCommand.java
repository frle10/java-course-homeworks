package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command mkdir takes a single argument: directory name and creates
 * the appropriate directory structure.
 * 
 * @author Ivan Skorupan
 */
public class MkdirShellCommand extends Command {

	/**
	 * Constructs a new {@link MkdirShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public MkdirShellCommand() {
		super("mkdir");
		addDescription("Creates the appropriate directory structure.");
		addDescription("Expects a single argument - directory name.");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length != 1) {
			env.writeln("Wrong number of arguments for mkdir command!");
			return ShellStatus.CONTINUE;
		}
		
		Path directoryStruct = null;
		try {
			directoryStruct = env.getCurrentDirectory().resolve(Paths.get(CommandUtil.shedString(args[0])));
		} catch(InvalidPathException ex) {
			env.writeln("The provided path is invalid!");
			return ShellStatus.CONTINUE;
		}
		
		// create the desired directory structure
		try {
			Files.createDirectories(directoryStruct);
		} catch (IOException e) {
			env.writeln("There was a problem while creating the provided directory sructure!");
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("\nProvided directory structure was successfully generated!");
		return ShellStatus.CONTINUE;
	}
	
}
