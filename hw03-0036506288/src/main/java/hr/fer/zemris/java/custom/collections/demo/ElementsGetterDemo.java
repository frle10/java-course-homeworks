package hr.fer.zemris.java.custom.collections.demo;

import java.util.NoSuchElementException;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Collection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;

/**
 * This demo program contains several examples copy pasted from the homework .pdf file.
 * <p>
 * Some of these examples originally throw an exception. Those exceptions were caught here
 * since if they were directly thrown at the user, the rest of the examples would not run.
 * 
 * @author Ivan Skorupan
 */
public class ElementsGetterDemo {
	
	public static void main(String[] args) {
		/*
		 * Example 1
		 */
		System.out.println("Example 1:");
		System.out.println();
		
		Collection col = new ArrayIndexedCollection(); // i.e. new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		ElementsGetter getter = col.createElementsGetter();

		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());

		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());

		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());

		try {
			System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
			System.out.println("Jedan element: " + getter.getNextElement());
		} catch(NoSuchElementException ex) {
			System.out.println("Ovdje se baca iznimka.");
		}
		
		System.out.println("---------------------------------------");
		/* ----------------------------------------------------------------------------- */
		
		/*
		 * Example 2
		 */
		System.out.println("Example 2:");
		System.out.println();
		
		col = new LinkedListIndexedCollection(); // i.e. new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		getter = col.createElementsGetter();

		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());

		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());

		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());

		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());
		System.out.println("Ima nepredanih elemenata: " + getter.hasNextElement());

		System.out.println("---------------------------------------");
		/* ----------------------------------------------------------------------------- */
		
		/*
		 * Example 3
		 */
		System.out.println("Example 3:");
		System.out.println();
		
		col = new ArrayIndexedCollection(); // i.e. new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");

		getter = col.createElementsGetter();

		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		
		try {
			System.out.println("Jedan element: " + getter.getNextElement());
		} catch(NoSuchElementException ex) {
			System.out.println("I tu se baca iznimka.");
		}
		
		System.out.println("---------------------------------------");
		/* ----------------------------------------------------------------------------- */
		
		/*
		 * Example 4
		 */
		System.out.println("Example 4:");
		System.out.println();
		
		col = new LinkedListIndexedCollection(); // i.e. new ArrayIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		
		ElementsGetter getter1 = col.createElementsGetter();
		ElementsGetter getter2 = col.createElementsGetter();
		
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
		
		System.out.println("---------------------------------------");
		/* ----------------------------------------------------------------------------- */
		
		/*
		 * Example 5
		 */
		System.out.println("Example 5:");
		System.out.println();
		
		Collection col1 = new ArrayIndexedCollection();
		Collection col2 = new ArrayIndexedCollection();
		col1.add("Ivo");
		col1.add("Ana");
		col1.add("Jasna");
		col2.add("Jasmina");
		col2.add("Štefanija");
		col2.add("Karmela");
		
		getter1 = col1.createElementsGetter();
		getter2 = col1.createElementsGetter();
		ElementsGetter getter3 = col2.createElementsGetter();
		
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter1.getNextElement());
		System.out.println("Jedan element: " + getter2.getNextElement());
		System.out.println("Jedan element: " + getter3.getNextElement());
		System.out.println("Jedan element: " + getter3.getNextElement());
	}

}
