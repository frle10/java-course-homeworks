package hr.fer.zemris.java.hw06.shell;

/**
 * This type of exception is thrown when reading or writing to
 * {@link MyShell} fails.
 * 
 * @author Ivan Skorupan
 */
public class ShellIOException extends RuntimeException {
	
	private static final long serialVersionUID = -1491587332828227666L;

	/**
	 * Constructs a new {@link ShellIOException} object.
	 */
	public ShellIOException() {
		super();
	}
	
	/**
	 * Constructs a new {@link ShellIOException} object and
	 * initializes it with given message.
	 * 
	 * @param message - information about exception cause
	 */
	public ShellIOException(String message) {
		super(message);
	}
	
}
