package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command mkdir takes a single argument: directory name and creates
 * the appropriate directory structure.
 * 
 * @author Ivan Skorupan
 */
public class MkdirShellCommand extends Command implements ShellCommand {

	/**
	 * Constructs a new {@link MkdirShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public MkdirShellCommand() {
		super("mkdir");
		getDescription().add("Creates the appropriate directory structure.");
		getDescription().add("Expects a single argument - directory name.");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		// if the number of arguments for this command is wrong, print appropriate message and return
		if(args.length != 1) {
			env.writeln("Wrong number of arguments for mkdir command!");
			return ShellStatus.CONTINUE;
		}
		
		// get the directory structure as a path
		Path directoryStruct = null;
		try {
			directoryStruct = Paths.get(CommandUtil.shedPath(args[0]));
		} catch(InvalidPathException ex) {
			// if provided path is invalid, print appropriate message and return
			env.writeln("The provided path is invalid!");
			return ShellStatus.CONTINUE;
		}
		
		// create the desired directory structure
		try {
			Files.createDirectories(directoryStruct);
		} catch (IOException e) {
			throw new ShellIOException("There was a problem while creating the provided directory sructure!");
		}
		
		env.writeln("\nProvided directory structure was successfully generated!");
		return ShellStatus.CONTINUE;
	}
	
	@Override
	public String getCommandName() {
		return getName();
	}

	@Override
	public List<String> getCommandDescription() {
		return getDescription();
	}
	
}
