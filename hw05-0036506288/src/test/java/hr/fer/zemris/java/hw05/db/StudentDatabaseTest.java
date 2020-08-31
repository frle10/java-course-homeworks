package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class StudentDatabaseTest {
	
	@Test
	void testForJMBAG() {
		String table = "001 Ivkić Ivo 5\n"
				+ "003 Merkić    Marta 2\n"
				+ "123 Lukić Luka 1\n"
				+ "124 Malonjić Menonja 3\n";
		
		List<String> records = table.lines().collect(Collectors.toList());
		
		StudentDatabase database = new StudentDatabase(records);
		
		assertEquals(new StudentRecord("123", "Lukić", "Luka", 1), database.forJMBAG("123"));
		assertEquals(new StudentRecord("001", "Ivkić", "Ivo", 5), database.forJMBAG("001"));
		
		assertThrows(NullPointerException.class, () -> database.forJMBAG(null));
	}
	
	@Test
	void testFilterOnlyTrue() {
		String table = "001 Ivkić Ivo 5\n"
				+ "003 Merkić    Marta 2\n"
				+ "123 Lukić Luka 1\n"
				+ "124 Malonjić Menonja 3\n";
		
		List<String> records = table.lines().collect(Collectors.toList());
		
		StudentDatabase database = new StudentDatabase(records);
		
		List<StudentRecord> acceptedRecords = database.filter(r -> {return true;});
		
		assertEquals(4, acceptedRecords.size());
	}
	
	@Test
	void testFilterOnlyFalse() {
		String table = "001 Ivkić Ivo 5\n"
				+ "003 Merkić    Marta 2\n"
				+ "123 Lukić Luka 1\n"
				+ "124 Malonjić Menonja 3\n";
		
		List<String> records = table.lines().collect(Collectors.toList());
		
		StudentDatabase database = new StudentDatabase(records);
		
		List<StudentRecord> acceptedRecords = database.filter(r -> {return false;});
		
		assertEquals(0, acceptedRecords.size());
	}
	
}
