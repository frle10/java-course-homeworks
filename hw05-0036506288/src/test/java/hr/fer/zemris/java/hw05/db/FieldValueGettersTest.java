package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FieldValueGettersTest {

	@Test
	void testGetFirstName() {
		StudentRecord record = new StudentRecord("123", "Ivić", "Iva", 5);
		
		assertEquals("Iva", FieldValueGetters.FIRST_NAME.get(record));
	}
	
	@Test
	void testGetLastName() {
		StudentRecord record = new StudentRecord("123", "Ivić", "Iva", 5);
		
		assertEquals("Ivić", FieldValueGetters.LAST_NAME.get(record));
	}
	
	@Test
	void testGetJMBAG() {
		StudentRecord record = new StudentRecord("123", "Ivić", "Iva", 5);
		
		assertEquals("123", FieldValueGetters.JMBAG.get(record));
	}

}
