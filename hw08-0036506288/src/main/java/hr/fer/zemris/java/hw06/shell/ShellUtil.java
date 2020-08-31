package hr.fer.zemris.java.hw06.shell;

import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.commands.CommandUtil;

/**
 * Utility class which provides three <code>public static</code>
 * methods used for extracting arguments or a command name from
 * user input line or for reading a line (or lines) of input
 * from the user inside a shell.
 * 
 * @author Ivan Skorupan
 */
public class ShellUtil {
	
	/**
	 * Takes a string of user input and extracts all command arguments
	 * from it after which they are returned as one string, each argument
	 * being separated by a single space character.
	 * 
	 * @param lines - line (or lines) of user input from the shell
	 * @return a string containing all arguments (separated by a single space)
	 * @throws NullPointerException if <code>lines</code> is <code>null</code>
	 */
	public static String extractArguments(String lines) {
		Objects.requireNonNull(lines);
		String[] lineTokens = CommandUtil.getSplitCommandInput(lines);
		
		StringBuilder arguments = new StringBuilder();
		for(int i = 1; i < lineTokens.length; i++) {
			arguments.append(reviveEscapeSequences(lineTokens[i]) + " ");
		}
		
		if(arguments.length() > 0) {
			arguments.deleteCharAt(arguments.length() - 1);
		}
		
		return arguments.toString();
	}
	
	/**
	 * Revives the lost escape sequence characters processed during parsing so that
	 * the commands can read their arguments correctly.
	 * 
	 * @param arg - a string whose escape sequences to revive
	 * @return revived string
	 */
	private static String reviveEscapeSequences(String arg) {
		StringBuilder revived = new StringBuilder(arg);
		
		if(arg.startsWith("\"") && arg.endsWith("\"")) {
			for(int i = 1; i < arg.length() - 1; i++) {
				if(arg.charAt(i) == '\"' || arg.charAt(i) == '\\') {
					revived.insert(i + (revived.length() - arg.length()), '\\');
				}
			}
		}
		
		return revived.toString();
	}

	/**
	 * Takes a string of user input and extracts and returns the command name.
	 * 
	 * @param lines - line (or lines) of user input from the shell
	 * @return command name the user entered
	 * @throws NullPointerException if <code>lines</code> is <code>null</code>
	 */
	public static String extractCommandName(String lines) {
		Objects.requireNonNull(lines);
		String[] lineTokens = CommandUtil.getSplitCommandInput(lines);
		
		return lineTokens[0];
	}
	
	/**
	 * Uses the given <code>env</code> to read user input.
	 * <p>
	 * If there were multiple lines of input, all of them
	 * are concatenated into one line without multiline or
	 * more lines symbols.
	 * 
	 * @param env - {@link Environment} object to use for communication with the user
	 * @return a {@link String} containing the read user input
	 * @throws NullPointerException if <code>env</code> is <code>null</code>
	 */
	public static String readLineOrLines(Environment env) {
		Objects.requireNonNull(env);
		StringBuilder lines = new StringBuilder();
		
		while(true) {
			String line = env.readLine().trim();
			
			if(line.charAt(line.length() - 1) != env.getMoreLinesSymbol()) {
				lines.append(line);
				break;
			}
			
			lines.append(line.substring(0, line.length() - 1));
			env.write(env.getMultilineSymbol() + " ");
		}
		
		return lines.toString();
	}
	
}
