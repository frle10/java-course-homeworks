package hr.fer.zemris.java.custom.collections;

import static java.lang.Math.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Models a hash-table that enables storage of ordered pairs
 * (key, value).
 * <p>
 * This is an implementation in which <code>null</code> keys are not allowed,
 * but values can be <code>null</code>.
 * <p>
 * If the hashing calculation gives the same hash-code already used by another entry,
 * the new entry is appended to the existing entry to create a linked list.
 * 
 * @author Ivan Skorupan
 *
 * @param <K> - type of key
 * @param <V> - type of value
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
	/**
	 * An internal table of references to the heads of linked lists
	 * in each slot of this table.
	 */
	private TableEntry<K, V>[] table;

	/**
	 * Number of pairs stored in this table.
	 */
	private int size;

	/**
	 * Number of times this table has been modified since its instantiation.
	 */
	private int modificationCount;

	/**
	 * The default capacity for this hash-table is 16 slots.
	 */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * The minimum possible capacity allowed for this table.
	 */
	private static final int MINIMUM_CAPACITY = 1;

	/**
	 * A number by which to multiply the old capacity when calling the {@link #remap()} method.
	 * <p>
	 * This number is equal to 2.
	 */
	private static final int CAPACITY_MULTIPLIER = 2;

	/**
	 * The percentage of this table's capacity that the number of entries can reach before
	 * the table is extended.
	 */
	private static final double THRESHOLD = 0.75;

	/**
	 * Models one slot in the {@link SimpleHashtable}.
	 * 
	 * @author Ivan Skorupan
	 *
	 * @param <K> - type of key
	 * @param <V> - type of value
	 */
	public static class TableEntry<K, V> {
		/**
		 * The key of this entry.
		 */
		private K key;

		/**
		 * The value stored in this entry.
		 */
		private V value;

		/**
		 * A reference to the next entry in a list of entries in one
		 * {@link SimpleHashtable} slot.
		 */
		private TableEntry<K, V> next;

		/**
		 * Constructs a new {@link TableEntry} object.
		 * 
		 * @param key - this entry's key
		 * @param value - this entry's value
		 * @param next - a reference to the next entry in this table slot
		 * @throws NullPointerException if <code>key</code> is <code>null</code>
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = Objects.requireNonNull(key);
			this.value = value;
			this.next = next;
		}

		/**
		 * Returns the <code>value</code> stored in this entry.
		 * 
		 * @return this entry's value
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets the <code>value</code> of this entry.
		 * 
		 * @param value - the value to be set for this entry
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * Returns the <code>key</code> of this entry.
		 * 
		 * @return this entry's key
		 */
		public K getKey() {
			return key;
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}
	}

	/**
	 * Models an iterator that can be used to iterate through entries of {@link SimpleHashtable}.
	 * 
	 * @author Ivan Skorupan
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {

		/**
		 * The current index at which this iterator is situated.
		 */
		private int currentIndex;

		/**
		 * Number of modifications made to the table at the time of this iterator's instantiation.
		 */
		private int modificationCountSnapshot;

		/**
		 * The last entry returned by the {@link #next()} method of this iterator.
		 */
		private TableEntry<K, V> lastReturned;

		/**
		 * The last entry visited by the {@link #next()} method of this iterator.
		 */
		private TableEntry<K, V> entry;

		/**
		 * Constructs a new {@link IteratorImpl} object.
		 */
		public IteratorImpl() {
			modificationCountSnapshot = modificationCount;
		}

		@Override
		public boolean hasNext() {
			if(modificationCountSnapshot != modificationCount) {
				throw new ConcurrentModificationException();
			}

			TableEntry<K, V> e = getNextElement(false);
			return e != null;
		}

		@Override
		public TableEntry<K, V> next() {
			TableEntry<K, V> e = getNextElement(true);

			if(e == null) {
				throw new NoSuchElementException();
			}
			
			entry = lastReturned = e;
			return e;
		}

		@Override
		public void remove() {
			if(lastReturned == null) {
				throw new IllegalStateException();
			} else if(modificationCountSnapshot != modificationCount) {
				throw new ConcurrentModificationException();
			}

			SimpleHashtable.this.remove(lastReturned.key);
			lastReturned = null;
			modificationCountSnapshot++;
		}

		/**
		 * Finds and returns the next element in the hash-table to be retrieved, if such exists, otherwise
		 * this method returns <code>null</code>.
		 * 
		 * @param updateCurrentIndex - <code>true</code> if this method should update the current index of this
		 * iterator, <code>false</code> otherwise
		 * @return the next entry in this table, <code>null</code> otherwise
		 */
		private TableEntry<K, V> getNextElement(boolean updateCurrentIndex) {
			// set e to the next entry in the list of last visited table slot (unless entry is null, then we will go visit other slots)
			TableEntry<K, V> e = (entry == null) ? null : entry.next;
			int index = currentIndex;

			// try find the next populated slot in the table
			while(e == null && index < table.length) {
				e = table[index++];
			}
			
			if(updateCurrentIndex == true) {
				currentIndex = index;
			}
			
			return e;
		}
	}

	/**
	 * Constructs a new {@link SimpleHashtable} object.
	 * <p>
	 * Since no initial capacity is provided, it is set to {@link #DEFAULT_CAPACITY}.
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}
	/**
	 * Constructs a new {@link SimpleHashtable} object.
	 * <p>
	 * Takes initial capacity as a parameter, but the actual capacity of this
	 * new table will be the first power of two greater than or equal to
	 * <code>initialCapacity</code>. 
	 * 
	 * @param initialCapacity - the initial number of slots for this hash-table
	 * @throws IllegalArgumentException if <code>capacity</code> is less than {@link #MINIMUM_CAPACITY}
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if(capacity < MINIMUM_CAPACITY) {
			throw new IllegalArgumentException();
		}

		table = (TableEntry<K, V>[]) new TableEntry[findPowerOfTwoCapacity(capacity)];
	}

	/**
	 * Finds the first power of two greater than or equal to the given integer parameter.
	 * 
	 * @param initialCapacity - given initial capacity for the table
	 * @return actual capacity for the table as the first power of two greater than or equal to <code>initialCapacity</code>
	 */
	private int findPowerOfTwoCapacity(int capacity) {
		int powerOfTwoCapacity = 1;

		while(powerOfTwoCapacity < capacity) {
			powerOfTwoCapacity *= 2;
		}

		return powerOfTwoCapacity;
	}

	/**
	 * Puts a new entry into the hash-table. If an entry with the same <code>key</code> already
	 * exists within this table, its <code>value</code> is overwritten.
	 * <p>
	 * If no such key exists, a new entry is put into the table at position determined by
	 * the formula: <code>key.hashCode() % table.length</code>.
	 * 
	 * @param key - key of the new entry
	 * @param value - value of the new entry
	 * @throws NullPointerException if the <code>key</code> is <code>null</code>
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key);

		// check if the hash-table is getting full and therefore has degraded performance
		checkThreshold();

		int slotIndex = abs(key.hashCode()) % table.length;

		TableEntry<K, V> currentEntry = table[slotIndex];
		TableEntry<K, V> previous = currentEntry;

		// if there is nothing in this slot, create a brand new table entry here
		if(currentEntry == null) {
			table[slotIndex] = new TableEntry<>(key, value, null);
			size++;
			modificationCount++;
			return;
		}

		// if there already are elements in this slot, move to the end of the list
		// and at the same time check for an existing key to update its value
		while(currentEntry != null) {
			if(key.equals(currentEntry.key)) {
				currentEntry.value = value;
				return;
			}

			previous = currentEntry;
			currentEntry = currentEntry.next;
		}

		// at this point, previous will be the last table entry in this slot's list
		previous.next = new TableEntry<>(key, value, null);
		size++;
		modificationCount++;
	}

	/**
	 * Checks if the table's capacity should be extended.
	 */
	private void checkThreshold() {
		if(size >= THRESHOLD * table.length) {
			remap();
		}
	}
	
	/**
	 * Extends this table's memory capacity when the number of entries reaches 75% of
	 * the table's old capacity.
	 * <p>
	 * The new capacity is twice the old capacity.
	 */
	@SuppressWarnings("unchecked")
	private void remap() {
		TableEntry<K, V>[] oldTable = table;

		int newCapacity = table.length * CAPACITY_MULTIPLIER;

		TableEntry<K, V>[] newTable = (TableEntry<K, V>[]) new TableEntry[newCapacity];
		table = newTable;

		for(TableEntry<K, V> entry : oldTable) {
			TableEntry<K, V> currentEntry = entry;

			while(currentEntry != null) {
				copyTableEntry(currentEntry.key, currentEntry.value);
				currentEntry = currentEntry.next;
			}
		}

		modificationCount++;
	}
	
	/**
	 * This method is a modification of the {@link #put(Object, Object) put} method that is
	 * called when the table is being extended by the {@link #remap()} method.
	 * <p>
	 * This method does not update the {@link #size} or {@link #modificationCount} fields.
	 * 
	 * @param key - key of entry to be copied
	 * @param value - value of entry to be copied
	 * @throws NullPointerException if <code>key</code> is <code>null</code>
	 */
	private void copyTableEntry(K key, V value) {
		Objects.requireNonNull(key);
		
		int slotIndex = abs(key.hashCode()) % table.length;

		TableEntry<K, V> currentEntry = table[slotIndex];
		TableEntry<K, V> previous = currentEntry;
		
		if(currentEntry == null) {
			table[slotIndex] = new TableEntry<>(key, value, null);
			return;
		}
		
		while(currentEntry != null) {
			if(key.equals(currentEntry.key)) {
				currentEntry.value = value;
				return;
			}

			previous = currentEntry;
			currentEntry = currentEntry.next;
		}
		
		previous.next = new TableEntry<>(key, value, null);
	}

	/**
	 * Returns the value stored under the given key.
	 * <p>
	 * If no such key is found or if <code>key</code> is <code>null</code>,
	 * <code>null</code> is returned.
	 * 
	 * @param key - the key whose value to search for
	 * @return the value stored within the given <code>key</code> or <code>null</code> if no such key exists
	 */
	public V get(Object key) {
		if(key == null) {
			return null;
		}

		int slotIndex = abs(key.hashCode()) % table.length;

		TableEntry<K, V> currentEntry = table[slotIndex];

		while(currentEntry != null) {
			if(key.equals(currentEntry.key)) {
				return currentEntry.value;
			}

			currentEntry = currentEntry.next;
		}

		return null;
	}

	/**
	 * Returns the number of entries in this {@link SimpleHashtable}.
	 * 
	 * @return number of entries in this table
	 */
	public int size() {
		return size;
	}

	/**
	 * Tests if this {@link SimpleHashtable} contains the given <code>key</code>.
	 * 
	 * @param key - the key to search for
	 * @return <code>true</code> if the given <code>key</code> was found, <code>false</code> otherwise
	 */
	public boolean containsKey(Object key) {
		if(key == null) {
			return false;
		}

		int slotIndex = abs(key.hashCode()) % table.length;

		TableEntry<K, V> currentEntry = table[slotIndex];

		while(currentEntry != null) {
			if(key.equals(currentEntry.key)) {
				return true;
			}

			currentEntry = currentEntry.next;
		}

		return false;
	}

	/**
	 * Tests if the given <code>value</code> is contained in this {@link SimpleHashtable}.
	 * 
	 * @param value - the value to search for
	 * @return <code>true</code> if the given <code>value</code> was found, <code>false</code> otherwise
	 */
	public boolean containsValue(Object value) {
		for(TableEntry<K, V> entry : table) {
			TableEntry<K, V> currentEntry = entry;

			while(currentEntry != null) {
				if(value.equals(currentEntry.value)) {
					return true;
				}

				currentEntry = currentEntry.next;
			}
		}

		return false;
	}

	/**
	 * Removes the given <code>key</code> from this {@link SimpleHashtable}.
	 * <p>
	 * If no such key was found, the method does nothing.
	 * 
	 * @param key - the key to remove from this table
	 */
	public void remove(Object key) {
		if(key == null) {
			return;
		}

		int slotIndex = abs(key.hashCode()) % table.length;

		TableEntry<K, V> currentEntry = table[slotIndex];
		TableEntry<K, V> previous = currentEntry;

		// the given key's hash-code index may give us an empty slot
		if(currentEntry == null) {
			return;
		}

		// check if this slot's first entry has the given key
		if(key.equals(currentEntry.key)) {
			table[slotIndex] = currentEntry.next;
		}

		// find the given key in this slot's list of entries and remove the entry if found
		while(currentEntry != null) {
			if(key.equals(currentEntry.key)) {
				previous.next = currentEntry.next;
				currentEntry = null;
				size--;
				modificationCount++;
				return;
			}

			previous = currentEntry;
			currentEntry = currentEntry.next;
		}
	}

	/**
	 * Tests if this {@link SimpleHashtable} is empty.
	 * 
	 * @return <code>true</code> if there are no entries in this table, <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Removes all entries from this {@link SimpleHashtable}.
	 * <p>
	 * This method does not modify the table's capacity.
	 */
	@SuppressWarnings("unused")
	public void clear() {
		for(TableEntry<K, V> entry : table) {
			entry = null;
		}

		size = 0;
		modificationCount++;
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	@Override
	public String toString() {
		StringBuilder tableAsText = new StringBuilder();

		tableAsText.append("[");

		for(TableEntry<K, V> entry : table) {
			TableEntry<K, V> currentEntry = entry;

			while(currentEntry != null) {
				tableAsText.append(currentEntry + ", ");
				currentEntry = currentEntry.next;
			}
		}

		tableAsText.delete(tableAsText.length() - 2, tableAsText.length());
		tableAsText.append("]");

		return tableAsText.toString();
	}
}
