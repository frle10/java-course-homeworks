package hr.fer.zemris.java.custom.collections;

/**
 * Represents an abstract collection of objects.
 * <p>
 * It contains all the methods that any type of collection is naturally expected to have,
 * such as methods for adding to or removing an object from the collection, testing if
 * the collection contains a passed object, checking the size of the collection etc.
 * <p>
 * This interface provides a default implementation of methods {@link #isEmpty()} and {@link #addAll(Collection other)}.
 * 
 * @author Ivan Skorupan
 */
public interface Collection<T> {
	
	/**
	 * Tests if the collection is empty.
	 * 
	 * @return <code>true</code> if the collection is empty, <code>false</code> otherwise
	 */
	default boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
	 * Returns the size of this collection defined as the number of elements inside it.
	 * 
	 * @return the number of elements in the collection
	 */
	int size();
	
	/**
	 * Adds the passed object to the collection.
	 * 
	 * @param value - an object to add to the collection
	 */
	void add(T value);
	
	/**
	 * Checks if the given object exists in this collection.
	 * 
	 * @param value - an object whose existence in this collection we're checking
	 * @return <code>true</code> if the object is contained in this collection, <code>false</code> otherwise
	 */
	boolean contains(Object value);
	
	/**
	 * Removes one occurrence of the given object from the collection.
	 * <p>
	 * Returns <code>true</code> only if the collection contains given <code>value</code> and removes
	 * one occurrence of it.
	 * <p>
	 * The existence of <code>value</code> in the collection should be determined by the <code>equals</code> method.
	 * 
	 * @param value - an object to remove from the collection
	 * @return <code>true</code> if one occurrence of <code>value</code> was successfully removed, <code>false</code> otherwise
	 */
	boolean remove(Object value);
	
	/**
	 * Allocates a new array with size equal to the size of this collection, fills it with collection content
	 * and returns the array.
	 * <p>
	 * This method never returns <code>null</code>.
	 * 
	 * @return an array containing the elements of this collection
	 */
	Object[] toArray();
	
	/**
	 * This method calls <code>processor.process</code> for each element of this collection.
	 * <p>
	 * In class {@link Collection} the order in which the elements will be sent is not defined.
	 * <p>
	 * A <code>null</code> reference to <code>processor</code> is <b>not</b> allowed.
	 * 
	 * @param processor - a {@link Processor} object to use for collection's elements processing
	 * @throws NullPointerException if <code>processor</code> is <code>null</code>
	 */
	default void forEach(Processor<? super T> processor) {
		ElementsGetter<T> getter = this.createElementsGetter();
		
		while(getter.hasNextElement()) {
			processor.process(getter.getNextElement());
		}
	}
	
	/**
	 * Adds all the elements from the given collection into the current collection.
	 * <p>
	 * The <code>other</code> collection remains <b>unchanged</b>.
	 * 
	 * @param other - the collection from which to copy the elements from
	 */
	default void addAll(Collection<? extends T> other) {
		/**
		 * A processor class that defines an implementation of the <code>process</code>
		 * method which adds an element into this collection.
		 * 
		 * @author Ivan Skorupan
		 * @see Processor
		 */
		class AddItemsToCurrentCollectionProcessor implements Processor<T> {
			
			/**
			 * {@inheritDoc}
			 * 
			 * This implementation of the <code>process</code> method adds the passed element to this collection.
			 */
			@Override
			public void process(T value) {
				add(value);
			}
			
		}
		
		other.forEach(new AddItemsToCurrentCollectionProcessor());
	}
	
	/**
	 * Gets elements from <code>col</code> one by one using its {@link ElementsGetter}
	 * and adds them into the current collection only if they pass the given
	 * {@link Tester Testers} test.
	 * 
	 * @param col - collection from which to copy elements from
	 * @param tester - a {@link Tester} object to use for filtering the elements from <code>col</code>
	 */
	default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
		ElementsGetter<? extends T> colGetter = col.createElementsGetter();
		
		while(colGetter.hasNextElement()) {
			T element = colGetter.getNextElement();
			if(tester.test(element)) {
				this.add(element);
			}
		}
	}
	
	/**
	 * Returns a new instance of an {@link ElementsGetter} type object on demand.
	 * 
	 * @return a new {@link ElementsGetter} type object
	 */
	ElementsGetter<T> createElementsGetter();
	
	/**
	 * Removes all elements from this collection.
	 */
	void clear();
	
}
