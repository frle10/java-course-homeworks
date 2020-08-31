package hr.fer.zemris.java.custom.collections.demo;

import java.util.ConcurrentModificationException;

import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * This is a demo class that showcases the throwing of a
 * {@link ConcurrentModificationException} when trying to
 * get the next element of our implemented collections
 * if they were previously modified.
 * 
 * @author Ivan Skorupan
 */
public class ConcurrentModificationExceptionDemo {
	
	public static void main(String[] args) {
		Collection col = new LinkedListIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		
		ElementsGetter getter = col.createElementsGetter();
		
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		
		col.clear();
		
		System.out.println("Jedan element: " + getter.getNextElement());
	}
	
}
