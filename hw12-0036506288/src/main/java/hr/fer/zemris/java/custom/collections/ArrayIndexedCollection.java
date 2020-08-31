package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
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
public class ArrayIndexedCollection implements List {
	
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
	 * Number of structural modifications made by the user on this collection.
	 */
	private long modificationCount;
	
	/**
	 * An implementation of {@link ElementsGetter} that fetches elements from
	 * {@link ArrayIndexedCollection} type of objects.
	 * 
	 * @author Ivan Skorupan
	 */
	private static class ArrayElementsGetter implements ElementsGetter {
		
		/**
		 * An outer {@link ArrayIndexedCollection} reference whose elements to fetch.
		 */
		private ArrayIndexedCollection array;
		
		/**
		 * Number of elements already delivered.
		 */
		private int elementsDelivered;
		
		/**
		 * Number of structural modifications made to the outer collection
		 * at the time of creation of this {@link ArrayElementsGetter} object.
		 */
		private long savedModificationCount;
		
		/**
		 * Constructs an object of type {@link ArrayElementsGetter}.
		 * 
		 * @param array - a reference to an {@link ArrayIndexedCollection} object to work with
		 */
		public ArrayElementsGetter(ArrayIndexedCollection array) {
			this.array = array;
			this.elementsDelivered = 0;
			this.savedModificationCount = array.modificationCount;
		}
		
		@Override
		public boolean hasNextElement() {
			if(savedModificationCount != array.modificationCount) {
				throw new ConcurrentModificationException();
			}
			
			return elementsDelivered < array.size();
		}

		@Override
		public Object getNextElement() {
			if(hasNextElement()) {
				return array.get(elementsDelivered++);
			} else {
				throw new NoSuchElementException();
			}
		}
		
	}
	
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
		this.modificationCount = 0;
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
		this.modificationCount = 0;
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
			modificationCount++;
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
		modificationCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Valid indexes are 0 to <code>size</code> - 1.
	 * <p>
	 * The average complexity of this method is O(1).
	 * 
	 * @throws IndexOutOfBoundsException if <code>index</code> is invalid.
	 */
	@Override
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
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Valid positions are 0 to size (both are included).
	 * <p>
	 * The average complexity of this method is O(n).
	 * 
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 * @throws IndexOutOfBoundsException if <code>position</code> is invalid
	 */
	@Override
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
		modificationCount++;
		return;
	}

	@Override
	public boolean remove(Object value) {
		if(value == null) {
			return false;
		}
		
		for(int i = 0; i < size; i++) {
			if(elements[i].equals(value)) {
				remove(i);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Valid indexes are 0 to size - 1. 
	 * 
	 * @throws IndexOutOfBoundsException if <code>index</code> is invalid
	 */
	@Override
	public void remove(int index) {
		if((index < 0) || (index > size - 1)) {
			throw new IndexOutOfBoundsException();
		}
		
		for(int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}
		
		elements[--size] = null;
		modificationCount++;
		return;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * The average complexity of this method is O(n).
	 */
	@Override
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
	public ElementsGetter createElementsGetter() {
		return new ArrayElementsGetter(this);
	}
	
	@Override
	public void clear() {
		for(int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
		modificationCount++;
		return;
	}

}
