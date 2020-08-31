package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DictionaryTest {

	@Test
	void testIsEmpty() {
		Dictionary<Integer, String> dictionary = new Dictionary<>();

		assertTrue(dictionary.isEmpty());

		dictionary.put(1, "Frle");

		assertFalse(dictionary.isEmpty());
	}

	@Test
	void testSize() {
		Dictionary<Integer, String> dictionary = new Dictionary<>();

		assertEquals(0, dictionary.size());

		dictionary.put(1, "Frle");
		dictionary.put(5, "Ivan");
		dictionary.put(3, "Marko");
		dictionary.put(2, "Ja sam koza!");
		dictionary.put(8, "LOL");

		assertEquals(5, dictionary.size());
	}

	@Test
	void testClear() {
		Dictionary<Integer, String> dictionary = new Dictionary<>();

		dictionary.put(1, "Frle");
		dictionary.put(5, "Ivan");
		dictionary.put(3, "Marko");
		dictionary.put(2, "Ja sam koza!");
		dictionary.put(8, "LOL");

		dictionary.clear();

		assertEquals(0, dictionary.size());
	}

	@Test
	void testPut() {
		Dictionary<Integer, String> dictionary = new Dictionary<>();

		dictionary.put(1, "Frle");
		dictionary.put(5, "Ivan");
		dictionary.put(3, "Marko");
		dictionary.put(2, "Ja sam koza!");
		dictionary.put(8, "LOL");

		assertEquals("Ja sam koza!", dictionary.get(2));
		assertEquals(5, dictionary.size());

		dictionary.put(5, "Skorupan");
		assertEquals("Skorupan", dictionary.get(5));

		assertEquals(5, dictionary.size());

		assertThrows(NullPointerException.class, () -> dictionary.put(null, "Neki string..."));
	}

	@Test
	void testGet() {
		Dictionary<Integer, String> dictionary = new Dictionary<>();
		
		dictionary.put(1, "Frle");
		dictionary.put(5, "Ivan");
		dictionary.put(3, "Marko");
		dictionary.put(2, "Ja sam koza!");
		dictionary.put(8, "LOL");
		
		assertEquals("Frle", dictionary.get(1));
		assertEquals(null, dictionary.get(3.14));
		assertEquals(null, dictionary.get(7));
		
		assertThrows(NullPointerException.class, () -> dictionary.get(null));
	}

}
