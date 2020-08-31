package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class LinkedListIndexedCollectionTest {

	@Test
	void testLinkedListIndexedCollection() {
		assertNotNull(new LinkedListIndexedCollection<String>());
	}

	@Test
	void testLinkedListIndexedCollectionCollectionOfQextendsT() {
		ArrayIndexedCollection<String> other = new ArrayIndexedCollection<>();
		
		other.add("bla");
		other.add("yo");
		other.add("haha");
		other.add("ja sam naj");
		other.add("ena je naj");
		other.add("nemam komentara");
		
		assertNotNull(new LinkedListIndexedCollection<String>(other));
		
		assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection<Boolean>(null));
	}

	@Test
	void testSize() {
		LinkedListIndexedCollection<String> list = new LinkedListIndexedCollection<>();
		
		assertEquals(0, list.size());
		
		list.add("1");
		list.add("2");
		list.add("5");
		
		assertEquals(3, list.size());
	}

	@Test
	void testAdd() {
		LinkedListIndexedCollection<String> list = new LinkedListIndexedCollection<>();

		list.add("Ivan");
		list.add("Frle");

		assertEquals(2, list.size());

		list.add("-----");

		assertEquals(3, list.size());

		assertThrows(NullPointerException.class, () -> list.add(null));
	}

	@Test
	void testGet() {
		LinkedListIndexedCollection<Number> list = new LinkedListIndexedCollection<>();

		list.add(5);
		list.add(-3.);

		assertEquals(-3., list.get(1));

		list.add(378L);

		assertEquals(378L, list.get(2));
		assertEquals(5, list.get(0));

		assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
	}

	@Test
	void testContains() {
		LinkedListIndexedCollection<Double> doubles = new LinkedListIndexedCollection<Double>();

		doubles.add(3.14);
		doubles.add(1.);
		doubles.add(-7.);
		doubles.add(10.5);

		assertTrue(doubles.contains(1.));
		assertTrue(doubles.contains(10.5));

		assertFalse(doubles.contains(0));
		assertFalse(doubles.contains(3.145));
		assertFalse(doubles.contains("boeing747"));
		assertFalse(doubles.contains(3L));
	}

	@Test
	void testInsert() {
		LinkedListIndexedCollection<String> list = new LinkedListIndexedCollection<String>();

		list.insert("boeing747", 0);
		assertEquals(1, list.size());
		assertEquals("boeing747", list.get(0));

		list.add("Frle");
		list.add("Yoo!");

		list.insert("Još jedan string.", 1);
		assertEquals("Još jedan string.", list.get(1));
		assertEquals("Yoo!", list.get(3));

		assertThrows(NullPointerException.class, () -> list.insert(null, 2));
		assertThrows(IndexOutOfBoundsException.class, () -> list.insert("P", -1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.insert("Ha!", 7));
	}

	@Test
	void testRemoveObject() {
		LinkedListIndexedCollection<String> items = new LinkedListIndexedCollection<String>();

		items.add("Hey now!");
		items.add("thenewboston");
		items.add("hee");
		items.add("nisam ja");
		items.add("struja misli");
		items.add("Kafka Franc");
		items.add("Jaoo");

		assertTrue(items.remove("hee"));
		assertTrue(items.remove("Kafka Franc"));
		assertTrue(items.remove("thenewboston"));

		assertFalse(items.remove(null));
		assertFalse(items.remove(Integer.valueOf(7)));
		assertFalse(items.remove("Mene nema."));
	}

	@Test
	void testRemoveInt() {
		LinkedListIndexedCollection<String> items = new LinkedListIndexedCollection<String>();

		items.add("Hey now!");
		items.add("thenewboston");
		items.add("hee");
		items.add("nisam ja");
		items.add("struja misli");
		items.add("Kafka Franc");
		items.add("Jaoo");

		items.remove(2);
		assertFalse(items.contains("hee"));

		assertThrows(IndexOutOfBoundsException.class, () -> items.remove(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> items.remove(7));
	}

	@Test
	void testIndexOf() {
		LinkedListIndexedCollection<String> items = new LinkedListIndexedCollection<String>();

		items.add("Hey now!");
		items.add("thenewboston");
		items.add("hee");
		items.add("nisam ja");
		items.add("struja misli");
		items.add("Kafka Franc");
		items.add("Jaoo");
		
		assertEquals(4, items.indexOf("struja misli"));
		assertEquals(0, items.indexOf("Hey now!"));
		
		assertEquals(-1, items.indexOf(null));
		assertEquals(-1, items.indexOf("mene opet nemaaa"));
		assertEquals(-1, items.indexOf(Double.valueOf(7.)));
	}

	@Test
	void testToArray() {
		LinkedListIndexedCollection<String> items = new LinkedListIndexedCollection<String>();

		items.add("Hey now!");
		items.add("thenewboston");
		items.add("hee");
		items.add("nisam ja");
		items.add("struja misli");
		items.add("Kafka Franc");
		items.add("Jaoo");
		
		Object[] expected = new Object[] {"Hey now!", "thenewboston", "hee", "nisam ja", "struja misli", "Kafka Franc", "Jaoo"};
		Object[] actual = items.toArray();
		
		assertTrue(Arrays.equals(expected, actual));
	}

	@Test
	void testCreateElementsGetter() {
		LinkedListIndexedCollection<String> items = new LinkedListIndexedCollection<String>();

		items.add("Hey now!");
		items.add("thenewboston");
		items.add("hee");
		
		assertNotNull(items.createElementsGetter());
	}

	@Test
	void testClear() {
		LinkedListIndexedCollection<String> items = new LinkedListIndexedCollection<String>();

		items.add("Hey now!");
		items.add("thenewboston");
		items.add("hee");
		items.add("nisam ja");
		items.add("struja misli");
		items.add("Kafka Franc");
		items.add("Jaoo");
		
		items.clear();
		
		assertEquals(0, items.size());
	}

}
