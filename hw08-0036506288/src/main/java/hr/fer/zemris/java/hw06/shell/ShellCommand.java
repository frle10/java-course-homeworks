package hr.fer.zemris.java.hw06.shell;

import java.util.List;

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
	 * @throws NullPointerException if <code>env</code> or <code>arguments</code> is <code>null</code>
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns this command's name.
	 * 
	 * @return this command's name
	 */
	String getCommandName();
	
	/**
	 * Returns a list of strings which represent each line
	 * of this command's description.
	 * 
	 * @return this command's description as a list of description lines
	 */
	List<String> getCommandDescription();

}
