package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SimpleHashtableTest {

	@Test
	void testSimpleHashtable() {
		SimpleHashtable<String, Number> table = new SimpleHashtable<>();

		assertNotNull(table);
		assertEquals(0, table.size());
		assertTrue(table.isEmpty());
	}

	@Test
	void testSimpleHashtableInt() {
		SimpleHashtable<String, Number> table = new SimpleHashtable<>(20);

		assertNotNull(table);
		assertEquals(0, table.size());
		assertTrue(table.isEmpty());

		assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<String, String>(0));
		assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<Double, Integer>(-1));
		assertDoesNotThrow(() -> new SimpleHashtable<List<Integer>, Number>(1));
	}

	@Test
	void testPut() {
		SimpleHashtable<String, Number> table = new SimpleHashtable<>();

		table.put("Prvi", 1.);
		table.put("Wow!", -3);
		table.put("Osam", 8.1);
		table.put("...", 3.14);
		table.put("87000", 0);
		
		assertEquals(5, table.size());
		
		table.put("Prvi", 2.);
		
		assertEquals(5, table.size());
		
		table.put("New", -50.7);
		
		assertEquals(6, table.size());
		
		assertThrows(NullPointerException.class, () -> table.put(null, 7));
	}

	@Test
	void testGet() {
		SimpleHashtable<String, Number> table = new SimpleHashtable<>();

		table.put("Prvi", 1.);
		table.put("Wow!", -3);
		table.put("Osam", 8.1);
		table.put("...", 3.14);
		table.put("87000", 0);
		
		assertEquals(-3, table.get("Wow!"));
		assertEquals(null, table.get("Wow"));
		assertEquals(null, table.get(null));
	}

	@Test
	void testSize() {
		SimpleHashtable<String, Number> table = new SimpleHashtable<>();

		assertEquals(0, table.size());

		table.put("Prvi", 1.);
		table.put("Wow!", -3);

		assertEquals(2, table.size());
	}

	@Test
	void testContainsKey() {
		SimpleHashtable<String, Number> table = new SimpleHashtable<>();

		table.put("Prvi", 1.);
		table.put("Wow!", -3);

		assertTrue(table.containsKey("Prvi"));
		assertFalse(table.containsKey("Nema me..."));
	}

	@Test
	void testContainsValue() {
		SimpleHashtable<String, Number> table = new SimpleHashtable<>();

		table.put("Prvi", 1.);
		table.put("Wow!", -3);

		assertTrue(table.containsValue(-3));
		assertFalse(table.containsValue(3.14));
	}

	@Test
	void testRemove() {
		SimpleHashtable<String, Number> table = new SimpleHashtable<>();

		table.put("Prvi", 1.);
		table.put("Wow!", -3);
		table.put("Osam", 8.1);
		table.put("...", 3.14);
		table.put("87000", 0);

		table.remove("Osam");

		assertFalse(table.containsKey("Osam"));
		assertEquals(4, table.size());
	}

	@Test
	void testIsEmpty() {
		SimpleHashtable<String, Number> table = new SimpleHashtable<>();

		assertTrue(table.isEmpty());

		table.put("Prvi", 1.);
		table.put("Wow!", -3);

		assertFalse(table.isEmpty());
	}

	@Test
	void testClear() {
		SimpleHashtable<String, Number> table = new SimpleHashtable<>();

		table.put("Prvi", 1.);
		table.put("Wow!", -3);
		table.put("Osam", 8.1);
		table.put("...", 3.14);
		table.put("87000", 0);
		
		table.clear();
		
		assertTrue(table.isEmpty());
		assertEquals(0, table.size());
	}

	@Test
	void testIterator() {
		SimpleHashtable<String, Number> table = new SimpleHashtable<>();

		table.put("Prvi", 1.);
		table.put("Wow!", -3);
		table.put("Osam", 8.1);
		table.put("...", 3.14);
		table.put("87000", 0);
		
		assertNotNull(table.iterator());
	}
}
