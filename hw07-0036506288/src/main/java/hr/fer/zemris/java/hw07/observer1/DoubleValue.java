package hr.fer.zemris.java.hw07.observer1;

import java.util.Objects;

/**
 * An observer of {@link IntegerStorage} which prints twice the internally
 * stored value of passed Subject.
 * <p>
 * This observer can only be used a limited number of times and that number
 * is defined as an argument to this object's constructor.
 * <p>
 * After being used <code>maxNumberOfTimesCalled</code> times, this observer
 * will automatically de-register itself from the list of observer's of passed
 * Subject object.
 * 
 * @author Ivan Skorupan
 */
public class DoubleValue implements IntegerStorageObserver {
	
	/**
	 * Number of times this observer's {@link #valueChanged(IntegerStorage) valueChanged()}
	 * method can be called.
	 */
	int maxNumberOfTimesCalled;
	
	/**
	 * Number of times this observer's {@link #valueChanged(IntegerStorage) valueChanged()}
	 * method was already called.
	 */
	int timesCalled;
	
	/**
	 * Constructs a new {@link DoubleValue} object. The argument this
	 * constructor takes is the number of times this observer will be
	 * able to be used.
	 * 
	 * @param maxNumberOfTimesCalled - maximum number of times the
	 * {@link #valueChanged(IntegerStorage) valueChanged} method can be called
	 * for this observer
	 */
	public DoubleValue(int maxNumberOfTimesCalled) {
		this.maxNumberOfTimesCalled = maxNumberOfTimesCalled;
	}
	
	@Override
	public void valueChanged(IntegerStorage istorage) {
		Objects.requireNonNull(istorage);
		System.out.println("Double value: " + (2 * istorage.getValue()));
		
		if(++timesCalled == maxNumberOfTimesCalled) {
			istorage.removeObserver(this);
		}
	}
	
}
