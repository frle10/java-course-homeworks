package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command hexdump expects a single argument: file name, and
 * produces hex-output of the given file.
 * 
 * @author Ivan Skorupan
 */
public class HexdumpShellCommand extends Command {
	
	/**
	 * Hexadecimal number system base.
	 */
	private static final int HEX_BASE = 16;
	
	/**
	 * A hex mask for keeping only the lower byte of an integer.
	 */
	private static final int BYTE_MASK = 0xFF;
	
	/**
	 * A hex mask for keeping only the lower nibble of an integer.
	 */
	private static final int NIBBLE_MASK = 0x0F;
	
	/**
	 * The index of '|' character which splits the bytes string
	 * into two parts of eight bytes for a total of sixteen.
	 */
	private static final int SPLIT_EIGHT_BYTES_INDEX = 16;
	
	/**
	 * Number of bytes printed per line of hexdump.
	 */
	private static final int BYTES_PER_LINE = 16;
	
	/**
	 * Input buffer size (in bytes) when producing one line of hexdump.
	 */
	private static final int BUFFER_SIZE = 16;
	
	/**
	 * The lowest integer value to be converted to corresponding character in hexdump.
	 */
	private static final int SMALLEST_CHAR_VALUE_TO_CONVERT = 32;
	
	/**
	 * The biggest char value to be converted to corresponding character in hexdump.
	 */
	private static final int BIGGEST_CHAR_VALUE_TO_CONVERT = 127;

	/**
	 * Constructs a new {@link HexdumpShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public HexdumpShellCommand() {
		super("hexdump");
		addDescription("Expects a single argument (file name) and produces hex-output.");
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length != 1) {
			env.writeln("Wrong number of arguments for hexdump command!");
			return ShellStatus.CONTINUE;
		}
		
		Path file = null;
		try {
			file = env.getCurrentDirectory().resolve(Paths.get(CommandUtil.shedString(args[0])));
		} catch(InvalidPathException ex) {
			env.writeln("The provided path is invalid!");
			return ShellStatus.CONTINUE;
		}
		
		if(!Files.exists(file)) {
			env.writeln("The provided path leads to a non-existing entity in the filesystem!");
			return ShellStatus.CONTINUE;
		}
		
		if(Files.isDirectory(file)) {
			env.writeln("The hexdump command expects a file name, not a directory name!");
			return ShellStatus.CONTINUE;
		}

		// read the file using a buffered input stream and produce hexdump line by line
		try(InputStream is = new BufferedInputStream(Files.newInputStream(file))) {
			byte[] buffer = new byte[BUFFER_SIZE];
			int row = 0;

			while(true) {
				int r = is.read(buffer);
				if(r < 1) break;
				String hexLine = byteToHex(buffer).toUpperCase();
				env.writeln(generateHexdumpLine(hexLine, buffer, row, r));
				row++;
			}
		} catch (IOException e) {
			env.writeln("There was an error reading the file!");
			return ShellStatus.CONTINUE;
		}

		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Converts a given <code>byte</code> array into a hex-encoded
	 * string.
	 * <p>
	 * The resulting hex-encoded string uses lowercase letters a-f for
	 * representing hex digits.
	 * <p>
	 * For zero-length array an empty string is returned. 
	 * 
	 * @param bytes - an array of bytes to be hex-encoded
	 * @return an appropriate hex-encoded string for given <code>byte</code> array
	 */
	public static String byteToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for(int i = 0; i < bytes.length; i++) {
			int maskedByte = bytes[i] & BYTE_MASK;
			hexString.append(Character.forDigit(maskedByte >>> 4, HEX_BASE));
			hexString.append(Character.forDigit(maskedByte & NIBBLE_MASK, HEX_BASE));
		}
		
		return hexString.toString();
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
			if(buffer[i] < SMALLEST_CHAR_VALUE_TO_CONVERT || buffer[i] > BIGGEST_CHAR_VALUE_TO_CONVERT) {
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
		String hexLineNumber = String.format("%08X", row * BYTES_PER_LINE);
		byteCountString.append(hexLineNumber);
		byteCountString.append(": ");
		return byteCountString.toString();
	}

}
