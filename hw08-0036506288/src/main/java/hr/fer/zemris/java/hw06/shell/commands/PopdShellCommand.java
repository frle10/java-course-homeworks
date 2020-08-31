package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EmptyStackException;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import static hr.fer.zemris.java.hw06.shell.commands.CommandUtil.*;

/**
 * Command "popd" expects no arguments. It pops one path from the stack
 * and sets it as the current working directory if such still exists.
 * <p>
 * In case the popped path was deleted in the meantime (while it was on the stack),
 * the path is still popped, but the current working directory is not modified.
 * <p>
 * An error message is printed if the stack is empty at this command call.
 * 
 * @author Ivan Skorupan
 */
public class PopdShellCommand extends Command {
	
	/**
	 * Constructs a new {@link PopdShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public PopdShellCommand() {
		super("popd");
		addDescription("Pops a single path from the stack and and sets it as the current directory of this shell.");
		addDescription("If meanwhile the directory was deleted and doesn't exist anymore, the path is popped, but current directory is not modified.");
		addDescription("Expects 0 arguments.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length != 0) {
			env.writeln("Too many arguments for hexdump command!");
			return ShellStatus.CONTINUE;
		}
		
		Path topPath = null;
		try {
			topPath = ((Stack<Path>) env.getSharedData(STACK_KEY)).pop();
			topPath = env.getCurrentDirectory().resolve(topPath);
		} catch(EmptyStackException ex) {
			env.writeln("The stack is empty!");
			return ShellStatus.CONTINUE;
		}
		
		if(!Files.exists(topPath)) {
			env.writeln("Current directory was not changed because the path from top of the stack does not exist anymore!");
			return ShellStatus.CONTINUE;
		}
		
		env.setCurrentDirectory(topPath);
		return ShellStatus.CONTINUE;
	}
	
}
