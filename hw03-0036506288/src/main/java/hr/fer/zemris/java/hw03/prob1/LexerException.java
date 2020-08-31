package hr.fer.zemris.java.hw03.prob1;

/**
 * A custom model of an exception that is thrown when a fault happens on
 * a {@link Lexer} object.
 * 
 * @author Ivan Skorupan
 * @see Lexer
 */
public class LexerException extends RuntimeException {
	
	private static final long serialVersionUID = -8689371241741643179L;
	
	/**
	 * This is the default constructor for this class.
	 * It initializes a new {@link LexerException} object.
	 */
	public LexerException() {
		super();
	}
	
	/**
	 * Initializes a new {@link LexerException} object containing
	 * extra information on this exception's cause in <code>message</code> string.
	 * 
	 * @param message - a string containing extra information on the cause of this exception
	 */
	public LexerException(String message) {
		super(message);
	}
	
}
