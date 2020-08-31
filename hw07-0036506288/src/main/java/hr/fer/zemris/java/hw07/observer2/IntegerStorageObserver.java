package hr.fer.zemris.java.hw07.observer2;

/**
 * Models objects that play a role of observers in the Observer design pattern.
 * <p>
 * Such objects must provide a concrete implementation of a method that is called
 * and executed every time the state of the observed object changes (that object
 * is called the Subject).
 * 
 * @author Ivan Skorupan
 */
public interface IntegerStorageObserver {
	
	/**
	 * This method is called every time the state of the observed object changes.
	 * <p>
	 * Since the observer needs to be able to communicate with the Subject, this method
	 * takes a reference to the Subject as its argument.
	 * <p>
	 * The implementation of this method is arbitrary and is dependent on what we want
	 * to do each time the state of the Subject changes.
	 * 
	 * @param istorage - a reference to the object whose state changed and is being observed
	 * @throws NullPointerException if <code>istorageChange</code> is <code>null</code>
	 */
	public void valueChanged(IntegerStorageChange istorageChange);
	
}
