package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LinkedListIndexedCollectionTest {
	
	@Test
	void testLinkedListIndexedCollection() {
		assertNotNull(new LinkedListIndexedCollection());
		assertEquals(0, new LinkedListIndexedCollection().size());
	}
	
	@Test
	void testLinkedListIndexedCollectionCollection() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		
		assertEquals(0, new LinkedListIndexedCollection(list).size());
		
		list.add(3);
		list.add(-5);
		list.add(0);
		
		assertEquals(3, new LinkedListIndexedCollection(list).size());
		assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection(null));
	}

	@Test
	void testSize() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		
		assertEquals(0, list.size());
		list.add("My string.");
		list.add(17);
		assertEquals(2, list.size());
		
		list.remove("My string.");
		assertEquals(1, list.size());
	}

	@Test
	void testAdd() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add("Test value...");
		
		assertEquals(1, list.size());
		
		list.add("Another value");
		list.add("Hello world!");
		list.add("Congrats.");
		
		assertEquals(4, list.size());
		assertThrows(NullPointerException.class, () -> list.add(null));
	}

	@Test
	void testContains() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		
		assertFalse(list.contains("String."));
		list.add(1);
		list.add(4);
		list.add(3.14);
		assertFalse(list.contains(0));
		assertTrue(list.contains(3.14));
		assertFalse(list.contains(null));
	}

	@Test
	void testRemoveObject() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add(2);
		list.add(3);
		list.add(5);
		list.add(7);
		
		assertFalse(list.remove(Integer.valueOf(1)));
		assertFalse(list.remove(null));
		assertTrue(list.remove(Integer.valueOf(3)));
		assertEquals(3, list.size());
	}

	@Test
	void testToArray() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add(2);
		list.add(3);
		list.add(5);
		list.add(7);
		
		Object[] elements = list.toArray();
		assertEquals(2, elements[0]);
		assertEquals(5, elements[2]);
		assertEquals(4, elements.length);
	}

	@Test
	void testForEach() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		LinkedListIndexedCollection list2 = new LinkedListIndexedCollection();
		list.add(2);
		list.add(3);
		list.add(5);
		list.add(7);
		
		class TestProcessor extends Processor {
			@Override
			public void process(Object value) {
				list2.add(value);
			}
		}
		
		list.forEach(new TestProcessor());
		assertEquals(4, list2.size());
		assertThrows(NullPointerException.class, () -> list.forEach(null));
	}

	@Test
	void testClear() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add(2);
		list.add(3);
		list.add(5);
		list.add(7);
		
		list.clear();
		assertEquals(0, list.size());
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
	}

	@Test
	void testGet() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add(2);
		list.add(3);
		list.add(5);
		list.add(7);
		
		assertEquals(2, list.get(0));
		assertEquals(5, list.get(2));
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.get(4));
	}

	@Test
	void testIndexOf() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add(2);
		list.add(3);
		list.add(5);
		list.add(7);
		
		assertEquals(1, list.indexOf(3));
		assertEquals(-1, list.indexOf(3.14));
		assertEquals(3, list.indexOf(7));
		assertEquals(-1, list.indexOf(null));
	}

	@Test
	void testInsert() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add(2);
		list.add(3);
		list.add(5);
		list.add(7);
		
		list.insert(0, 1);
		assertEquals(2, list.get(0));
		assertEquals(0, list.get(1));
		assertEquals(3, list.get(2));
		assertEquals(7, list.get(4));
		
		list.insert(11, 5);
		assertEquals(11, list.get(5));
		
		list.insert("Some string...", 0);
		assertEquals("Some string...", list.get(0));
		assertEquals(2, list.get(1));
		assertEquals(7, list.size());
		
		assertThrows(NullPointerException.class, () -> list.insert(null, 3));
		assertThrows(IndexOutOfBoundsException.class, () -> list.insert("Test", -1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.insert(3.14, 10));
	}

	@Test
	void testRemoveInt() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(3.14);
		list.add(5);
		list.add(7);
		
		list.remove(0); // remove 2
		assertEquals(5, list.size());
		assertEquals(3, list.get(0));
		
		list.remove(4); // remove 7
		assertEquals(4, list.size());
		
		list.remove(1); // remove 4
		assertEquals(3, list.size());
		assertEquals(3.14, list.get(1));
		
		// only 3, 3.14 and 5 are left
		assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> list.remove(3));
	}

	@Test
	void testIsEmpty() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		
		assertTrue(list.isEmpty());
		list.add(2);
		assertFalse(list.isEmpty());
	}

	@Test
	void testAddAll() {
		LinkedListIndexedCollection list = new LinkedListIndexedCollection();
		LinkedListIndexedCollection list2 = new LinkedListIndexedCollection();
		
		list.add(2);
		list.add(3);
		list.add(5);
		list.add(7);
		
		list2.addAll(list);
		assertFalse(list2.isEmpty());
		assertEquals(4, list2.size());
		assertEquals(5, list2.get(2));
	}

}
