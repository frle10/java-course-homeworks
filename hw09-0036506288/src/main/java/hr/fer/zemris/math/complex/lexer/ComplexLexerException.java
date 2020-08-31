package hr.fer.zemris.math.complex.lexer;

/**
 * A custom model of an exception that is thrown when a fault happens on
 * a {@link ComplexLexer} object.
 * 
 * @author Ivan Skorupan
 */
public class ComplexLexerException extends RuntimeException {
	
	private static final long serialVersionUID = -3486739111634531536L;
	
	/**
	 * This is the default constructor for this class.
	 * It initializes a new {@link ComplexLexerException} object.
	 */
	public ComplexLexerException() {
		super();
	}
	
	/**
	 * Initializes a new {@link ComplexLexerException} object containing
	 * extra information on this exception's cause in <code>message</code> string.
	 * 
	 * @param message - a string containing extra information on the cause of this exception
	 */
	public ComplexLexerException(String message) {
		super(message);
	}
	
}
