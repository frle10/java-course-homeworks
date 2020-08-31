package hr.fer.zemris.java.hw17.trazilica.commands;

import java.util.Objects;

/**
 * Models a command that can be executed in a shell like program.
 * <p>
 * A typical, abstract command has a name.
 * 
 * @author Ivan Skorupan
 */
public abstract class Command implements ShellCommand {
	
	/**
	 * This command's name.
	 */
	private String name;
	
	/**
	 * This constructor is used to set the command name using given <code>name</code>.
	 * 
	 * @param name - this command's name
	 * @throws NullPointerException if <code>name</code> is <code>null</code>
	 */
	public Command(String name) {
		this.name = Objects.requireNonNull(name);
	}
	
	@Override
	public String getCommandName() {
		return name;
	}
	
}
