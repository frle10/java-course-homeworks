package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * A custom model of an exception that is thrown when a fault happens on
 * a {@link SmartScriptLexer} object.
 * 
 * @author Ivan Skorupan
 */
public class SmartScriptLexerException extends RuntimeException {
	
	private static final long serialVersionUID = -3486739111634531536L;
	
	/**
	 * This is the default constructor for this class.
	 * It initializes a new {@link SmartScriptLexerException} object.
	 */
	public SmartScriptLexerException() {
		super();
	}
	
	/**
	 * Initializes a new {@link SmartScriptLexerException} object containing
	 * extra information on this exception's cause in <code>message</code> string.
	 * 
	 * @param message - a string containing extra information on the cause of this exception
	 */
	public SmartScriptLexerException(String message) {
		super(message);
	}
	
}
