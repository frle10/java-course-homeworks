package hr.fer.zemris.java.custom.collections.demo;

import java.util.Arrays;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;
import hr.fer.zemris.java.custom.collections.Processor;


/**
 * A helper class that implements some code in the <code>main</code> method to test
 * collection classes developed in {@link hr.fer.zemris.java.custom.collections}
 * package.
 * <p>
 * The program does not expect any command line arguments.
 * 
 * @author Ivan Skorupan
 */
public class CollectionDemo {

	/**
	 * Implements some code to showcase functionality of subclasses of
	 * the {@link Collection} class.
	 * 
	 * @param args - command line arguments array, not used
	 */
	public static void main(String[] args) {
		ArrayIndexedCollection col = new ArrayIndexedCollection(2);
		col.add(Integer.valueOf(20));
		col.add("New York");
		col.add("San Francisco"); // here the internal array is reallocated to 4
		System.out.println(col.contains("New York")); // writes: true
		col.remove(1); // removes "New York"; shifts "San Francisco" to position 1
		System.out.println(col.get(1)); // writes: "San Francisco"
		System.out.println(col.size()); // writes: 2
		col.add("Los Angeles");
		
		LinkedListIndexedCollection col2 = new LinkedListIndexedCollection(col);
		
		/**
		 * This is a local class representing a processor which writes objects to System.out
		 * 
		 * @author Ivan Skorupan
		 * @see {@link Processor}
		 */
		class P implements Processor {
			
			/**
			 * {@inheritDoc}
			 * 
			 * <p>
			 * This implementation of the <code>process</code> method prints the passed object out on the screen.
			 */
			@Override
			public void process(Object o) {
				System.out.println(o);
			}
			
		}
		
		System.out.println("col elements:");
		col.forEach(new P());
		
		System.out.println("col elements again:");
		System.out.println(Arrays.toString(col.toArray()));
		
		System.out.println("col2 elements:");
		col2.forEach(new P());
		
		System.out.println("col2 elements again:");
		System.out.println(Arrays.toString(col2.toArray()));
		
		System.out.println(col.contains(col2.get(1))); // true
		System.out.println(col2.contains(col.get(1))); // true
		
		col.remove(Integer.valueOf(20)); // removes 20 from collection (at position 0)
	}

}
