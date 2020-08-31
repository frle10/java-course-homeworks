package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vector2DTest {

	private static double DELTA = 1e-6;
	
	@Test
	void testVector2D() {
		assertNotNull(new Vector2D(1, 2));

		Vector2D vector = new Vector2D(3, 4);

		assertEquals(3, vector.getX());
		assertEquals(4, vector.getY());
	}

	@Test
	void testGetX() {
		assertEquals(3.14, new Vector2D(3.14, 2.81).getX());
	}

	@Test
	void testGetY() {
		assertEquals(2.81, new Vector2D(3.14, 2.81).getY());
	}

	@Test
	void testTranslate() {
		Vector2D vector = new Vector2D(1., 2.);

		vector.translate(new Vector2D(3., -5.));

		assertEquals(4., vector.getX());
		assertEquals(-3., vector.getY());
	}

	@Test
	void testTranslated() {
		Vector2D vector = new Vector2D(1., 2.);

		Vector2D translatedVector = vector.translated(new Vector2D(3., -5.));

		assertEquals(1., vector.getX());
		assertEquals(2., vector.getY());
		
		assertEquals(4., translatedVector.getX());
		assertEquals(-3., translatedVector.getY());
	}

	@Test
	void testRotate() {
		Vector2D vector = new Vector2D(1., 2.);

		vector.rotate(1);

		assertEquals(-1.142640, vector.getX(), DELTA);
		assertEquals(1.922076, vector.getY(), DELTA);
	}

	@Test
	void testRotated() {
		Vector2D vector = new Vector2D(1., 2.);

		Vector2D rotatedVector = vector.rotated(1);

		assertEquals(1., vector.getX());
		assertEquals(2., vector.getY());
		
		assertEquals(-1.142640, rotatedVector.getX(), DELTA);
		assertEquals(1.922076, rotatedVector.getY(), DELTA);
	}

	@Test
	void testScale() {
		Vector2D vector = new Vector2D(1., 2.);

		vector.scale(4);
		
		assertEquals(4., vector.getX());
		assertEquals(8., vector.getY());
	}

	@Test
	void testScaled() {
		Vector2D vector = new Vector2D(1., 2.);

		Vector2D scaledVector = vector.scaled(4);

		assertEquals(1., vector.getX());
		assertEquals(2., vector.getY());
		
		assertEquals(4., scaledVector.getX());
		assertEquals(8., scaledVector.getY());
	}

	@Test
	void testCopy() {
		Vector2D vector = new Vector2D(10., 15.);
		
		Vector2D copiedVector = vector.copy();
		
		assertEquals(10., vector.getX());
		assertEquals(15., vector.getY());
		
		assertEquals(10., copiedVector.getX());
		assertEquals(15., copiedVector.getY());
	}

}
