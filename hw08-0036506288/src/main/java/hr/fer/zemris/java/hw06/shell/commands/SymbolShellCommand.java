package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command symbol expects one or two arguments.
 * <p>
 * If called with one argument, it prints the symbol for given symbol type.
 * <p>
 * If called with two arguments, it changes the symbol for given symbol type.
 * <p>
 * Possible symbol types: PROMPT, MORELINES, MULTILINE.
 * 
 * @author Ivan Skorupan
 */
public class SymbolShellCommand extends Command {
	
	/**
	 * Constructs a new {@link SymbolShellCommand} object.
	 * <p>
	 * The constructor sets the command name and updates this command's
	 * description.
	 */
	public SymbolShellCommand() {
		super("symbol");
		addDescription("If provided with 1 argument, prints the symbol for given symbol type.");
		addDescription("If provided with 2 arguments, changes the symbol for given symbol type.\n");
		addDescription("Possible symbol types: PROMPT, MORELINES, MULTILINE");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		// check if we have the version with only one argument
		if(args.length == 1) {
			String symbolType = args[0];
			
			if(isValidSymbolType(symbolType)) {
				env.writeln("Symbol for " + symbolType + " is '" + getSymbolForSymbolType(symbolType, env) + "'");
			} else {
				env.writeln("Symbol type " + symbolType + " is not valid!");
			}
			
			return ShellStatus.CONTINUE;
		}
		
		// check if we have a version with two arguments
		if(args.length == 2) {
			String symbolType = args[0];
			String newSymbol = args[1];
			
			if(!isValidSymbolType(symbolType) || newSymbol.length() != 1) {
				env.writeln("Either the symbol type is invalid or the desired new symbol is more than one character long!");
				return ShellStatus.CONTINUE;
			}
			
			char newSymbolCharacter = newSymbol.charAt(0);
			char oldSymbol = getSymbolForSymbolType(symbolType, env);
			
			setNewSymbolForSymbolType(symbolType, newSymbolCharacter, env);
			
			env.writeln("Symbol for " + symbolType + " changed from '" + oldSymbol + "' to '" + newSymbolCharacter + "'");
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Wrong number of arguments for symbol command!");
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Sets the new symbol for given symbol type of <code>env</code> to given symbol.
	 * 
	 * @param symbolType - the symbol type to change
	 * @param newSymbol - new character which will be used as a new symbol
	 * @param env - environment whose symbol type to change
	 * @throws NullPointerException if <code>symbolType</code> or <code>env</code> is <code>null</code>
	 */
	private void setNewSymbolForSymbolType(String symbolType, char newSymbol, Environment env) {
		Objects.requireNonNull(symbolType);
		Objects.requireNonNull(env);
		
		switch(symbolType) {
			case "PROMPT":
				env.setPromptSymbol(newSymbol);
				break;
			case "MORELINES":
				env.setMoreLinesSymbol(newSymbol);
				break;
			case "MULTILINE":
				env.setMultilineSymbol(newSymbol);
				break;
			default:
				return;
		}
	}
	
	/**
	 * Tests if the given symbolType is valid or not.
	 * 
	 * @param symbolType - symbol type to test
	 * @return <code>true</code> if <code>symbolType</code> is valid, <code>false</code> otherwise
	 * @throws NullPointerException if <code>symbolType</code> is <code>null</code>
	 */
	private boolean isValidSymbolType(String symbolType) {
		Objects.requireNonNull(symbolType);
		
		switch(symbolType) {
			case "PROMPT":
			case "MORELINES":
			case "MULTILINE":
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * Returns the current symbol for given symbol type of <code>env</code>.
	 * 
	 * @param symbolType - symbol type to get
	 * @param env - environment whose symbol type to fetch
	 * @return current symbol for given symbol type of <code>env</code>
	 * @throws NullPointerException if <code>symbolType</code> or <code>env</code> is <code>null</code>
	 */
	private char getSymbolForSymbolType(String symbolType, Environment env) {
		Objects.requireNonNull(symbolType);
		Objects.requireNonNull(env);
		
		switch(symbolType) {
			case "PROMPT":
				return env.getPromptSymbol();
			case "MORELINES":
				return env.getMoreLinesSymbol();
			case "MULTILINE":
				return env.getMultilineSymbol();
			default:
				return '\0';
		}
	}
	
}
