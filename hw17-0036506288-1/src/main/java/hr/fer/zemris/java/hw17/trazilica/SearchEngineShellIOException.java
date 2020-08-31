package hr.fer.zemris.java.hw17.trazilica;

/**
 * This type of exception is thrown when reading or writing to
 * {@link SearchEngineShellEnvironment} fails.
 * 
 * @author Ivan Skorupan
 */
public class SearchEngineShellIOException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new {@link SearchEngineShellIOException} object.
	 */
	public SearchEngineShellIOException() {
		super();
	}
	
	/**
	 * Constructs a new {@link SearchEngineShellIOException} object and
	 * initializes it with given message.
	 * 
	 * @param message - information about exception cause
	 */
	public SearchEngineShellIOException(String message) {
		super(message);
	}
	
}
