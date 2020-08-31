package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import static hr.fer.zemris.java.hw06.shell.commands.CommandUtil.*;

/**
 * Prints all the paths currently in the stack to the terminal starting from
 * the path that was last pushed on the stack.
 * <p>
 * The actual path is printed, not the content of the directory.
 * <p>
 * If there are 3 paths on the stack, then 3 lines of output are printed.
 * <p>
 * If the stack is empty at this command call, a message is printed informing
 * the user that there are no paths stored in the stack.
 * 
 * @author Ivan Skorupan
 */
public class ListdShellCommand extends Command {
	
	/**
	 * Constructs a new {@link ListdShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public ListdShellCommand() {
		super("listd");
		addDescription("Prints all paths currently on the stack to the shell.");
		addDescription("Prints an error message if the stack is empty.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length != 0) {
			env.writeln("Too many arguments for listd command!");
			return ShellStatus.CONTINUE;
		}
		
		if(env.getSharedData(STACK_KEY) != null) {
			Stack<Path> paths = (Stack<Path>) env.getSharedData(STACK_KEY);
			if(paths.isEmpty()) {
				env.writeln("Nema pohranjenih direktorija.");
			} else {
				paths.stream().forEach((p) -> env.writeln(p.toString()));
			}
		}
		
		return ShellStatus.CONTINUE;
	}
	
}
