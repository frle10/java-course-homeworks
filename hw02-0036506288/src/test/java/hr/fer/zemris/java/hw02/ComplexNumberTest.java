package hr.fer.zemris.java.hw02;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.java.hw02.ComplexNumber.*;

import org.junit.jupiter.api.Test;

class ComplexNumberTest {

	private static final double delta = 1e-6;
	
	@Test
	void testComplexNumber() {
		assertNotNull(new ComplexNumber(1.1, -1.3));
		assertEquals(1.1, new ComplexNumber(1.1, -1.3).getReal(), delta);
		assertEquals(-1.3, new ComplexNumber(1.1, -1.3).getImaginary(), delta);
	}

	@Test
	void testFromReal() {
		assertNotNull(fromReal(0));
		assertEquals(3.14, fromReal(3.14).getReal(), delta);
		assertEquals(0., fromReal(3.14).getImaginary(), delta);
	}

	@Test
	void testFromImaginary() {
		assertNotNull(fromImaginary(0));
		assertEquals(0., fromImaginary(3.14).getReal(), delta);
		assertEquals(3.14, fromImaginary(3.14).getImaginary(), delta);
	}

	@Test
	void testFromMagnitudeAndAngle() {
		assertTrue(new ComplexNumber(0, 0).equals(fromMagnitudeAndAngle(0, 0))); // 0
		assertTrue(new ComplexNumber(3.14, 0).equals(fromMagnitudeAndAngle(3.14, 0))); // 3.14
		assertTrue(new ComplexNumber(0, -2.57).equals(fromMagnitudeAndAngle(2.57, -1.570796))); // -2.57i
		assertTrue(new ComplexNumber(1, 1).equals(fromMagnitudeAndAngle(1.414214, 0.785398))); // 1 + i
		assertTrue(new ComplexNumber(2.54, -7.9).equals(fromMagnitudeAndAngle(8.298289, 5.0234692))); // 2.54 - 7.9i
	}

