package hr.fer.zemris.java.custom.collections.demo;

import java.util.Iterator;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;
import hr.fer.zemris.java.custom.collections.SimpleHashtable.TableEntry;

public class Primjer {

	public static void main(String[] args) {
		// create collection:
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);

		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		//				// should throw IllegalStateException
		//				Iterator<TableEntry<String,Integer>> iter3 = examMarks.iterator();
		//				while(iter3.hasNext()) {
		//					TableEntry<String,Integer> pair = iter3.next();
		//					if(pair.getKey().equals("Ivana")) {
		//						iter3.remove();
		//						iter3.remove();
		//					}
		//				}

		//				// should throw ConcurrentModificationException
		//				Iterator<SimpleHashtable.TableEntry<String,Integer>> iter4 = examMarks.iterator();
		//				while(iter4.hasNext()) {
		//					SimpleHashtable.TableEntry<String,Integer> pair = iter4.next();
		//					if(pair.getKey().equals("Ivana")) {
		//						examMarks.remove("Ivana");
		//					}
		//				}


		for(TableEntry<String,Integer> pair : examMarks) {
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
		}

		System.out.println();

		for(TableEntry<String,Integer> pair1 : examMarks) {
			for(TableEntry<String,Integer> pair2 : examMarks) {
				System.out.printf("(%s => %d) - (%s => %d)%n", pair1.getKey(), pair1.getValue(), pair2.getKey(), pair2.getValue());
			}
		}

		System.out.println();
		System.out.println(examMarks); // print the table

		Iterator<TableEntry<String,Integer>> iter = examMarks.iterator();
		while(iter.hasNext()) {
			TableEntry<String,Integer> pair = iter.next();
			if(pair.getKey().equals("Ivana")) {
				iter.remove(); // sam iterator kontrolirano uklanja trenutni element
			}
		}

		System.out.print("Removed Ivana: ");
		System.out.println(examMarks); // print exam marks table without "Ivana"

		System.out.println();

		// print all pairs in this table and then clear it
		Iterator<TableEntry<String,Integer>> iter2 = examMarks.iterator();
		while(iter2.hasNext()) {
			TableEntry<String,Integer> pair = iter2.next();
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
			iter2.remove();
		}
		System.out.printf("Veličina: %d%n", examMarks.size());
	}

}
