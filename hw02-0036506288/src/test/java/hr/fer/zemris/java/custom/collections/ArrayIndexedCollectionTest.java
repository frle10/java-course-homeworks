package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ArrayIndexedCollectionTest {

	@Test
	void testArrayIndexedCollection() {
		assertNotNull(new ArrayIndexedCollection());
		assertEquals(0, new ArrayIndexedCollection().size());
	}
	
	@Test
	void testArrayIndexedCollectionInt() {
		assertNotNull(new ArrayIndexedCollection(10));
		assertNotNull(new ArrayIndexedCollection(1));
		assertEquals(0, new ArrayIndexedCollection(10).size());
		assertEquals(0, new ArrayIndexedCollection(1).size());
		
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(0));
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(-1));
	}
	
	@Test
	void testAdd() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		array.add("Test value...");
		
		assertEquals(1, array.size());
		
		array.add("Another value");
		array.add("Hello world!");
		array.add("Congrats.");
		
		assertEquals(4, array.size());
		assertThrows(NullPointerException.class, () -> array.add(null));
	}
	
	@Test
	void testArrayIndexedCollectionCollection() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		
		assertEquals(0, new ArrayIndexedCollection(array).size());
		
		array.add(3);
		array.add(-5);
		array.add(0);
		
		assertEquals(3, new ArrayIndexedCollection(array).size());
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
	}
	
	@Test
	void testArrayIndexedCollectionCollectionInt() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		
		assertEquals(0, new ArrayIndexedCollection(array, 10).size());
		
		array.add(3);
		array.add(-5);
		array.add(0);
		
		assertEquals(3, new ArrayIndexedCollection(array, 10).size());
		assertEquals(3, new ArrayIndexedCollection(array, 2).size());
		assertEquals(-5, new ArrayIndexedCollection(array, 5).get(1));
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(array, 0));
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
	}
	
	@Test
	void testSize() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		
		assertEquals(0, array.size());
		array.add("My string.");
		array.add(17);
		assertEquals(2, array.size());
		
		array.remove("My string.");
		assertEquals(1, array.size());
	}

	@Test
	void testContains() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		
		assertFalse(array.contains("String."));
		array.add(1);
		array.add(4);
		array.add(3.14);
		assertFalse(array.contains(0));
		assertTrue(array.contains(3.14));
		assertFalse(array.contains(null));
	}

	@Test
	void testRemoveObject() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		array.add(2);
		array.add(3);
		array.add(5);
		array.add(7);
		
		assertFalse(array.remove(Integer.valueOf(1)));
		assertFalse(array.remove(null));
		assertTrue(array.remove(Integer.valueOf(3)));
		assertEquals(3, array.size());
	}

	@Test
	void testToArray() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		array.add(2);
		array.add(3);
		array.add(5);
		array.add(7);
		
		Object[] elements = array.toArray();
		assertEquals(2, elements[0]);
		assertEquals(5, elements[2]);
		assertEquals(4, elements.length);
	}

	@Test
	void testForEach() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		ArrayIndexedCollection array2 = new ArrayIndexedCollection();
		array.add(2);
		array.add(3);
		array.add(5);
		array.add(7);
		
		class TestProcessor extends Processor {
			@Override
			public void process(Object value) {
				array2.add(value);
			}
		}
		
		array.forEach(new TestProcessor());
		assertEquals(4, array2.size());
		assertThrows(NullPointerException.class, () -> array.forEach(null));
	}

	@Test
	void testClear() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		array.add(2);
		array.add(3);
		array.add(5);
		array.add(7);
		
		array.clear();
		assertEquals(0, array.size());
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
	}


	@Test
	void testGet() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		array.add(2);
		array.add(3);
		array.add(5);
		array.add(7);
		
		assertEquals(2, array.get(0));
		assertEquals(5, array.get(2));
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(4));
	}

	@Test
	void testInsert() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		array.add(2);
		array.add(3);
		array.add(5);
		array.add(7);
		
		array.insert(0, 1);
		assertEquals(2, array.get(0));
		assertEquals(0, array.get(1));
		assertEquals(3, array.get(2));
		assertEquals(7, array.get(4));
		
		array.insert(11, 5);
		assertEquals(11, array.get(5));
		
		array.insert("Some string...", 0);
		assertEquals("Some string...", array.get(0));
		assertEquals(2, array.get(1));
		assertEquals(7, array.size());
		
		assertThrows(NullPointerException.class, () -> array.insert(null, 3));
		assertThrows(IndexOutOfBoundsException.class, () -> array.insert("Test", -1));
		assertThrows(IndexOutOfBoundsException.class, () -> array.insert(3.14, 10));
	}

	@Test
	void testRemoveInt() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		array.add(2);
		array.add(3);
		array.add(4);
		array.add(3.14);
		array.add(5);
		array.add(7);
		
		array.remove(0); // remove 2
		assertEquals(5, array.size());
		assertEquals(3, array.get(0));
		
		array.remove(4); // remove 7
		assertEquals(4, array.size());
		
		array.remove(1); // remove 4
		assertEquals(3, array.size());
		assertEquals(3.14, array.get(1));
		
		// only 3, 3.14 and 5 are left
		assertThrows(IndexOutOfBoundsException.class, () -> array.remove(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> array.remove(3));
	}

	@Test
	void testIndexOf() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		array.add(2);
		array.add(3);
		array.add(5);
		array.add(7);
		
		assertEquals(1, array.indexOf(3));
		assertEquals(-1, array.indexOf(3.14));
		assertEquals(3, array.indexOf(7));
		assertEquals(-1, array.indexOf(null));
	}

	@Test
	void testIsEmpty() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		
		assertTrue(array.isEmpty());
		array.add(2);
		assertFalse(array.isEmpty());
	}

	@Test
	void testAddAll() {
		ArrayIndexedCollection array = new ArrayIndexedCollection();
		ArrayIndexedCollection array2 = new ArrayIndexedCollection();
		
		array.add(2);
		array.add(3);
		array.add(5);
		array.add(7);
		
		array2.addAll(array);
		assertFalse(array2.isEmpty());
		assertEquals(4, array2.size());
		assertEquals(5, array2.get(2));
	}

}
