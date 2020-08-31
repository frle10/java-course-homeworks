package hr.fer.zemris.java.hw06.shell.commands;

import java.util.List;
import java.util.Objects;
import java.util.SortedMap;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command help expects either no arguments or one argument.
 * <p>
 * If called without arguments, it will list all supported commands in current
 * {@link Environment}.
 * <p>
 * If called with one argument, which should be a commands name, it will print
 * the name of given command along with its description.
 * <p>
 * If given command name argument is not a supported command, an appropriate
 * message to the user is printed.
 * 
 * @author Ivan Skorupan
 */
public class HelpShellCommand extends Command implements ShellCommand {
	
	/**
	 * Constructs a new {@link HelpShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public HelpShellCommand() {
		super("help");
		getDescription().add("If provided with 0 arguments, lists all supported commands.");
		getDescription().add("If provided with 1 argument, prints given command name and description.");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		// if we have no arguments, print all supported commands and return
		if(args.length == 0) {
			SortedMap<String, ShellCommand> commands = env.commands();
			env.writeln("Supported commands:");
			commands.forEach((key, value) -> env.writeln("--> " + key));
			return ShellStatus.CONTINUE;
		}
		
		// if we have one argument, print command name and description if command is supported
		if(args.length == 1) {
			ShellCommand command = env.commands().get(args[0]);
			
			if(command != null) {
				env.writeln("Name: " + command.getCommandName() + "\n");
				env.writeln("Description: ");
				command.getCommandDescription().forEach((s) -> env.writeln("# " + s));
				return ShellStatus.CONTINUE;
			} else {
				// if command is not supported, print appropriate message to user
				env.writeln("Command " + args[0] + " is not supported!");
				return ShellStatus.CONTINUE;
			}
		}
		
		// if there are too many arguments, print appropriate message to user
		env.writeln("Too many arguments for help command!");
		return ShellStatus.CONTINUE;
	}
	
	@Override
	public String getCommandName() {
		return getName();
	}
	
	@Override
	public List<String> getCommandDescription() {
		return getDescription();
	}
	
}
