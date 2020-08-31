package hr.fer.zemris.java.hw07.observer2;

import java.util.Objects;

/**
 * An observer of {@link IntegerStorage} which prints out the square
 * of internally stored integer value every time it changes.
 * 
 * @author Ivan Skorupan
 */
public class SquareValue implements IntegerStorageObserver {
	
	@Override
	public void valueChanged(IntegerStorageChange istorageChange) {
		Objects.requireNonNull(istorageChange);
		int newValue = istorageChange.getNewIntegerValue();
		System.out.println("Provided new value: " + newValue + ", square is " + newValue * newValue);
	}
	
}
