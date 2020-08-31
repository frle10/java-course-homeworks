package hr.fer.zemris.java.hw17.trazilica.commands;

import hr.fer.zemris.java.hw17.trazilica.Environment;
import hr.fer.zemris.java.hw17.trazilica.ShellStatus;

/**
 * Models objects that are commands which can be executed inside a shell.
 * 
 * @author Ivan Skorupan
 */
public interface ShellCommand {
	
	/**
	 * Implements this command's execution in the given <code>environment</code> using
	 * given <code>arguments</code> as command input parameters.
	 * 
	 * @param env - shell environment that the command should communicate with (be executed in) 
	 * @param arguments - parameters for this command
	 * @return {@link ShellStatus#CONTINUE continue} or {@link ShellStatus#TERMINATE terminate}
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns this command's name.
	 * 
	 * @return this command's name
	 */
	String getCommandName();

}
