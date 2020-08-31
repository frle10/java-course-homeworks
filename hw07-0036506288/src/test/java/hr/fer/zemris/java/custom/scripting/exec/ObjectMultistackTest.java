package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import java.util.EmptyStackException;

import org.junit.jupiter.api.Test;

class ObjectMultistackTest {

	@Test
	void testObjectMultistack() {
		assertNotNull(new ObjectMultistack());
		assertEquals(true, new ObjectMultistack().isEmpty("key"));
	}

	@Test
	void testPush() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.push("key", new ValueWrapper(3.14));
		multistack.push("another", new ValueWrapper("Boeing747"));
		multistack.push("yeiii", new ValueWrapper(new Object()));
		
		multistack.push("another", new ValueWrapper(78));
		
		assertEquals(false, multistack.isEmpty("key"));
		assertEquals(false, multistack.isEmpty("another"));
		assertEquals(true, multistack.isEmpty("someKey"));
		
		assertEquals(3.14, multistack.peek("key").getValue());
		assertEquals(78, multistack.pop("another").getValue());
		
		assertThrows(NullPointerException.class, () -> multistack.push(null, new ValueWrapper(null)));
	}

	@Test
	void testPop() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.push("key", new ValueWrapper(3.14));
		multistack.push("another", new ValueWrapper("Boeing747"));
		multistack.push("yeiii", new ValueWrapper(new Object()));
		
		multistack.push("another", new ValueWrapper(78));
		
		assertEquals(78, multistack.pop("another").getValue());
		assertEquals("Boeing747", multistack.pop("another").getValue());
		
		assertThrows(EmptyStackException.class, () -> multistack.pop("another"));
		assertThrows(NullPointerException.class, () -> multistack.pop(null));
	}

	@Test
	void testPeek() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.push("key", new ValueWrapper(3.14));
		multistack.push("another", new ValueWrapper("Boeing747"));
		multistack.push("yeiii", new ValueWrapper(new Object()));
		
		multistack.push("another", new ValueWrapper(78));
		
		assertEquals(78, multistack.peek("another").getValue());
		assertEquals(3.14, multistack.peek("key").getValue());
		
		assertThrows(NullPointerException.class, () -> multistack.peek(null));
		assertThrows(EmptyStackException.class, () -> multistack.peek("nonexisting"));
	}

	@Test
	void testIsEmpty() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		multistack.push("key", new ValueWrapper(3.14));
		multistack.push("another", new ValueWrapper("Boeing747"));
		multistack.push("yeiii", new ValueWrapper(new Object()));
		
		multistack.push("another", new ValueWrapper(78));
		
		assertEquals(true, multistack.isEmpty("nonexisting"));
		assertEquals(false, multistack.isEmpty("another"));
		assertEquals(false, multistack.isEmpty("yeiii"));
		
		assertThrows(NullPointerException.class, () -> multistack.isEmpty(null));
	}

}
