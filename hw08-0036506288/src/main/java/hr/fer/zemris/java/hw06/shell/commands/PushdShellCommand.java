package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

import static hr.fer.zemris.java.hw06.shell.commands.CommandUtil.*;

/**
 * Command "pushd" pushes the current working directory to the stack and
 * then sets the provided path as the current directory.
 * <p>
 * This command expects exactly one argument - new working directory, which
 * must be an existing directory.
 * <p>
 * If the provided path is not an existing directory, then an error message
 * is printed an nothing is pushed on the stack nor is the current directory
 * modified.
 * 
 * @author Ivan Skorupan
 */
public class PushdShellCommand extends Command implements ShellCommand {
	
	/**
	 * Constructs a new {@link PushdShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public PushdShellCommand() {
		super("pushd");
		addDescription("Pushed current working directory to the stack and sets the given directory as current.");
		addDescription("Expects exactly one argument: new current directory.");
		addDescription("If given directory doesn't exist, then the current directory is not modified.");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length != 1) {
			env.writeln("Wrong number of arguments for pushd command!");
			return ShellStatus.CONTINUE;
		}
		
		Path directory = null;
		try {
			directory = env.getCurrentDirectory().resolve(Paths.get(CommandUtil.shedString(args[0])));
		} catch(InvalidPathException ex) {
			env.writeln("The given directory is invalid!");
			return ShellStatus.CONTINUE;
		}
		
		if(!Files.exists(directory) || !Files.isDirectory(directory)) {
			env.writeln("The given path does not lead to an existing directory!");
		}
		
		Stack<Path> stack = null;
		if(env.getSharedData(STACK_KEY) == null) {
			stack = new Stack<>();
		} else {
			stack = (Stack<Path>) env.getSharedData(STACK_KEY);
		}
		
		stack.push(env.getCurrentDirectory());
		env.setSharedData(STACK_KEY, stack);
		env.setCurrentDirectory(directory);
		return ShellStatus.CONTINUE;
	}
	
}
