package hr.fer.zemris.java.custom.collections;

/**
 * Represents a type of an object whose task is to return an element by element
 * from a collection on user demand.
 * 
 * @author Ivan Skorupan
 */
public interface ElementsGetter<T> {
	
	/**
	 * Checks if there are any elements left in the collection to return.
	 * 
	 * @return <code>true</code> if there are more elements in the collection, <code>false</code> otherwise
	 */
	boolean hasNextElement();
	
	/**
	 * Returns the next element in the collection.
	 * 
	 * @return the next element in the collection
	 */
	T getNextElement();
	
	/**
	 * Calls the {@link Processor#process(Object) process} method on all remaining elements
	 * in a collection.
	 * 
	 * @param p
	 */
	default void processRemaining(Processor<? super T> p) {
		while(hasNextElement()) {
			p.process(getNextElement());
		}
	}
	
}
