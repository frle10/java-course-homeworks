package hr.fer.zemris.java.custom.collections;

/**
 * The <code>Collection</code> class represents an abstract collection of objects.
 * <p>
 * It contains all the methods that any type of collection is naturally expected to have,
 * such as methods for adding to or removing an object from the collection, testing if
 * the collection contains a passed object, checking the size of the collection etc.
 * <p>
 * Note that this class does not contain any useful implementation of provided methods,
 * except for {@link #isEmpty()} and {@link #addAll(Collection other)}.
 * <p>
 * A new class is expected to extend the <code>Collection</code> class and override
 * all the methods from it to provide concrete implementation.
 * 
 * @author Ivan Skorupan
 */
public class Collection {
	
	/**
	 * This is the default constructor for class {@link Collection}.
	 * <p>
	 * It is not intended for any class to directly create instances of {@link Collection}
	 * class. The constructor here is implemented to do nothing.
	 */
	protected Collection() {
		
	}
	
	/**
	 * Tests if the collection is empty.
	 * <p>
	 * In class {@link Collection} this method is implemented to always return
	 * <code>true</code> and it is expected that a subclass overrides it.
	 * 
	 * @return <code>true</code> if the collection is empty, <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
	 * Returns the size of this collection defined as the number of elements inside it.
	 * <p>
	 * In class {@link Collection} this method is implemented to always return
	 * 0 and it is expected that a subclass overrides it.
	 * 
	 * @return the number of elements in the collection
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Adds the passed object to the collection.
	 * 
	 * @param value - an object to add to the collection
	 */
	public void add(Object value) {
		
	}
	
	/**
	 * Checks if the given object exists in this collection.
	 * 
	 * @param value - an object whose existence in this collection we're checking
	 * @return <code>true</code> if the object is contained in this collection, <code>false</code> otherwise
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Removes one occurrence of the given object from the collection.
	 * <p>
	 * Returns <code>true</code> only if the collection contains given <code>value</code> and removes
	 * one occurrence of it.
	 * <p>
	 * The existence of <code>value</code> in the collection should be determined by the <code>equals</code> method.
	 * <p>
	 * In class <code>Collection</code> it is not determined which occurrence of the given object will be removed in case that there is
	 * more than one.
	 * <p>
	 * In class {@link Collection} this method is implemented to always return <code>false</code> and it is expected that a subclass
	 * overrides it.
	 * 
	 * @param value - an object to remove from the collection
	 * @return <code>true</code> if one occurrence of <code>value</code> was successfully removed, <code>false</code> otherwise
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Allocates a new array with size equal to the size of this collection, fills it with collection content
	 * and returns the array.
	 * <p>
	 * This method never returns <code>null</code>.
	 * <p>
	 * In class {@link Collection} this method is implemented to always throw an {@link UnsupportedOperationException}
	 * and it is expected that a subclass overrides it.
	 * 
	 * @return an array containing the elements of this collection
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
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
	public void forEach(Processor processor) {
		
	}
	
	/**
	 * Adds all the elements from the given collection into the current collection.
	 * <p>
	 * The <code>other</code> collection remains <b>unchanged</b>.
	 * 
	 * @param other - the collection from which to copy the elements from
	 */
	public void addAll(Collection other) {
		/**
		 * A processor class that defines an implementation of the <code>process</code>
		 * method which adds an element into this collection.
		 * 
		 * @author Ivan Skorupan
		 * @see Processor
		 */
		class AddItemsToCurrentCollectionProcessor extends Processor {
			
			/**
			 * {@inheritDoc}
			 * 
			 * This implementation of the <code>process</code> method adds the passed element to this collection.
			 */
			@Override
			public void process(Object value) {
				Collection.this.add(value);
			}
			
		}
		
		other.forEach(new AddItemsToCurrentCollectionProcessor());
	}
	
	/**
	 * Removes all elements from this collection.
	 */
	public void clear() {
		
	}
	
}
