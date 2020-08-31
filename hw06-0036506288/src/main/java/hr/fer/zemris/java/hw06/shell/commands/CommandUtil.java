package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.ShellTokenizer;

/**
 * A utility class which provides two <code>public static</code>
 * method.
 * <p>
 * One is used for splitting the command arguments given the arguments string.
 * <p>
 * The second is used to shed the path string of enclosing quotes generated by
 * {@link ShellTokenizer}.
 * 
 * @author Ivan Skorupan
 */
public class CommandUtil {
	
	/**
	 * Splits the arguments string into discrete argument strings and returns
	 * a string array of those arguments.
	 * <p>
	 * This method uses {@link ShellTokenizer} in order to split
	 * the argument string.
	 * 
	 * @param arguments - string containing all arguments for a command
	 * @return string array containing split arguments
	 * @throws NullPointerException if <code>arguments</code> is <code>null</code>
	 */
	public static String[] getSplitCommandInput(String arguments) {
		Objects.requireNonNull(arguments);
		ShellTokenizer tokenizer = new ShellTokenizer(arguments);
		List<String> args = new ArrayList<>();
		tokenizer.nextToken();
		
		while(tokenizer.getToken() != null) {
			args.add(tokenizer.getToken());
			tokenizer.nextToken();
		}
		
		String[] array = new String[args.size()];
		for(int i = 0; i < args.size(); i++) {
			array[i] = args.get(i);
		}
		
		return array;
	}
	
	/**
	 * Takes a string that represents a file system path and sheds off
	 * unnecessary enclosing quotes.
	 * 
	 * @param pathArgument - a string representing a path
	 * @return given string whose enclosing quotes have been deleted
	 * @throws NullPointerException if <code>pathArgument</code> is <code>null</code>
	 */
	public static String shedPath(String pathArgument) {
		Objects.requireNonNull(pathArgument);
		
		if(pathArgument.startsWith("\"") && pathArgument.endsWith("\"")) {
			return pathArgument.substring(1, pathArgument.length() - 1);
		} else {
			return pathArgument;
		}
	}
	
}
