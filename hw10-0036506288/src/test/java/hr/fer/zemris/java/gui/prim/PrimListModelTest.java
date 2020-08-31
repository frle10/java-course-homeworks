package hr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PrimListModelTest {

	@Test
	void testPrimListModel() {
		assertNotNull(new PrimListModel());
		assertEquals(1, new PrimListModel().getSize());
	}

	@Test
	void testNext() {
		PrimListModel model = new PrimListModel();
		model.next();
		model.next();
		assertEquals(3, model.getSize());
		assertEquals(3, model.getElementAt(2));
		
		model.next();
		model.next();
		assertEquals(5, model.getSize());
		assertEquals(7, model.getElementAt(4));
	}

	@Test
	void testGetSize() {
		PrimListModel model = new PrimListModel();
		assertEquals(1, model.getSize());
		
		model.next();
		model.next();
		
		assertEquals(3, model.getSize());
	}

	@Test
	void testGetElementAt() {
		PrimListModel model = new PrimListModel();
		model.next();
		model.next();
		assertEquals(3, model.getElementAt(2));
		
		model.next();
		model.next();
		model.next();
		assertEquals(11, model.getElementAt(5));
	}

	@Test
	void testAddListDataListener() {
		PrimListModel model = new PrimListModel();
		assertThrows(NullPointerException.class, () -> model.addListDataListener(null));
	}

	@Test
	void testRemoveListDataListener() {
		PrimListModel model = new PrimListModel();
		assertDoesNotThrow(() -> model.removeListDataListener(null));
	}

}