	@Test
	void testParse() {
		/* Test valid inputs */
		assertTrue(new ComplexNumber(0, 0).equals(ComplexNumber.parse("+0")));
		assertTrue(new ComplexNumber(0, 0).equals(ComplexNumber.parse("-0")));
		assertTrue(new ComplexNumber(0, 0).equals(ComplexNumber.parse("0")));
		assertTrue(new ComplexNumber(19, 0).equals(ComplexNumber.parse("19")));
		assertTrue(new ComplexNumber(3.14, 0).equals(ComplexNumber.parse("+3.14")));
		assertTrue(new ComplexNumber(-1.1, 0).equals(ComplexNumber.parse("-1.1")));
		assertTrue(new ComplexNumber(5.1297, 0).equals(ComplexNumber.parse("5.1297")));
		assertTrue(new ComplexNumber(0, 1).equals(ComplexNumber.parse("i")));
		assertTrue(new ComplexNumber(0, 1).equals(ComplexNumber.parse("+i")));
		assertTrue(new ComplexNumber(0, -1).equals(ComplexNumber.parse("-i")));
		assertTrue(new ComplexNumber(0, 3).equals(ComplexNumber.parse("3i")));
		assertTrue(new ComplexNumber(0, -2.57).equals(ComplexNumber.parse("-2.57i")));
		assertTrue(new ComplexNumber(0, 0).equals(ComplexNumber.parse("0i")));
		assertTrue(new ComplexNumber(0, 0).equals(ComplexNumber.parse("+0i")));
		assertTrue(new ComplexNumber(0, 0).equals(ComplexNumber.parse("-0i")));
		assertTrue(new ComplexNumber(0, 0).equals(ComplexNumber.parse("+0+0i")));
		assertTrue(new ComplexNumber(1, 1).equals(ComplexNumber.parse("1+i")));
		assertTrue(new ComplexNumber(2.54, -7.9).equals(ComplexNumber.parse("2.54-7.9i")));
		assertTrue(new ComplexNumber(2, 5).equals(ComplexNumber.parse("+2+5i")));
		assertTrue(new ComplexNumber(-3.25, -1).equals(ComplexNumber.parse("-3.25-i")));
		assertTrue(new ComplexNumber(0.1, 0).equals(ComplexNumber.parse("0.1-0i")));
		assertTrue(new ComplexNumber(0, 7).equals(ComplexNumber.parse("-0+7i")));
		assertTrue(new ComplexNumber(3, 8.67).equals(ComplexNumber.parse("   3+8.67i     ")));
		
		/* Test invalid inputs */
		assertThrows(NullPointerException.class, () -> ComplexNumber.parse(null));
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse(""));
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("a"));
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("350+-8i"));
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("1..3-i"));
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse(".0i"));
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("1.0-.5i"));
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("++11"));
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("i351"));
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("-i3.17"));
		assertThrows(NumberFormatException.class, () -> ComplexNumber.parse("3 + 7i"));
	}

	@Test
	void testAdd() {
		ComplexNumber c1 = new ComplexNumber(1.1, 3.157);
		ComplexNumber c2 = new ComplexNumber(-3, 2.54);
		assertTrue(new ComplexNumber(-1.9, 5.697).equals(c1.add(c2)));
		assertThrows(NullPointerException.class, () -> c1.add(null));
	}

	@Test
	void testSub() {
		ComplexNumber c1 = new ComplexNumber(1.1, 3.157);
		ComplexNumber c2 = new ComplexNumber(-3, 2.54);
		assertTrue(new ComplexNumber(4.1, 0.617).equals(c1.sub(c2)));
		assertThrows(NullPointerException.class, () -> c1.sub(null));
	}

	@Test
	void testMul() {
		ComplexNumber c1 = new ComplexNumber(1.1, 3.157);
		ComplexNumber c2 = new ComplexNumber(-3, 2.54);
		assertTrue(new ComplexNumber(-11.31878, -6.677).equals(c1.mul(c2)));
		assertThrows(NullPointerException.class, () -> c1.mul(null));
	}

	@Test
	void testDiv() {
		ComplexNumber c1 = new ComplexNumber(1.1, 3.157);
		ComplexNumber c2 = new ComplexNumber(-3, 2.54);
		assertTrue(new ComplexNumber(0.305391, -0.793769).equals(c1.div(c2)));
		assertThrows(NullPointerException.class, () -> c1.div(null));
	}

	@Test
	void testPower() {
		ComplexNumber c1 = new ComplexNumber(2.54, -7.9);
		ComplexNumber c2 = c1.power(0);
		ComplexNumber c3 = c1.power(2);
		ComplexNumber c4 = c1.power(5);
		
		assertTrue(new ComplexNumber(1, 0).equals(c2));
		assertTrue(new ComplexNumber(-55.9584, -40.132).equals(c3));
		assertTrue(new ComplexNumber(39345.15901, -605.773997).equals(c4));
		assertThrows(IllegalArgumentException.class, () -> c1.power(-1));
	}

	@Test
	void testRoot() {
		ComplexNumber c1 = new ComplexNumber(2.54, -7.9);
		
		ComplexNumber[] array = c1.root(1);
		assertEquals(c1, array[0]);
		
		array = c1.root(2);
		assertTrue(new ComplexNumber(-2.327906, 1.696804).equals(array[0]));
		assertTrue(new ComplexNumber(2.327906, -1.696804).equals(array[1]));
		
		assertThrows(IllegalArgumentException.class, () -> c1.root(0));
		assertThrows(IllegalArgumentException.class, () -> c1.root(-1));
	}

	@Test
	void testGetReal() {
		assertEquals(3.14, new ComplexNumber(3.14, -5.1).getReal(), delta);
	}

	@Test
	void testGetImaginary() {
		assertEquals(-5.1, new ComplexNumber(3.14, -5.1).getImaginary(), delta);
	}

	@Test
	void testGetMagnitude() {
		assertEquals(1.1, new ComplexNumber(-1.1, 0).getMagnitude(), delta);
		assertEquals(3, new ComplexNumber(0, 3).getMagnitude(), delta);
		assertEquals(8.298289, new ComplexNumber(2.54, -7.9).getMagnitude(), delta);
	}

	@Test
	void testGetAngle() {
		assertEquals(0, new ComplexNumber(3, 0).getAngle(), delta);
		assertEquals(1.570796, new ComplexNumber(0, 2.5).getAngle(), delta);
		assertEquals(5.023469, new ComplexNumber(2.54, -7.9).getAngle(), delta);
	}

	@Test
	void testEqualsObject() {
		ComplexNumber c1 = new ComplexNumber(1.1, 3.157);
		ComplexNumber c2 = new ComplexNumber(-3, 2.54);
		assertFalse(c1.equals(c2));
		
		c2 = new ComplexNumber(1.1, 3.157);
		assertTrue(c1.equals(c2));
		
		c2 = new ComplexNumber(1.1, 3.15708);
		assertFalse(c1.equals(c2));
		
		c2 = new ComplexNumber(1.1, 3.1570008);
		assertTrue(c1.equals(c2));
	}

}
