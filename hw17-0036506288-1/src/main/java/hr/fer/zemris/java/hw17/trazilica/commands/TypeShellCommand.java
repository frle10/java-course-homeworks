package hr.fer.zemris.java.hw17.trazilica.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw17.trazilica.Environment;
import hr.fer.zemris.java.hw17.trazilica.QueryResult;
import hr.fer.zemris.java.hw17.trazilica.ShellStatus;

/**
 * Command "type" expects exactly one argument.
 * <p>
 * The argument is the index of a query result whose corresponding file should
 * be printed to the shell.
 * 
 * @author Ivan Skorupan
 */
public class TypeShellCommand extends Command {
	
	/**
	 * Constructs a new {@link TypeShellCommand} object.
	 */
	public TypeShellCommand() {
		super("type");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String args[] = CommandUtil.getSplitCommandInput(arguments);
		
		// check number of arguments
		if(args.length != 1) {
			env.writeln("Wrong number of arguments for type command!");
			return ShellStatus.CONTINUE;
		}
		
		int number = 0;
		try {
			number = Integer.parseInt(args[0]);
		} catch(NumberFormatException ex) {
			env.writeln("The type command takes one argument which must be an integer.");
			return ShellStatus.CONTINUE;
		}
		
		
		List<QueryResult> results = (List<QueryResult>) env.getSharedData("results");
		
		if(results == null) {
			env.writeln("No successful query was run since shell startup so there are no results to print.");
			return ShellStatus.CONTINUE;
		}
		
		if(number > results.size() - 1) {
			env.writeln("Number " + number + " goes out of results bounds.");
			return ShellStatus.CONTINUE;
		}
		
		// print the file to shell
		try {
			printFileToShell(results.get(number).getDocumentPath(), env);
		} catch (IOException e) {
			env.writeln("There was an error reading from given file!");
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Prints the given file to shell using given {@link Environment} <code>env</code>.
	 * 
	 * @param path - path to file which should be printed
	 * @param env - shell environment through which to print the file
	 * @throws IOException if there was a problem while reading the <code>file</code>
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	private void printFileToShell(Path file, Environment env) throws IOException {
		Objects.requireNonNull(file);
		Objects.requireNonNull(env);
		
		env.writeln("Dokument: " + file.toString());
		env.writeln("---------------------------------------------------------------------------");
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(Files.newInputStream(file))))) {
			while(true) {
				String line = br.readLine();
				if(line == null) break;
				env.writeln(line);
			}
		}
	}
	
}
