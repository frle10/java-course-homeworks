package hr.fer.zemris.java.hw07.observer1;

import java.util.Objects;

/**
 * An observer of {@link IntegerStorage} which prints out the square
 * of internally stored integer value every time it changes.
 * 
 * @author Ivan Skorupan
 */
public class SquareValue implements IntegerStorageObserver {
	
	@Override
	public void valueChanged(IntegerStorage istorage) {
		Objects.requireNonNull(istorage);
		int newValue = istorage.getValue();
		System.out.println("Provided new value: " + newValue + ", square is " + newValue * newValue);
	}
	
}
