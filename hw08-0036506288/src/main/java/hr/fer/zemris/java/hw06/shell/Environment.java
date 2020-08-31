package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * This interface models all objects that act as a command line shell environment.
 * <p>
 * A shell can usually read input from user, can have a line written to it, it has
 * a list of supported commands and a lot of times has special symbols such as a
 * prompt symbol. Such set of operations is something we call a shell "environment".
 * <p>
 * Not all shells communicate through System.in and System.out, they can also read input
 * and write data through network constructs or files. Hence, this interface exists.
 * 
 * @author Ivan Skorupan
 */
public interface Environment {
	
	/**
	 * Reads a line of input from user and returns it.
	 * 
	 * @return one line of user input as a {@link String}
	 * @throws ShellIOException if there is a problem while reading user input
	 */
	String readLine() throws ShellIOException;
	
	/**
	 * Takes a {@link String} of <code>text</code> and writes it to the shell, but
	 * stays in current line.
	 * 
	 * @param text - text to write to the shell
	 * @throws ShellIOException if there is a problem while writing the provided <code>text</code>
	 * to the shell
	 */
	void write(String text) throws ShellIOException;
	
	/**
	 * Takes a {@link String} of <code>text</code>, writes it to the shell and
	 * goes to the next line.
	 * 
	 * @param text - text to write to the shell
	 * @throws ShellIOException if there is a problem while writing the provided <code>text</code>
	 * to the shell
	 */
	void writeln(String text) throws ShellIOException;
	
	/**
	 * Returns a {@link SortedMap} of supported commands that this shell
	 * can execute.
	 * <p>
	 * Keys of this map are command name's as {@link String strings} and values are actual
	 * {@link ShellCommand} objects.
	 * 
	 * @return a map of supported commands for this shell
	 */
	SortedMap<String, ShellCommand> commands();
	
	/**
	 * Returns the symbol that this shell uses to denote each additional
	 * line of input from user for the same command.
	 * 
	 * @return this shell's multi-line symbol
	 */
	Character getMultilineSymbol();
	
	/**
	 * Sets the symbol that this shell uses to denote each additional
	 * line of input from user for the same command.
	 * 
	 * @param symbol - character that this shell should use as a multi-line symbol
	 * @throws NullPointerException if <code>symbol</code> is <code>null</code>
	 */
	void setMultilineSymbol(Character symbol);
	
	/**
	 * Returns the symbol that this shell uses to denote the start of user input line.
	 * 
	 * @return this shell's prompt symbol
	 */
	Character getPromptSymbol();
	
	/**
	 * Sets the symbol that this shell uses to denote the start of user input line.
	 * 
	 * @param symbol - character that this shell should use as a prompt symbol
	 * @throws NullPointerException if <code>symbol</code> is <code>null</code>
	 */
	void setPromptSymbol(Character symbol);
	
	/**
	 * Returns the symbol that the user can use to denote that this shell should
	 * expect additional input lines for the same command.
	 * 
	 * @return this shell's more lines symbol
	 */
	Character getMoreLinesSymbol();
	
	/**
	 * Sets the symbol that the user can use to denote that this shell should
	 * expect additional input lines for the same command.
	 * 
	 * @param symbol - character that this shell should use as a more lines symbol
	 * @throws NullPointerException if <code>symbol</code> is <code>null</code>
	 */
	void setMoreLinesSymbol(Character symbol);
	
	/**
	 * Returns an absolute and normalized path which corresponds to the current
	 * directory of the running Java process.
	 * 
	 * @return current directory of the running Java process
	 */
	Path getCurrentDirectory();
	
	/**
	 * Sets the working directory of the shell to the provided path, if such
	 * exists. Otherwise, if the path is non-existent or invalid, an exception
	 * is thrown.
	 * 
	 * @param path - desired working directory to change to in the shell
	 */
	void setCurrentDirectory(Path path);
	
	/**
	 * Gets the shared command data from internal map under given <code>key</code>.
	 * <p>
	 * If the given <code>key</code> does not exist in the map, <code>null</code>
	 * is returned.
	 * <p>
	 * Implementations of interface {@link Environment} must always use a map for
	 * sharing data between commands. The data should never be shared using
	 * <code>static<code> variables.
	 * 
	 * @param key - key under which to fetch the shared command data from the map
	 * @return the value stored in the map under given <code>key</code>
	 */
	Object getSharedData(String key);
	
	/**
	 * Sets the shared command data in internal map to <code>value</code>
	 * under given <code>key</code>.
	 * <p>
	 * Implementations of interface {@link Environment} must always use a map for
	 * sharing data between commands. The data should never be shared using
	 * <code>static<code> variables.
	 * 
	 * @param key - key under which to set the shared command data in the map
	 * @param value - actual data to store under given <code>key</code>
	 */
	void setSharedData(String key, Object value);
	
}
