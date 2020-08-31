package hr.fer.zemris.java.hw05.db.lexer;

/**
 * A custom model of an exception that is thrown when a fault happens on
 * a {@link QueryLexer} object.
 * 
 * @author Ivan Skorupan
 */
public class QueryLexerException extends RuntimeException {
	
	private static final long serialVersionUID = 3360404459335877324L;

	/**
	 * This is the default constructor for this class.
	 * It initializes a new {@link QueryLexerException} object.
	 */
	public QueryLexerException() {
		super();
	}
	
	/**
	 * Initializes a new {@link QueryLexerException} object containing
	 * extra information on this exception's cause in <code>message</code> string.
	 * 
	 * @param message - a string containing extra information on the cause of this exception
	 */
	public QueryLexerException(String message) {
		super(message);
	}
	
}
