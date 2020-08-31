package hr.fer.zemris.java.hw06.shell.commands.nameparser;

/**
 * This exception is thrown whenever there is a problem encountered
 * during the parsing proccess in {@link NameBuilderParser}.
 * 
 * @author Ivan Skorupan
 */
public class NameBuilderParserException extends RuntimeException {
	
	private static final long serialVersionUID = -5463025665241436436L;
	
	/**
	 * Constructs a new {@link NameBuilderParserException} object.
	 */
	public NameBuilderParserException() {
		super();
	}
	
	/**
	 * Constructs a new {@link NameBuilderParserException} object and
	 * initializes it with given <code>message</code> about the problem
	 * that occurred.
	 * 
	 * @param message - extra information about the encountered problem
	 */
	public NameBuilderParserException(String message) {
		super(message);
	}
	
}
