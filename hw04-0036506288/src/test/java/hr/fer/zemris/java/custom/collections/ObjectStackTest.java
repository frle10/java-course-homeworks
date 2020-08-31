package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ObjectStackTest {

	@Test
	void testIsEmpty() {
		ObjectStack<Number> stack = new ObjectStack<>();

		assertTrue(stack.isEmpty());

		stack.push(3);

		assertFalse(stack.isEmpty());
	}

	@Test
	void testSize() {
		ObjectStack<Number> stack = new ObjectStack<>();

		assertEquals(0, stack.size());

		stack.push(3.14);
		stack.push(2L);
		stack.push(18);

		assertEquals(3, stack.size());
	}

	@Test
	void testPush() {
		ObjectStack<Number> stack = new ObjectStack<>();

		stack.push(1.);
		stack.push(-5L);

		assertEquals(2, stack.size());

		assertThrows(NullPointerException.class, () -> stack.push(null));
	}

	@Test
	void testPop() {
		ObjectStack<Number> stack = new ObjectStack<>();

		stack.push(13.78);
		stack.push(18);
		stack.push(0.1);

		assertEquals(0.1, stack.pop());
		assertEquals(2, stack.size());

		stack.pop();
		stack.pop();

		assertEquals(0, stack.size());

		assertThrows(EmptyStackException.class, () -> stack.pop());
	}

	@Test
	void testPeek() {
		ObjectStack<Number> stack = new ObjectStack<>();

		stack.push(13.78);
		stack.push(18);
		stack.push(0.1);

		assertEquals(0.1, stack.peek());
		assertEquals(3, stack.size());

		stack.pop();
		stack.pop();
		stack.pop();

		assertThrows(EmptyStackException.class, () -> stack.peek());
	}

	@Test
	void testClear() {
		ObjectStack<Number> stack = new ObjectStack<>();

		stack.push(13.78);
		stack.push(18);
		stack.push(0.1);
		stack.push(0);
		stack.push(345428L);
		
		stack.clear();
		
		assertEquals(0, stack.size());
	}

}
