package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.ShellCommand;

/**
 * Models a command that can be executed in a shell like program.
 * <p>
 * A typical, abstract command has a name and description of its use.
 * 
 * @author Ivan Skorupan
 */
public abstract class Command implements ShellCommand {
	
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
	 * Adds the provided text to this command's description list.
	 * 
	 * @param text - a line of description to be added to this command
	 */
	protected void addDescription(String text) {
		Objects.requireNonNull(text);
		description.add(text);
	}
	
	@Override
	public String getCommandName() {
		return name;
	}
	
	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(description);
	}
	
}
