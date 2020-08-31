package hr.fer.zemris.java.hw07.observer2;

import java.util.Objects;

/**
 * An observer of {@link IntegerStorage} which tracks how many times
 * the internally stored integer value has been changed since this
 * observer's registration.
 * 
 * @author Ivan Skorupan
 */
public class ChangeCounter implements IntegerStorageObserver {
	
	/**
	 * Number of times the Subject state has been changed since this observer's registration.
	 */
	int changeCounter;
	
	@Override
	public void valueChanged(IntegerStorageChange istorageChange) {
		Objects.requireNonNull(istorageChange);
		System.out.println("Number of value changes since tracking: " + (++changeCounter));
	}
	
}
