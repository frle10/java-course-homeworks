package hr.fer.zemris.math.complex.parser;

/**
 * If any exception occurs during parsing using a {@link ComplexParser},
 * the parser should catch it and re-throw an instance of this exception.
 * 
 * @author Ivan Skorupan
 */
public class ComplexParserException extends RuntimeException {
	
	private static final long serialVersionUID = 981395893165535867L;
	
	/**
	 * This is the default constructor for this class.
	 * It initializes a new {@link ComplexParserException} object.
	 */
	public ComplexParserException() {
		super();
	}
	
	/**
	 * Initializes a new {@link ComplexParserException} object containing
	 * extra information on this exception's cause in <code>message</code> string.
	 * 
	 * @param message - a string containing extra information on the cause of this exception
	 */
	public ComplexParserException(String message) {
		super(message);
	}
	
}
