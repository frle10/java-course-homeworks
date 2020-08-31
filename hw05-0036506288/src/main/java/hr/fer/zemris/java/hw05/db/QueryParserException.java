package hr.fer.zemris.java.hw05.db;

/**
 * If any exception occurs during parsing using a {@link QueryParser},
 * the parser should catch it and re-throw an instance of this exception.
 * 
 * @author Ivan Skorupan
 */
public class QueryParserException extends RuntimeException {
	
	private static final long serialVersionUID = 981395893165535867L;
	
	/**
	 * This is the default constructor for this class.
	 * It initializes a new {@link QueryParserException} object.
	 */
	public QueryParserException() {
		super();
	}
	
	/**
	 * Initializes a new {@link QueryParserException} object containing
	 * extra information on this exception's cause in <code>message</code> string.
	 * 
	 * @param message - a string containing extra information on the cause of this exception
	 */
	public QueryParserException(String message) {
		super(message);
	}
	
}
