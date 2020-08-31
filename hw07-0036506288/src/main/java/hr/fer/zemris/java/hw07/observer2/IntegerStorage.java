package hr.fer.zemris.java.hw07.observer2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A model of an integer container which can also have registered observers that
 * can perform arbitrary actions whenever the internal integer value changes.
 * <p>
 * The class provides <code>public</code> methods for adding, removing and clearing
 * observers. The observers themselves are contained in an internal list.
 * <p>
 * There is also a <code>public</code> method for changing (setting) the internal
 * integer value. If a new value is being set, that method is the one that will
 * call all observers' {@link IntegerStorageObserver#valueChanged(IntegerStorage) valueChanged}
 * methods.
 * 
 * @author Ivan Skorupan
 */
public class IntegerStorage {
	
	/**
	 * Internally contained integer value.
	 */
	private int value;
	
	/**
	 * A list of all registered observers in this storage object.
	 */
	private List<IntegerStorageObserver> observers; // use ArrayList here!!!
	
	/**
	 * Constructs a new {@link IntegerStorage} object. The constructor
	 * takes an initial integer value as an argument and sets the
	 * internal integer value to it when the object is created.
	 * <p>
	 * Also, the internal list is also initialized in this constructor
	 * as an empty {@link ArrayList} object.
	 * 
	 * @param initialValue - the initial value to store in this integer container
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
		this.observers = new ArrayList<>();
	}
	
	/**
	 * Adds the passed <code>observer</code> to the internal list of registered observers,
	 * but only if the given <code>observer</code> is not already present.
	 * 
	 * @param observer - a new observer to be added to the list of registered observers
	 * @throws NullPointerException if <code>observer</code> is <code>null</code>
	 */
	public void addObserver(IntegerStorageObserver observer) {
		Objects.requireNonNull(observer);
		
		if(!observers.contains(observer)) {
			List<IntegerStorageObserver> observersCopy = new ArrayList<>(observers);
			observersCopy.add(observer);
			observers = observersCopy;
		}
	}
	
	/**
	 * Removes the desired <code>observer</code> from the list of registered
	 * observers, but of course, only if it exists in the list, otherwise
	 * this method does nothing and the list is unchanged.
	 * 
	 * @param observer - an observer to be unregistered
	 * @throws NullPointerException if <code>observer</code> is <code>null</code>
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		Objects.requireNonNull(observer);
		
		if(observers.contains(observer)) {
			List<IntegerStorageObserver> observersCopy = new ArrayList<>(observers);
			observersCopy.remove(observer);
			observers = observersCopy;
		}
	}
	
	/**
	 * Clears the internal list of observers and leaves it empty.
	 * <p>
	 * After calling this method, there will be no registered observers
	 * for this {@link IntegerStorage} object.
	 */
	public void clearObservers() {
		observers.clear();
	}
	
	/**
	 * Getter for internal integer value of this {@link IntegerStorage} object.
	 * 
	 * @return internally stored integer value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Sets the passed value as the new internally stored integer value,
	 * but only if the passed value is different from current value,
	 * otherwise this method does nothing.
	 * <p>
	 * If a new value has been set, this method calls the
	 * {@link IntegerStorageObserver#valueChanged(IntegerStorage) valueChanged}
	 * method for each registered observer.
	 * 
	 * @param value - new value to be stored in this integer container
	 */
	public void setValue(int value) {
		// Only if new value is different than the current value:
		if(this.value != value) {
			// make a change information object
			IntegerStorageChange change = new IntegerStorageChange(this, getValue(), value);
			// Update current value
			this.value = value;
			// Notify all registered observers
			if(observers != null) {
				for(IntegerStorageObserver observer : observers) {
					observer.valueChanged(change);
				}
			}
		}
	}
	
}
