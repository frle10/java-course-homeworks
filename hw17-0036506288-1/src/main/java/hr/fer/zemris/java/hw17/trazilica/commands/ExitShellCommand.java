package hr.fer.zemris.java.hw17.trazilica.commands;

import java.util.Objects;

import hr.fer.zemris.java.hw17.trazilica.Environment;
import hr.fer.zemris.java.hw17.trazilica.ShellStatus;

/**
 * Command exit expects no arguments and it changes the shell status to
 * {@link ShellStatus#TERMINATE TERMINATE} so the shell can safely exit.
 * 
 * @author Ivan Skorupan
 */
public class ExitShellCommand extends Command {
	
	/**
	 * Constructs a new {@link ExitShellCommand} object.
	 */
	public ExitShellCommand() {
		super("exit");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length != 0) {
			env.writeln("Too many arguments for exit command!");
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.TERMINATE;
	}
	
}
