package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command ls takes a single argument: directory and writes a 
 * directory listing (non-recursively).
 * 
 * @author Ivan Skorupan
 */
public class LsShellCommand extends Command implements ShellCommand {

	/**
	 * Maximum number of characters for string which represents the file size.
	 */
	private static final int MAXIMUM_NUMBER_OF_SIZE_CHARACTERS = 10;

	/**
	 * Constructs a new {@link LsShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public LsShellCommand() {
		super("ls");
		getDescription().add("Writes a directory listing (not recursive).");
		getDescription().add("Takes a single argument - directory.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);

		// if number of arguments for this command is wrong, print appropriate message to user and return
		if(args.length != 1) {
			env.writeln("Wrong number of arguments for ls command!");
			return ShellStatus.CONTINUE;
		}

		// get the path to directory
		Path directory = null;
		try {
			directory = Paths.get(CommandUtil.shedPath(args[0]));
		} catch(InvalidPathException ex) {
			// if the provided path is invalid, print appropriate message to user and return
			env.writeln("The provided path is invalid!");
			return ShellStatus.CONTINUE;
		}

		// if given path is not a directory, print appropriate message to user and return
		if(!Files.isDirectory(directory) || !Files.exists(directory)) {
			env.writeln("You must provide a path to existing directory!");
			return ShellStatus.CONTINUE;
		}

		// list all the files in given directory
		try {
			listFiles(directory, env);
		} catch (IOException e) {
			throw new ShellIOException("There was an error while listing the files in given directory!");
		}
		
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
	 * Lists all files in given <code>directory</code> to the shell using
	 * given <code>env</code>.
	 * 
	 * @param directory - directory whose contents to list
	 * @param env - environment to use for printing the directory contents
	 * @throws IOException if there was a problem while listing this directory's contents
	 * @throws NullPointerException if <code>directory</code> or <code>env</code> is <code>null</code>
	 */
	private void listFiles(Path directory, Environment env) throws IOException {
		Objects.requireNonNull(directory);
		Objects.requireNonNull(env);
		
		Comparator<Path> c = (p1, p2) -> p1.getFileName().compareTo(p2.getFileName());
		List<Path> files = Files.list(directory).collect(Collectors.toList());
		files.sort(c);
		
		StringBuilder fileLine = new StringBuilder();
		for(Path file : files) {
			fileLine.append(generateDirReadWriteAndExecuteString(file));
			fileLine.append(generateFileSizeString(file));
			fileLine.append(generateFileCreationTimeString(file));
			fileLine.append(file.getFileName());
			env.writeln(fileLine.toString());
			fileLine.delete(0, fileLine.length());
		}
	}
	
	/**
	 * Generates and returns a string representing the creation time of given
	 * <code>file</code> in format "yyyy-MM-dd HH:mm:ss".
	 * 
	 * @param file - file whose creation time to format as a string
	 * @return file creation time string
	 * @throws IOException if there was a problem reading the creation time attribute from
	 * <code>file</code>
	 * @throws NullPointerException if <code>file</code> is <code>null</code>
	 */
	private String generateFileCreationTimeString(Path file) throws IOException {
		Objects.requireNonNull(file);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BasicFileAttributeView faView = Files.getFileAttributeView(
				file, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attributes = faView.readAttributes();
		
		FileTime fileTime = attributes.creationTime();
		String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
		
		return formattedDateTime + " ";
	}
	
	/**
	 * Generates and returns a string representing the file size of given
	 * <code>file</code>.
	 * <p>
	 * The file size string is maximum ten characters long.
	 * 
	 * @param file - file whose size to format as a string
	 * @return file size string
	 * @throws IOException if there was a problem reading the size attribute from
	 * <code>file</code>
	 * @throws NullPointerException if <code>file</code> is <code>null</code>
	 */
	private String generateFileSizeString(Path file) throws IOException {
		Objects.requireNonNull(file);
		BasicFileAttributeView faView = Files.getFileAttributeView(
				file, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attributes = faView.readAttributes();

		Long fileSize = attributes.size();
		StringBuilder fileSizeString = new StringBuilder(fileSize.toString());

		int requiredSpaces = MAXIMUM_NUMBER_OF_SIZE_CHARACTERS - fileSizeString.length();
		for(int i = 0; i < requiredSpaces; i++) {
			fileSizeString.insert(0, ' ');
		}
		
		fileSizeString.append(' ');
		return fileSizeString.toString();
	}
	
	/**
	 * Generates and returns a string representing four different flags for
	 * given <code>file</code>.
	 * <p>
	 * The flags are:
	 * <ul>
	 * 	<li>directory - 'd'</li>
	 * 	<li>readable - 'r'</li>
	 * 	<li>writable - 'w'</li>
	 * 	<li>executable - 'x'</li>
	 * </ul>
	 * @param file - file whose flags to format as a string
	 * @return a string representing directory, readable, writable and executable flags of given
	 * <code>file</code>
	 * @throws NullPointerException if <code>file</code> is <code>null</code>
	 */
	private String generateDirReadWriteAndExecuteString(Path file) {
		Objects.requireNonNull(file);
		StringBuilder dirReadWriteExec = new StringBuilder();

		char attr = Files.isDirectory(file) ? 'd' : '-';
		dirReadWriteExec.append(attr);

		attr = Files.isReadable(file) ? 'r' : '-';
		dirReadWriteExec.append(attr);

		attr = Files.isWritable(file) ? 'w' : '-';
		dirReadWriteExec.append(attr);

		attr = Files.isExecutable(file) ? 'x' : '-';
		dirReadWriteExec.append(attr);

		dirReadWriteExec.append(' ');
		return dirReadWriteExec.toString();
	}

}
