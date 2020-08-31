package hr.fer.zemris.java.custom.collections;

/**
 * Represents a model of an object capable of performing a
 * certain operation on the passed object.
 * 
 * @author Ivan Skorupan
 */
public interface Processor<T> {
	
	/**
	 * This method performs any kind of action on the passed <code>value</code> object.
	 * 
	 * @param value - an object to perform an action on
	 */
	public void process(T value);
	
}
