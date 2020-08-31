package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command "pwd" expects no arguments and prints the current
 * working directory to the terminal as stored in the shell's
 * implementation of {@link Environment}.
 * 
 * @author Ivan Skorupan
 */
public class PwdShellCommand extends Command {
	
	/**
	 * Constructs a new {@link PwdShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public PwdShellCommand() {
		super("pwd");
		addDescription("Prints current working directory of the shell.");
		addDescription("Expects no arguments.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length != 0) {
			env.writeln("Too many arguments for pwd command!");
			return ShellStatus.CONTINUE;
		}
		
		env.writeln(env.getCurrentDirectory().toString());
		return ShellStatus.CONTINUE;
	}
	
}
