package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class is an implementation of a resizable array-backed collection of objects.
 * <p>
 * This collection <b>allows</b> duplicate elements.
 * <p>
 * Storage of <code>null</code> references in this collection is <b>not</b> allowed.
 * 
 * @author Ivan Skorupan
 */
public class ArrayIndexedCollection extends Collection {
	
	/**
	 * Default capacity for a new {@link ArrayIndexedCollection} object.
	 */
	private static final int DEFAULT_CAPACITY = 16;
	
	/**
	 * Minimum backing array capacity allowed for this collection
	 */
	private static final int MINIMUM_CAPACITY = 1;
	
	/**
	 * Capacity multiplier when reallocating memory (backing array gets full).
	 */
	private static final int CAPACITY_MULTIPLIER = 2;
	
	/**
	 * An array of objects that contains this collections's elements.
	 */
	private Object[] elements;
	
	/**
	 * An integer variable storing the number of elements in this collection.
	 */
	private int size;
	
	/**
	 * This is the default constructor for class {@link ArrayIndexedCollection}.
	 * It initializes a new {@link ArrayIndexedCollection} object with initial
	 * capacity equal to {@link #DEFAULT_CAPACITY}.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Initializes a new {@link ArrayIndexedCollection} object with initial
	 * capacity equal to <code>initialCapacity</code>.
	 * 
	 * @param initialCapacity - initial capacity to be set for this {@link ArrayIndexedCollection} object
	 * @throws IllegalArgumentException if <code>initialCapacity</code> is below {@link MINIMUM_CAPACITY}
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if(initialCapacity < MINIMUM_CAPACITY) {
			throw new IllegalArgumentException();
		}
		
		this.elements = new Object[initialCapacity];
		this.size = 0;
	}
	
	/**
	 * Initializes a new {@link ArrayIndexedCollection} object.
	 * <p>
	 * Makes a subsequent delegate call to {@link #ArrayIndexedCollection(Collection other, int initialCapacity)} using
	 * <code>other</code> and {@link #DEFAULT_CAPACITY} respectively as parameters.
	 * 
	 * @see #ArrayIndexedCollection(Collection other, int initialCapacity)
	 * @param other - a collection from which to initialize a new {@link ArrayIndexedCollection} object from
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, DEFAULT_CAPACITY);
	}
	
	/**
	 * Initializes a new {@link ArrayIndexedCollection} object.
	 * <p>
	 * Initial capacity is set to <code>initialCapacity</code> unless the size of <code>other</code> is greater than that,
	 * in which case the initial capacity will be set to <code>other.size()</code> and then the contents from <code>other</code>
	 * will be copied to this new object.
	 * <p>
	 * The collection <code>other</code> must be different from <code>null</code>.
	 * 
	 * @param other - a collection from which to initialize a new {@link ArrayIndexedCollection} object from
	 * @param initialCapacity - initial capacity to be set for this {@link ArrayIndexedCollection} object
	 * @throws NullPointerException if <code>other</code> is <code>null</code>
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		Objects.requireNonNull(other);
		
		if(initialCapacity < MINIMUM_CAPACITY) {
			throw new IllegalArgumentException();
		} else if(initialCapacity < other.size()) {
			this.elements = new Object[other.size()];
		} else {
			this.elements = new Object[initialCapacity];
		}
		
		this.size = 0;
		this.addAll(other);
	}
	
	/**
	 * Test if the number of elements has reached this collection's capacity and then reallocate the memory for the backing array.
	 * <p>
	 * In case of reallocation, sets the new array capacity as old capacity multiplied by {@link #CAPACITY_MULTIPLIER}.
	 */
	private void reallocateArrayMemory() {
		if(size >= elements.length) {
			elements = Arrays.copyOf(elements, elements.length * CAPACITY_MULTIPLIER);
		}
	}

