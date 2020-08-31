package hr.fer.zemris.java.hw17.trazilica;

import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw17.trazilica.commands.CommandUtil;

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
	 * Writes the query results to shell given the <code>env</code> environment
	 * and list of results <code>results</code>.
	 * <p>
	 * Only the first 10 results will be printed, unless a result with similarity 0
	 * is encountered in which case the printing will stop.
	 * 
	 * @param env - shell environment through which to write to user
	 * @param results - list of query results to print out
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public static void writeResults(Environment env, List<QueryResult> results) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(results);
		
		int i = 0;
		for(QueryResult result : results) {
			if(i >= 10 || result.getSimilarity() == 0.00) break;
			env.write("[" + results.indexOf(result) + "] ");
			env.writeln(result.toString());
			i++;
		}
		env.writeln("");
	}
	
}
