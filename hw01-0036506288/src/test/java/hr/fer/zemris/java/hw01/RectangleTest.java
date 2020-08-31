package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.java.hw01.Rectangle.*;


import org.junit.jupiter.api.Test;

class RectangleTest {

	@Test
	void testCalculateRectangleArea() {
		assertEquals(21., calculateRectangleArea(3, 7));
		assertEquals(0, calculateRectangleArea(0, 35));
		assertEquals(0, calculateRectangleArea(20, 0));
		assertEquals(33.06734, calculateRectangleArea(3.14, 10.531), 10e-6);
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculateRectangleArea(-2, 3));
		assertEquals("Širina i visina pravokutnika moraju biti nenegativni realni brojevi.", ex.getMessage());
	}

	@Test
	void testCalculateRectanglePerimeter() {
		assertEquals(20, calculateRectanglePerimeter(3, 7));
		assertEquals(35, calculateRectanglePerimeter(0, 35));
		assertEquals(20, calculateRectanglePerimeter(20, 0));
		assertEquals(27.342, calculateRectanglePerimeter(3.14, 10.531), 10e-6);
		
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> calculateRectanglePerimeter(-3, 20));
		assertEquals("Širina i visina pravokutnika moraju biti nenegativni realni brojevi.", ex.getMessage());
	}

	@Test
	void testCheckRectangleValidity() {
		assertTrue(checkRectangleValidity(0, 3.17));
		assertTrue(checkRectangleValidity(5.26, 0));
		assertTrue(checkRectangleValidity(0, 0));
		assertTrue(checkRectangleValidity(4.138, 3.17));
		assertFalse(checkRectangleValidity(-3, 3.17));
		assertFalse(checkRectangleValidity(4, -3));
		assertFalse(checkRectangleValidity(-1, -0.5));
	}

	@Test
	void testCheckDimensionDomain() {
		assertTrue(checkDimensionDomain(0));
		assertTrue(checkDimensionDomain(3.14));
		assertFalse(checkDimensionDomain(-7));
	}

	@Test
	void testGetRectangleDimensionFromCommandLine() {
		String[] argumentArray = {"3.14", "-5.28"};
		
		assertEquals(3.14, getRectangleDimensionFromCommandLine(argumentArray[0]), 10e-6);
		assertThrows(IllegalArgumentException.class, () -> getRectangleDimensionFromCommandLine(argumentArray[1]));
		
		String[] argumentArray2 = {"štefica", "-3"};
		assertThrows(NumberFormatException.class, () -> getRectangleDimensionFromCommandLine(argumentArray2[0]));
	}

}
