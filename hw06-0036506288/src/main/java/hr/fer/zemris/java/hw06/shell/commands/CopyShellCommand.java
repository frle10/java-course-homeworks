package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command copy expects two arguments: source file name and destination file name.
 * <p>
 * If the destination file exists, the user is asked for overwriting permission.
 * <p>
 * This command works only with files, not directories (it is not possible to copy a
 * whole directory with this command).
 * <p>
 * If the second argument is a directory, it is assumed that the user wants to copy
 * the original file into that directory using the original file name.
 * 
 * @author Ivan Skorupan
 */
public class CopyShellCommand extends Command implements ShellCommand {
	
	/**
	 * String that denotes that the user has granted overwriting permission.
	 */
	private static final String ALLOW_COPY = "y";
	
	/**
	 * String that denotes that the user has not granted overwriting permission.
	 */
	private static final String DENY_COPY = "n";
	
	/**
	 * Constructs a new {@link CopyShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public CopyShellCommand() {
		super("copy");
		getDescription().add("Copies the source file to destination.");
		getDescription().add("Expects two arguments: source file name and destination path.");
		getDescription().add("If destination file already exists, the user is asked for overwriting permission.");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		// if number of arguments for this command is wrong, print appropriate message to user and return
		if(args.length != 2) {
			env.writeln("Wrong number of arguments for copy command!");
			return ShellStatus.CONTINUE;
		}
		
		// get the provided source and destination paths 
		Path source = null;
		Path destination = null;
		try {
			source = Paths.get(CommandUtil.shedPath(args[0]));
			destination = Paths.get(CommandUtil.shedPath(args[1]));
		} catch(InvalidPathException ex) {
			// if any of the provided paths is invalid, print appropriate message to user and return
			env.writeln("One of the given paths is invalid!");
			return ShellStatus.CONTINUE;
		}
		
		// if the source path is not pointing to a file or is pointing to a non-existing file,
		// print appropriate message to user and return
		if(Files.isDirectory(source) || !Files.exists(source)) {
			env.writeln("Source path must be an existing file!");
			return ShellStatus.CONTINUE;
		}
		
		// try to actually copy the file
		try {
			if(!copyFile(source, destination, env)) {
				env.writeln("\nFile " + source.getFileName() + " was not copied!");
				return ShellStatus.CONTINUE;
			}
		} catch (IOException e) {
			throw new ShellIOException("There was a problem during file copy!");
		}
		
		env.writeln("\nFile " + source.getFileName() + " successfully copied!");
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
	
	/**
	 * Implements actual execution of this commands, copies the <code>source</code>
	 * file to target <code>destination</code> directory.
	 * 
	 * @param source - source file to copy
	 * @param destination - directory to which to copy the source file (it can also be destination file name)
	 * @param env - environment through which to write messages to shell
	 * @return <code>true</code> if the file was successfully copied, <code>false</code> otherwise
	 * @throws IOException if there was a problem reading from source file
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	private boolean copyFile(Path source, Path destination, Environment env) throws IOException {
		Objects.requireNonNull(source);
		Objects.requireNonNull(destination);
		Objects.requireNonNull(env);
		
		if(Files.isDirectory(destination)) {
			destination = Paths.get(destination.toString() + source.getFileName());
		}
		
		String copy = String.valueOf(ALLOW_COPY);
		// test if the destination file already exists and if so, ask for overwriting permission
		if(Files.exists(destination)) {
			env.write("The destination file already exists. Overwrite? (y/n) --> ");
			copy = env.readLine();
			while(!copy.equals(ALLOW_COPY) && !copy.equals(DENY_COPY)) {
				env.write("Enter y/n to allow/disallow overwriting: ");
				copy = env.readLine();
			}
		}
		
		// if the user has granted overwriting permission or the file didn't already exist, copy
		if(copy.equals(ALLOW_COPY)) {
			try(InputStream is = new BufferedInputStream(Files.newInputStream(source));
					OutputStream os = new BufferedOutputStream(Files.newOutputStream(destination))){
				byte[] buffer = new byte[16];
				while(true) {
					int r = is.read(buffer);
					if(r < 1) break;
					os.write(buffer, 0, r);
				}
			}
		} else {
			// if we don't have writing permission, print appropriate message and return
			env.writeln("Copying operation did not commence.");
			return false;
		}
		
		return true;
	}
	
}
