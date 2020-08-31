package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Models a command that can be executed in a shell like program.
 * <p>
 * A typical, abstract command has a name and description of its use.
 * 
 * @author Ivan Skorupan
 */
public abstract class Command {
	
	/**
	 * This command's name.
	 */
	private String name;
	
	/**
	 * This command's description as a list of description lines.
	 */
	private List<String> description;
	
	/**
	 * This constructor is used to set the command name using given <code>name</code>.
	 * <p>
	 * SInce this is an abstract class, this constructor will only ever be called by
	 * using <code>super(name)</code>.
	 * 
	 * @param name - this command's name
	 * @throws NullPointerException if <code>name</code> is <code>null</code>
	 */
	public Command(String name) {
		this.name = Objects.requireNonNull(name);
		this.description = new ArrayList<>();
	}
	
	/**
	 * Getter for command name.
	 * 
	 * @return this command's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for command description.
	 * 
	 * @return this command's description lines list
	 */
	public List<String> getDescription() {
		return description;
	}
	
}
