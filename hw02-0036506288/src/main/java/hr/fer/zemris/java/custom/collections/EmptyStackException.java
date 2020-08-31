package hr.fer.zemris.java.custom.collections;

/**
 * A custom model of an exception that is thrown when an illegal operation
 * is attempted on an empty {@link ObjectStack} object.
 * 
 * @author Ivan Skorupan
 * @see <code>ObjectStack</code>
 */
public class EmptyStackException extends RuntimeException {
	
	private static final long serialVersionUID = 8649440350758699570L;

	/**
	 * This is the default constructor for this class.
	 * It initializes a new {@link EmptyStackException} object.
	 */
	public EmptyStackException() {
		super();
	}
	
	/**
	 * Initializes a new {@link EmptyStackException} object containing
	 * extra information on this exception's cause in <code>message</code> string.
	 * 
	 * @param message - a string containing extra information on the cause of this exception
	 */
	public EmptyStackException(String message) {
		super(message);
	}
	
}
