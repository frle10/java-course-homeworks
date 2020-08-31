package hr.fer.zemris.java.hw14.dao;

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
	 */
	public DAOException() {
	}

	/**
	 * Constructs a new {@link DAOException} object.
	 * 
	 * @param message - information about this exception's cause
	 * @param cause - a {@link Throwable} object containing the stack trace
	 * explaining the cause of this exception
	 * @param enableSuppression - a flag indicating if this exception can be suppressed or not
	 * @param writableStackTrace - a flag indicating if this exception's stack trace is writable
	 */
	public DAOException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

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

	/**
	 * Constructs a new {@link DAOException} object.
	 * 
	 * @param cause - a {@link Throwable} object containing the stack trace
	 * explaining the cause of this exception
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}