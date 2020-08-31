package hr.fer.zemris.java.hw17.trazilica;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw17.trazilica.commands.ExitShellCommand;
import hr.fer.zemris.java.hw17.trazilica.commands.QueryShellCommand;
import hr.fer.zemris.java.hw17.trazilica.commands.ResultsShellCommand;
import hr.fer.zemris.java.hw17.trazilica.commands.ShellCommand;
import hr.fer.zemris.java.hw17.trazilica.commands.TypeShellCommand;

/**
 * A concrete implementation of {@link Environment} interface.
 * <p>
 * This shell environment communicates with the user through {@link System#in}
 * for reading input and {@link System#out} for writing output.
 * <p>
 * This shell environment provides the following supported commands:
 * <ul>
 * 	<li>query</li>
 * 	<li>type</li>
 * 	<li>results</li>
 * 	<li>exit</li>
 * </ul>
 * 
 * @author Ivan Skorupan
 */
public class SearchEngineShellEnvironment implements Environment {
	
	/**
	 * A {@link Scanner} object that this shell environment uses for communication with
	 * the user.
	 * <p>
	 * This <code>scanner</code> is never initialized directly in this class
	 * but is passed from the outside to this class' constructor.
	 */
	private Scanner scanner;
	
	/**
	 * Internal {@link SortedMap sorted map} of this shell's supported commands.
	 * <p>
	 * Keys are commands names and values are actual {@link ShellCommand} objects.
	 */
	private final SortedMap<String, ShellCommand> commands;
	
	/**
	 * A map that contains data under distinct {@link String} keys that multiple commands can share.
	 */
	private Map<String, Object> sharedData;
	
	/**
	 * Current working directory of this environment.
	 */
	private Path workingDirectory;
	
	/**
	 * Constructs a new {@link SearchEngineShellEnvironment} object.
	 * <p>
	 * The shell is initialized with passed <code>scanner</code> that will
	 * be used for communication with the user.
	 * <p>
	 * The constructor also fills this environment's internal map
	 * with supported commands and sets the current working directory
	 * of this shell environment.
	 * 
	 * @param scanner - a {@link Scanner} object to use for communication with the user
	 * @throws NullPointerException if <code>scanner</code> is <code>null</code>
	 */
	public SearchEngineShellEnvironment(Scanner scanner) {
		this.scanner = Objects.requireNonNull(scanner);
		this.workingDirectory = Paths.get(".").toAbsolutePath().normalize();
		this.sharedData = new HashMap<>();
		commands = new TreeMap<>();
		
		commands.put("query", new QueryShellCommand());
		commands.put("type", new TypeShellCommand());
		commands.put("results", new ResultsShellCommand());
		commands.put("exit", new ExitShellCommand());
	}
	
	@Override
	public String readLine() throws SearchEngineShellIOException {
		if(scanner.hasNextLine()) {
			return scanner.nextLine();
		}
		
		throw new SearchEngineShellIOException("There was an error reading the next line in the shell!");
	}

	@Override
	public void write(String text) throws SearchEngineShellIOException {
		if(text == null) {
			throw new SearchEngineShellIOException("Text to be written to shell cannot be null!");
		}
		
		System.out.print(text);
	}

	@Override
	public void writeln(String text) throws SearchEngineShellIOException {
		if(text == null) {
			throw new SearchEngineShellIOException("Text to be written to shell cannot be null!");
		}
		
		System.out.println(text);
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return Collections.unmodifiableSortedMap(commands);
	}
	
	@Override
	public Path getCurrentDirectory() {
		return workingDirectory;
	}

	@Override
	public void setCurrentDirectory(Path path) {
		if(!Files.exists(path)) {
			throw new SearchEngineShellIOException("The provided directory does not exist!");
		}
		
		if(!Files.isDirectory(path)) {
			throw new SearchEngineShellIOException("The provided path does not lead to a directory!");
		}
		
		this.workingDirectory = path.toAbsolutePath().normalize();
	}

	@Override
	public Object getSharedData(String key) {
		return (key == null) ? null : sharedData.get(key);
	}

	@Override
	public void setSharedData(String key, Object value) {
		if(key == null) {
			throw new SearchEngineShellIOException("The key for shared command data map cannot be null!");
		}
		
		this.sharedData.put(key, value);
	}
	
}
