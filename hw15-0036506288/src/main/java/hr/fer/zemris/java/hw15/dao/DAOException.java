package hr.fer.zemris.java.hw15.dao;

/**
 * This type of exception is thrown whenever there is a failure of data access object (DAO)
 * communicating with the data persistence layer.
 * 
 * @author Ivan Skorupan
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@link DAOException} object.
	 * 
	 * @param message - information about this exception's cause
	 * @param cause - a {@link Throwable} object containing the stack trace
	 * explaining the cause of this exception
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new {@link DAOException} object with
	 * provided <code>message</code> that provides more
	 * information about the cause of this exception.
	 * 
	 * @param message - information about this exception's cause
	 */
	public DAOException(String message) {
		super(message);
	}
}