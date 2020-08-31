package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.EmptyStackException;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import static hr.fer.zemris.java.hw06.shell.commands.CommandUtil.*;

/**
 * This command pops the top path from the stack, but does not modify the
 * current working directory.
 * <p>
 * If the stack is empty at this command call, an error message is printed.
 * 
 * @author Ivan Skorupan
 */
public class DropdShellCommand extends Command {
	
	/**
	 * Constructs a new {@link DropdShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public DropdShellCommand() {
		super("dropd");
		addDescription("Removes the path at the top of the stack.");
		addDescription("Does not modify the current directory.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length != 0) {
			env.writeln("Too many arguments for dropd command!");
			return ShellStatus.CONTINUE;
		}
		
		if(env.getSharedData(STACK_KEY) != null) {
			Stack<Path> stack = (Stack<Path>) env.getSharedData(STACK_KEY);
			try {
				stack.pop();
			} catch(EmptyStackException ex) {
				env.writeln("The stack is empty!");
				return ShellStatus.CONTINUE;
			}
		}
		
		return ShellStatus.CONTINUE;
	}
	
}
