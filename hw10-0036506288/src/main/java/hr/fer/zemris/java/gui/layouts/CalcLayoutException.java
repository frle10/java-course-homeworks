package hr.fer.zemris.java.gui.layouts;

/**
 * This type of exception is thrown every time a user tries to add
 * a component on invalid position in {@link CalcLayout}.
 * 
 * @author Ivan Skorupan
 */
public class CalcLayoutException extends RuntimeException {
	
	private static final long serialVersionUID = -3503721957041493250L;
	
	/**
	 * Constructs a new {@link CalcLayoutException} object.
	 */
	public CalcLayoutException() {
		super();
	}
	
	/**
	 * Constructs a new {@link CalcLayoutException} object.
	 * <p>
	 * Takes the <code>message</code> argument string which
	 * contains extra information about the fault that
	 * happened and caused this exception to be thrown.
	 * 
	 * @param message
	 */
	public CalcLayoutException(String message) {
		super(message);
	}
	
}
