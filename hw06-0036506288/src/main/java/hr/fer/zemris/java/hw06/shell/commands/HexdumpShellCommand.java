package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.crypto.Util;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command hexdump expects a single argument: file name, and
 * produces hex-output of the given file.
 * 
 * @author Ivan Skorupan
 */
public class HexdumpShellCommand extends Command implements ShellCommand {

	/**
	 * Length of string on the very left side of hex-output line
	 * which shows the number of bytes printed before current line
	 * in hexadecimal system.
	 */
	private static final int BYTE_COUNT_STRING_LENGTH = 8;

	/**
	 * The index of '|' character which splits the bytes string
	 * into two parts of eight bytes for a total of sixteen.
	 */
	private static final int SPLIT_EIGHT_BYTES_INDEX = 16;

	/**
	 * Constructs a new {@link HexdumpShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public HexdumpShellCommand() {
		super("hexdump");
		getDescription().add("Expects a single argument (file name) and produces hex-output.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);

		// if the number of arguments for this command is wrong, print appropriate message and return
		if(args.length != 1) {
			env.writeln("Wrong number of arguments for hexdump command!");
			return ShellStatus.CONTINUE;
		}

		// get the path to file
		Path file = null;
		try {
			file = Paths.get(CommandUtil.shedPath(args[0]));
		} catch(InvalidPathException ex) {
			// if given path is invalid, print appropriate message and return
			env.writeln("The provided path is invalid!");
			return ShellStatus.CONTINUE;
		}

		// check if given path is a directory, if so, print appropriate message and return
		if(Files.isDirectory(file)) {
			env.writeln("The hexdump command expects a file name, not a directory name!");
			return ShellStatus.CONTINUE;
		}

		// read the file using a buffered input stream and produce hexdump line by line
		try(InputStream is = new BufferedInputStream(Files.newInputStream(file))) {
			// read 16 bytes at a time, just enough needed to produce one hex-output line
			byte[] buffer = new byte[16];
			int row = 0;

			while(true) {
				int r = is.read(buffer);
				if(r < 1) break;
				String hexLine = Util.byteToHex(buffer).toUpperCase();
				env.writeln(generateHexdumpLine(hexLine, buffer, row, r));
				row++;
			}
		} catch (IOException e) {
			throw new ShellIOException("There was an error reading the file!");
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
	 * Generates and returns one line of hexdump from given <code>hexLine</code>
	 * and <code>buffer</code>.
	 * 
	 * @param hexLine - a string of 32 hex-characters representing the hex-form of current hex-line
	 * @param buffer - buffer of bytes from which to read characters
	 * @param row - current row in hexdump, it is used for generating the byte count string
	 * @param bytesRead - number of bytes read into <code>buffer</code> (it doesn't have to be full)
	 * @return a generated hexdump line as a {@link String}
	 * @throws NullPointerException if <code>hexLine</code> is <code>null</code>
	 */
	private String generateHexdumpLine(String hexLine, byte[] buffer, int row, int bytesRead) {
		Objects.requireNonNull(hexLine);
		StringBuilder hexdumpLine = new StringBuilder(generateByteCountString(row));
		
		// generate the actual hex-form part of this hex-line
		for(int i = 0, index = 0; i < bytesRead || index < hexLine.length(); i++, index += 2) {
			if(index == SPLIT_EIGHT_BYTES_INDEX) {
				hexdumpLine.deleteCharAt(hexdumpLine.length() - 1);
				hexdumpLine.append("|");
			}

			if(i >= bytesRead) {
				hexdumpLine.append(" ".repeat(3));
				continue;
			}

			hexdumpLine.append(hexLine.substring(index, index + 2) + " ");
		}

		hexdumpLine.append("| ");
		
		// generate the right-most side of hexdump line with characters from actual file
		for(int i = 0; i < bytesRead; i++) {
			if(buffer[i] < 32 || buffer[i] > 127) {
				hexdumpLine.append(".");
			} else {
				hexdumpLine.append(Character.toString(buffer[i]));
			}
		}

		return hexdumpLine.toString();
	}

	/**
	 * Generates and returns the left-most side of hexdump line which is a string
	 * showing the number of bytes read before current row.
	 * 
	 * @param row - the current row in hexdump output
	 * @return generated byte count string
	 */
	private String generateByteCountString(int row) {
		StringBuilder byteCountString = new StringBuilder();
		byteCountString.append(Integer.valueOf(row * 10));

		int requiredLeadingZeros = BYTE_COUNT_STRING_LENGTH - byteCountString.length();
		for(int i = 0; i < requiredLeadingZeros; i++) {
			byteCountString.insert(0, "0");
		}

		byteCountString.append(": ");
		
		return byteCountString.toString();
	}

}
