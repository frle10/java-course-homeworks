package hr.fer.zemris.java.custom.collections;

/**
 * Represents a collection that is a list of elements.
 * 
 * @author Ivan Skorupan
 */
public interface List<T> extends Collection<T> {
	
	/**
	 * Returns the object that is stored at the given position in this list collection.
	 * 
	 * @param index - position to get the value from
	 * @return value element at the given position
	 */
	T get(int index);
	
	/**
	 * <b>Inserts</b> (does not overwrite) the given value at the given position in this list collection.
	 * Elements starting from this position are shifted one position.
	 * 
	 * @param value - an object to be inserted into this list
	 * @param position - index at which to insert <code>value</code>
	 */
	void insert(T value, int position);
	
	/**
	 * Searches the collection and returns the index of the first occurrence of the given value
	 * or -1 if the value is not found.
	 * <p>
	 * A <code>null</code> reference is a valid argument.
	 * 
	 * @param value - an object whose index we're trying to fetch 
	 * @return index of <code>value</code> in this list
	 */
	int indexOf(Object value);
	
	/**
	 * Removes an element at specified index from this list collection.
	 * Element that was previously at location index + 1 is on location index after
	 * this operation, etc.
	 * 
	 * @param index - position to remove a node from
	 * @throws IndexOutOfBoundsException if <code>index</code> is less than 0 or greater than size - 1
	 */
	void remove(int index);
	
}
