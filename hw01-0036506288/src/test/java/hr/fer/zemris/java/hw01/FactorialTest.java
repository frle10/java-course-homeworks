package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.java.hw01.Factorial.*;

import org.junit.jupiter.api.Test;

class FactorialTest {

	@Test
	public void testCalculateFactorial() {
		// test edge values (in the segment allowed in the task, [3, 20])
		assertEquals(6, calculateFactorial(3));
		assertEquals(2432902008176640000L, calculateFactorial(20));
		
		// test some other values
		assertEquals(1, calculateFactorial(0));
		assertEquals(1, calculateFactorial(1));
		assertEquals(24, calculateFactorial(4));
		assertEquals(120, calculateFactorial(5));
		assertEquals(39916800, calculateFactorial(11));
		assertEquals(355687428096000L, calculateFactorial(17));
		
		// test if method throws an exception correctly
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculateFactorial(-1));
		assertEquals("Broj mora biti nenegativan da bi se mogla izvršiti kalkulacija faktorijela!", ex.getMessage());
	}

}
