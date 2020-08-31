package hr.fer.zemris.java.hw07.observer2;

import java.util.Objects;

/**
 * Model of an object that encapsulates extra information about the Subject's
 * state change in Observer design pattern. This one holds information
 * on state change of one {@link IntegerStorage} object.
 * <p>
 * An instance of this object would be created only once for every state update
 * of the Subject and is then sent to each observer.
 * <p>
 * The information encapsulated is the reference to actual Subject, the old
 * integer value that was stored in {@link IntegerStorage} Subject before the state
 * change occurred and the new integer value that's now actually stored there.
 * <p>
 * Of course, <code>public</code> getters for each of the mentioned fields are available.
 * 
 * @author Ivan Skorupan
 */
public class IntegerStorageChange {
	
	/**
	 * A reference to the object whose state has changed and is being observed (this is the Subject).
	 */
	private IntegerStorage istorage;
	
	/**
	 * Value stored in the Subject before the state change occurred.
	 */
	private int oldIntegerValue;
	
	/**
	 * Value stored in the Subject after state change.
	 */
	private int newIntegerValue;
	
	/**
	 * Constructs a new {@link IntegerStorageChange} object and asks
	 * for all fields to be initialized so as to provide the full
	 * information on state change to each observer.
	 * 
	 * @param istorage - reference to the Subject
	 * @param oldIntegerValue - old value stored in <code>istorage</code> (before state change)
	 * @param newIntegerValue - new value stored in <code>istorage</code> (after state change)
	 * @throws NullPointerException if <code>istorage</code> is <code>null</code>
	 */
	public IntegerStorageChange(IntegerStorage istorage, int oldIntegerValue, int newIntegerValue) {
		this.istorage = Objects.requireNonNull(istorage);
		this.oldIntegerValue = oldIntegerValue;
		this.newIntegerValue = newIntegerValue;
	}
	
	/**
	 * Getter for Subject reference.
	 * 
	 * @return reference to the observed Subject
	 */
	public IntegerStorage getIstorage() {
		return istorage;
	}
	
	/**
	 * Getter for old value that used to be stored in the observee.
	 * 
	 * @return integer value stored in the Subject before state change
	 */
	public int getOldIntegerValue() {
		return oldIntegerValue;
	}
	
	/**
	 * Getter for new value that's now stored in the observee.
	 * 
	 * @return integer value stored in the Subject after state change
	 */
	public int getNewIntegerValue() {
		return newIntegerValue;
	}
	
}
