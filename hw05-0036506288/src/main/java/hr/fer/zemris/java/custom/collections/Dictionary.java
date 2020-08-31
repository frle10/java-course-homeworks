package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Models an object that under a key remembers a given value.
 * <p>
 * This model is also known as a map, associative field and dictionary
 * depending on the programming language.
 * 
 * @author Ivan Skorupan
 *
 * @param <K> - type of key
 * @param <V> - type of value
 */
public class Dictionary<K, V> {
	
	/**
	 * An array containing this dictionary's entries.
	 */
	private ArrayIndexedCollection<Entry<K, V>> entries = new ArrayIndexedCollection<>();
	
	/**
	 * Nested static class that models one entry in {@link Dictionary}.
	 * 
	 * @author Ivan Skorupan
	 *
	 * @param <K> - type of key
	 * @param <V> - type of value
	 */
	private static class Entry<K, V> {
		/**
		 * The key of this entry.
		 */
		private K key;
		
		/**
		 * The value stored under this entry.
		 */
		private V value;
		
		/**
		 * Constructs a new {@link Entry} object with given <code>key</code>
		 * and <code>value</code>.
		 * <p>
		 * The <code>key</code> cannot be <code>null</code>, but <code>value</code>
		 * can.
		 * 
		 * @param key - this entrie's key
		 * @param value - this entrie's value
		 * @throws NullPointerException if <code>key</code> is <code>null</code>
		 */
		public Entry(K key, V value) {
			this.key = Objects.requireNonNull(key);
			this.value = value;
		}
	}
	
	/**
	 * Tests if the dictionary is empty.
	 * 
	 * @return <code>true</code> if this dictionary is empty, <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return entries.size() == 0;
	}
	
	/**
	 * Returns the number of entries in this dictionary.
	 * 
	 * @return number of entries in the dictionary
	 */
	public int size() {
		return entries.size();
	}
	
	/**
	 * Removes all entries from this dictionary.
	 */
	public void clear() {
		entries.clear();
	}
	
	/**
	 * Puts a new entry in this dictionary.
	 * <p>
	 * If an entry with the same <code>key</code> already exists, this new one
	 * overwrites it.
	 * 
	 * @param key - new entry's key
	 * @param value - new entry's value
	 * @throws NullPointerException if <code>key</code> is <code>null</code>
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key);
		
		int index = 0;
		
		while(index < entries.size()) {
			Entry<K, V> currentEntry = entries.get(index++);
			
			if(key.equals(currentEntry.key)) {
				currentEntry.value = value;
				return;
			}
		}
		
		entries.add(new Entry<K, V>(key, value));
	}
	
	/**
	 * Returns the value stored under the given key.
	 * <p>
	 * If no such value exists, it returns <code>null</code> (either
	 * the <code>key</code> exists, but the value is <code>null</code>
	 * or the key does not exist at all).
	 * 
	 * @param key - the key whose value to search for
	 * @return the value under the given <code>key</code>
	 * @throws NullPointerException if <code>key</code> is <code>null</code>
	 */
	public V get(Object key) {
		Objects.requireNonNull(key);
		
		int index = 0;
		
		while(index < entries.size()) {
			Entry<K, V> currentEntry = entries.get(index++);
			
			if(key.equals(currentEntry.key)) {
				return currentEntry.value;
			}
		}
		
		return null;
	}
	
}