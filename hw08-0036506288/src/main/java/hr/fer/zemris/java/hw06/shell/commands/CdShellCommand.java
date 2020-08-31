package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * This command takes a single argument (a path to existing directory) and
 * sets it as the shell's new current working directory through its
 * {@link Environment}.
 * 
 * @author Ivan Skorupan
 */
public class CdShellCommand extends Command {
	
	/**
	 * Constructs a new {@link CdShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public CdShellCommand() {
		super("cd");
		addDescription("Changes the current working directory to given path.");
		addDescription("Expects exactly one argument: new current directory");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length != 1) {
			env.writeln("Wrong number of arguments for copy command!");
			return ShellStatus.CONTINUE;
		}
		
		Path directory = null;
		try {
			directory = Paths.get(CommandUtil.shedString(args[0]));
		} catch(InvalidPathException ex) {
			env.writeln("The given path is invalid!");
			return ShellStatus.CONTINUE;
		}
		
		if(!Files.exists(directory) || !Files.isDirectory(directory)) {
			env.writeln("You need to provide a path to an existing directory!");
			return ShellStatus.CONTINUE;
		}
		
		env.setCurrentDirectory(env.getCurrentDirectory().resolve(directory));
		return ShellStatus.CONTINUE;
	}
	
}
