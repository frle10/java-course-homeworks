package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command exit expects no arguments and it changes the shell status to
 * {@link ShellStatus#TERMINATE TERMINATE} so the shell can safely exit.
 * 
 * @author Ivan Skorupan
 */
public class ExitShellCommand extends Command {
	
	/**
	 * Constructs a new {@link ExitShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public ExitShellCommand() {
		super("exit");
		addDescription("Terminates the shell (expects 0 arguments).");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		// if there are too many arguments for this command, print appropriate message and return
		if(args.length != 0) {
			env.writeln("Too many arguments for exit command!");
		}
		
		return ShellStatus.TERMINATE;
	}
	
}
