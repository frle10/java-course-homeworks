package hr.fer.zemris.java.hw06.shell;

import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
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
	 * Constructs a new {@link MyShellEnvironment} object.
	 * <p>
	 * The shell is initialized with passed <code>scanner</code> that will
	 * be used for communication with the user.
	 * <p>
	 * The constructor also fills this environment's internal map
	 * with supported commands.
	 * 
	 * @param scanner - a {@link Scanner} object to use for communication with the user
	 * @throws NullPointerException if <code>scanner</code> is <code>null</code>
	 */
	public MyShellEnvironment(Scanner scanner) {
		this.scanner = Objects.requireNonNull(scanner);
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
	
}
