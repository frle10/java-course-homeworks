package hr.fer.zemris.java.hw06.shell;

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

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.DropdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ListdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MassrenameShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PopdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PushdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PwdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;

/**
 * A concrete implementation of {@link Environment} interface.
 * <p>
 * This shell environment communicates with the user through {@link System#in}
 * for reading input and {@link System#out} for writing output.
 * <p>
 * This shell environment provides the following supported commands:
 * <ul>
 * 	<li>charsets</li>
 * 	<li>cat</li>
 * 	<li>ls</li>
 * 	<li>tree</li>
 * 	<li>copy</li>
 * 	<li>mkdir</li>
 * 	<li>hexdump</li>
 * </ul>
 * Default symbols for this shell environment are:
 * <ul>
 * 	<li>prompt --> '>'</li>
 * 	<li>multiline --> '|'</li>
 * 	<li>more lines --> '\'</li>
 * </ul>
 * 
 * @author Ivan Skorupan
 */
public class MyShellEnvironment implements Environment {
	
	/**
	 * Current prompt symbol for this shell environment.
	 */
	private char promptSymbol = '>';
	
	/**
	 * Current multiline symbol for this shell environment.
	 */
	private char multilineSymbol = '|';
	
	/**
	 * Current more lines symbol for this shell environment.
	 */
	private char moreLinesSymbol = '\\';
	
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
	 * Constructs a new {@link MyShellEnvironment} object.
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
	public MyShellEnvironment(Scanner scanner) {
		this.scanner = Objects.requireNonNull(scanner);
		this.workingDirectory = Paths.get(".").toAbsolutePath().normalize();
		this.sharedData = new HashMap<>();
		commands = new TreeMap<>();
		
		commands.put("exit", new ExitShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
		commands.put("pwd", new PwdShellCommand());
		commands.put("cd", new CdShellCommand());
		commands.put("pushd", new PushdShellCommand());
		commands.put("popd", new PopdShellCommand());
		commands.put("listd", new ListdShellCommand());
		commands.put("dropd", new DropdShellCommand());
		commands.put("massrename", new MassrenameShellCommand()); 
	}
	
	@Override
	public String readLine() throws ShellIOException {
		if(scanner.hasNextLine()) {
			return scanner.nextLine();
		}
		
		throw new ShellIOException("There was an error reading the next line in the shell!");
	}

	@Override
	public void write(String text) throws ShellIOException {
		if(text == null) {
			throw new ShellIOException("Text to be written to shell cannot be null!");
		}
		
		System.out.print(text);
	}

	@Override
	public void writeln(String text) throws ShellIOException {
		if(text == null) {
			throw new ShellIOException("Text to be written to shell cannot be null!");
		}
		
		System.out.println(text);
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return Collections.unmodifiableSortedMap(commands);
	}

	@Override
	public Character getMultilineSymbol() {
		return multilineSymbol;
	}
	
	@Override
	public void setMultilineSymbol(Character symbol) {
		multilineSymbol = Objects.requireNonNull(symbol);
	}

	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		promptSymbol = Objects.requireNonNull(symbol);
	}

	@Override
	public Character getMoreLinesSymbol() {
		return moreLinesSymbol;
	}

	@Override
	public void setMoreLinesSymbol(Character symbol) {
		moreLinesSymbol = Objects.requireNonNull(symbol);
	}

	@Override
	public Path getCurrentDirectory() {
		return workingDirectory;
	}

	@Override
	public void setCurrentDirectory(Path path) {
		if(!Files.exists(path)) {
			throw new ShellIOException("The provided directory does not exist!");
		}
		
		if(!Files.isDirectory(path)) {
			throw new ShellIOException("The provided path does not lead to a directory!");
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
			throw new ShellIOException("The key for shared command data map cannot be null!");
		}
		
		this.sharedData.put(key, value);
	}
	
}
