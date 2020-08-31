package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.java.hw01.UniqueNumbers.*;

import org.junit.jupiter.api.Test;
import hr.fer.zemris.java.hw01.UniqueNumbers.TreeNode;

class UniqueNumbersTest {
	
	@Test
	void testContainsValue() {
		TreeNode glava = null;
		assertFalse(containsValue(glava, 7));
		
		glava = addNode(glava, 30);
		glava = addNode(glava, -5);
		glava = addNode(glava, 7);
		glava = addNode(glava, 0);
		glava = addNode(glava, -4);
		
		assertTrue(containsValue(glava, -5));
		assertFalse(containsValue(glava, 3));
	}

	@Test
	void testTreeSize() {
		TreeNode glava = null;
		assertEquals(0, treeSize(glava));
		
		glava = addNode(glava, 30);
		glava = addNode(glava, -5);
		glava = addNode(glava, 7);
		glava = addNode(glava, 7);
		glava = addNode(glava, 0);
		glava = addNode(glava, -4);
		glava = addNode(glava, 30);
		
		assertEquals(5, treeSize(glava));
	}

	@Test
	void testAddNode() {
		TreeNode glava = null;
		assertNull(glava);
		
		glava = addNode(glava, 17);
		glava = addNode(glava, -3);
		glava = addNode(glava, 8);
		glava = addNode(glava, 8);
		glava = addNode(glava, 0);
		glava = addNode(glava, -4);
		glava = addNode(glava, 30);
		
		assertNotNull(glava);
		assertEquals(6, treeSize(glava));
	}

}
