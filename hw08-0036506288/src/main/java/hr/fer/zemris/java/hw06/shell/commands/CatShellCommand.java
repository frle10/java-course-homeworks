package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command cat expects one or two arguments.
 * <p>
 * The first argument is path to some file and is mandatory. This path
 * lead to a directory or to a file that doesn't exist in the file system.
 * <p>
 * The second argument is charset name that should be used to interpret chars from
 * bytes. If not provided, a default platform charset is used.
 * <p>
 * This command opens given file and writes its content to the shell.
 * 
 * @author Ivan Skorupan
 */
public class CatShellCommand extends Command {
	
	/**
	 * Constructs a new {@link CatShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public CatShellCommand() {
		super("cat");
		addDescription("Opens given file and writes its contents to console.");
		addDescription("The first argument is path to some file and is mandatory.");
		addDescription("The second argument is charset name that should be used to interpret chars from bytes.");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String args[] = CommandUtil.getSplitCommandInput(arguments);
		
		Charset charset = Charset.defaultCharset();
		
		// check number of arguments
		if(args.length < 1 || args.length > 2) {
			env.writeln("Wrong number of arguments for cat command!");
			return ShellStatus.CONTINUE;
		}
		
		// get the path to file from the first argument
		Path file = null;
		try {
			file = env.getCurrentDirectory().resolve(Paths.get(CommandUtil.shedString(args[0])));
		} catch(InvalidPathException ex) {
			env.writeln("The provided path is invalid!");
			return ShellStatus.CONTINUE;
		}
		
		// if the given path is a directory or if it is a file which doesn't exist, print
		// appropriate message to user and return
		if(Files.isDirectory(file)) {
			env.writeln("You must provide a path to file, not a directory!");
			return ShellStatus.CONTINUE;
		} else if(!Files.exists(file)) {
			env.writeln("Cannot print a non-existing file!");
			return ShellStatus.CONTINUE;
		}
		
		// if there are two arguments, interpret second argument as desired charset for reading the file
		if(args.length == 2) {
			try {
				charset = Charset.forName(args[1]);
			} catch(IllegalCharsetNameException | UnsupportedCharsetException ex) {
				// if the given charset is not valid, print appropriate message and return 
				env.writeln("The provided charset is either invalid or not supported");
				return ShellStatus.CONTINUE;
			}
		}
		
		// print the file to shell
		try {
			printFileToShell(file, charset, env);
		} catch (IOException e) {
			env.writeln("There was an error reading from given file!");
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Prints the given file to shell using given {@link Environment} <code>env</code>.
	 * <p>
	 * The file is read using the given <code>charset</code>.
	 * 
	 * @param path - path to file which should be printed
	 * @param charset - charset by which to interpret characters from <code>file</code>
	 * @param env - shell environment through which to print the file
	 * @throws IOException if there was a problem while reading the <code>file</code>
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	private void printFileToShell(Path file, Charset charset, Environment env) throws IOException {
		Objects.requireNonNull(file);
		Objects.requireNonNull(charset);
		Objects.requireNonNull(env);
		
		env.writeln("Printing file " + file.getFileName() + ":");
		env.writeln("------------------------------------------------");
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(Files.newInputStream(file)), charset))) {
			while(true) {
				String line = br.readLine();
				if(line == null) break;
				env.writeln(line);
			}
		}
	}
	
}