	@Override
	public int size() {
		return this.size;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Adding of <code>null</code> elements is not permitted.
	 * <p>
	 * The average complexity of this method is O(1).
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 */
	@Override
	public void add(Object value) {
		Objects.requireNonNull(value);
		
		reallocateArrayMemory();
		elements[size++] = value;
	}
	
	/**
	 * Returns the object stored in the backing array at position <code>index</code>.
	 * <p>
	 * Valid indexes are 0 to <code>size</code> - 1.
	 * <p>
	 * The average complexity of this method is O(1).
	 * 
	 * @param index - the position of the object to retrieve
	 * @return the object at position <code>index</code> in the backing array
	 * @throws IndexOutOfBoundsException if <code>index</code> is not valid.
	 */
	public Object get(int index) {
		if((index < 0) || (index > size - 1)) {
			throw new IndexOutOfBoundsException();
		}
		
		return elements[index];
	}

	@Override
	public boolean contains(Object value) {
		if(value == null) {
			return false;
		}
		
		for(int i = 0; i < size; i++) {
			if(elements[i].equals(value)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * <b>Inserts</b> (does not overwrite) the given value at the given position in the array (before actual 
	 * insertion elements at <code>position</code> and at greater positions must be shifted one place
	 * toward the end, so that an empty place is created at <code>position</code>).
	 * <p>
	 * The legal positions are 0 to size (both are included).
	 * <p>
	 * The average complexity of this method is O(n).
	 * 
	 * @param value - an object to be inserted into this collection
	 * @param position - index at which to insert <code>value</code>
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 * @throws IndexOutOfBoundsException if <code>position</code> is not legal
	 */
	public void insert(Object value, int position) {
		Objects.requireNonNull(value);
		
		if(position < 0 || position > size) {
			throw new IndexOutOfBoundsException();
		}
		
		reallocateArrayMemory();
		
		for(int i = size++; i > position; i--) {
			elements[i] = elements[i - 1];
		}
		
		elements[position] = value;
		return;
	}

	@Override
	public boolean remove(Object value) {
		if(value == null) {
			return false;
		}
		
		for(int i = 0; i < size; i++) {
			if(elements[i].equals(value)) {
				for(int j = i; j < size - 1; j++) {
					elements[j] = elements[j + 1];
				}
				elements[--size] = null;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Removes an element at specified index from the collection. Element that was previously at location index + 1 
	 * is on location index after this operation, etc.
	 * <p>
	 * Legal indexes are 0 to size - 1. 
	 * 
	 * @param index - position from which to remove an object
	 * @throws IndexOutOfBoundsException if <code>index</code> is not legal
	 */
	public void remove(int index) {
		if((index < 0) || (index > size - 1)) {
			throw new IndexOutOfBoundsException();
		}
		
		for(int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}
		
		elements[--size] = null;
		return;
	}
	
	/**
	 * Searches the collection and returns the <code>index</code> of the first occurrence of the given value 
	 * or -1 if the value is not found.
	 * <p>
	 * The argument can be <code>null</code> and the result must be that this 
	 * element is not found (since the collection cannot contain <code>null</code> elements).
	 * <p>
	 * The average complexity of this method is O(n).
	 * 
	 * @param value - an object whose <code>index</code> to search for
	 * @return position of the given object in this collection or -1 if such an object was not found
	 */
	public int indexOf(Object value) {
		final int INDEX_NOT_FOUND = -1;
		
		if(value == null) {
			return INDEX_NOT_FOUND;
		}
		
		for(int i = 0; i < size; i++) {
			if(elements[i].equals(value)) {
				return i;
			}
		}
		
		return INDEX_NOT_FOUND;
	}
	
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(elements, size);
	}
	
	@Override
	public void forEach(Processor processor) {
		Objects.requireNonNull(processor);
		
		for(int i = 0; i < size; i++) {
			processor.process(elements[i]);
		}
		
		return;
	}

	@Override
	public void clear() {
		for(int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
		return;
	}
}
