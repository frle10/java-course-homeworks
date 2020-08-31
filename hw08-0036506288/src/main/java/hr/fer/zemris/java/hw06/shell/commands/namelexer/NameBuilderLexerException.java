package hr.fer.zemris.java.hw06.shell.commands.namelexer;

/**
 * This exception is thrown when a problem is encountered during lexical
 * analysis of an expression in {@link NameBuilderLexer}.
 * 
 * @author Ivan Skorupan
 */
public class NameBuilderLexerException extends RuntimeException {
	
	private static final long serialVersionUID = -8082959435315914141L;
	
	/**
	 * Constructs a new {@link NameBuilderLexerException} object.
	 */
	public NameBuilderLexerException() {
		super();
	}
	
	/**
	 * Constructs a new {@link NameBuilderLexerException} object and
	 * initializes it with given <code>message</code> about the problem
	 * that occurred.
	 * 
	 * @param message - extra information about the encountered problem
	 */
	public NameBuilderLexerException(String message) {
		super(message);
	}
	
}
