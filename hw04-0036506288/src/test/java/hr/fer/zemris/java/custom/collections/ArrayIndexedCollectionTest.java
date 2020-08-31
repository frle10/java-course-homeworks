package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class ArrayIndexedCollectionTest {

	@Test
	void testArrayIndexedCollection() {
		assertNotNull(new ArrayIndexedCollection<String>());
		assertNotNull(new ArrayIndexedCollection<Number>());
		assertNotNull(new ArrayIndexedCollection<Object>());

		assertDoesNotThrow(() -> new ArrayIndexedCollection<List<Integer>>());
	}

	@Test
	void testArrayIndexedCollectionInt() {
		assertNotNull(new ArrayIndexedCollection<Double>(20));

		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<RuntimeException>(0));
	}

	@Test
	void testArrayIndexedCollectionCollectionOfQextendsT() {
		ArrayIndexedCollection<String> other = new ArrayIndexedCollection<String>();

		assertNotNull(new ArrayIndexedCollection<String>(other));

		other = new ArrayIndexedCollection<>(30);

		assertNotNull(new ArrayIndexedCollection<String>(other));

		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<Boolean>(null));
	}

	@Test
	void testArrayIndexedCollectionCollectionOfQextendsTInt() {
		ArrayIndexedCollection<String> other = new ArrayIndexedCollection<String>();

		assertNotNull(new ArrayIndexedCollection<String>(other, 20));
		assertNotNull(new ArrayIndexedCollection<String>(other, 1));

		other = new ArrayIndexedCollection<>(30);

		assertNotNull(new ArrayIndexedCollection<String>(other, 20));

		ArrayIndexedCollection<Integer> array2 = new ArrayIndexedCollection<>(50);

		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<Boolean>(null, 0));
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection<Number>(array2, -3));
	}

	@Test
	void testSize() {
		ArrayIndexedCollection<String> array = new ArrayIndexedCollection<>();
		
		assertEquals(0, array.size());

		array = new ArrayIndexedCollection<>(27);
		assertEquals(0, array.size());

		array = new ArrayIndexedCollection<String>(1);
		assertEquals(0, array.size());
		
		array.add("1");
		array.add("2");
		array.add("5");
		
		assertEquals(3, array.size());
	}

	@Test
	void testAdd() {
		ArrayIndexedCollection<String> array = new ArrayIndexedCollection<>(2);

		array.add("Ivan");
		array.add("Frle");

		assertEquals(2, array.size());

		array.add("-----");

		assertEquals(3, array.size());

		assertThrows(NullPointerException.class, () -> array.add(null));
	}

	@Test
	void testGet() {
		ArrayIndexedCollection<Number> array = new ArrayIndexedCollection<>(2);

		array.add(5);
		array.add(-3.);

		assertEquals(-3., array.get(1));

		array.add(378L);

		assertEquals(378L, array.get(2));
		assertEquals(5, array.get(0));

		assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(5));
	}

	@Test
	void testContains() {
		ArrayIndexedCollection<Double> doubles = new ArrayIndexedCollection<Double>(45);

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
		ArrayIndexedCollection<String> array = new ArrayIndexedCollection<String>();

		array.insert("boeing747", 0);
		assertEquals(1, array.size());
		assertEquals("boeing747", array.get(0));

		array.add("Frle");
		array.add("Yoo!");

		array.insert("Još jedan string.", 1);
		assertEquals("Još jedan string.", array.get(1));
		assertEquals("Yoo!", array.get(3));

		assertThrows(NullPointerException.class, () -> array.insert(null, 2));
		assertThrows(IndexOutOfBoundsException.class, () -> array.insert("P", -1));
		assertThrows(IndexOutOfBoundsException.class, () -> array.insert("Ha!", 7));
	}

	@Test
	void testRemoveObject() {
		ArrayIndexedCollection<String> items = new ArrayIndexedCollection<String>();

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
		ArrayIndexedCollection<String> items = new ArrayIndexedCollection<String>();

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
		ArrayIndexedCollection<String> items = new ArrayIndexedCollection<String>();

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
		ArrayIndexedCollection<String> items = new ArrayIndexedCollection<String>();

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
		ArrayIndexedCollection<String> items = new ArrayIndexedCollection<String>();

		items.add("Hey now!");
		items.add("thenewboston");
		items.add("hee");
		
		assertNotNull(items.createElementsGetter());
	}

	@Test
	void testClear() {
		ArrayIndexedCollection<String> items = new ArrayIndexedCollection<String>();

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
