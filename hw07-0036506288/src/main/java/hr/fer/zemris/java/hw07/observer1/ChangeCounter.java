package hr.fer.zemris.java.hw07.observer1;

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
	public void valueChanged(IntegerStorage istorage) {
		Objects.requireNonNull(istorage);
		System.out.println("Number of value changes since tracking: " + (++changeCounter));
	}
	
}
