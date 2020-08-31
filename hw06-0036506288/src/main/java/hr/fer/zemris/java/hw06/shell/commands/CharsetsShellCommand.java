package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command charsets takes no arguments and and lists names of supported charsets
 * for this Java platform.
 * <p>
 * A single charset name is written per line.
 * 
 * @author Ivan Skorupan
 */
public class CharsetsShellCommand extends Command implements ShellCommand {
	
	/**
	 * Constructs a new {@link CharsetsShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public CharsetsShellCommand() {
		super("charsets");
		getDescription().add("Takes no arguments and lists names of supported charsets for your Java platform.");
		getDescription().add("A single charset name is written per line.");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		// if there is more than 0 arguments, print appropriate message to user and return
		if(args.length != 0) {
			env.writeln("Too many arguments for charsets command!");
			return ShellStatus.CONTINUE;
		}
		
		// get all available charset for this Java platform and print them to shell
		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		charsets.forEach((s, c) -> env.writeln(s));
		
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
