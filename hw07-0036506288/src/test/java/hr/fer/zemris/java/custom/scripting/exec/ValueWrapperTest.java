package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class ValueWrapperTest {
	
	private static final double DELTA = 10e-6;
	
	@Test
	void testValueWrapper() {
		assertNotNull(new ValueWrapper(null));
		assertNotNull(new ValueWrapper("value"));
		assertNotNull(new ValueWrapper(new ArrayList<Integer>()));
	}

	@Test
	void testGetValue() {
		assertEquals(3.14, new ValueWrapper(3.14).getValue());
		assertEquals(57, new ValueWrapper(57).getValue());
		assertEquals("wow!", new ValueWrapper("wow!").getValue());
		assertEquals(null, new ValueWrapper(null).getValue());
	}

	@Test
	void testSetValue() {
		ValueWrapper vw = new ValueWrapper(null);
		vw.setValue(3.14);
		assertEquals(3.14, vw.getValue());
		
		vw.setValue(null);
		assertEquals(null, vw.getValue());
	}

	@Test
	void testAdd() {
		ValueWrapper vw = new ValueWrapper(3.14);
		vw.add(3.14);
		
		assertEquals(6.28, (Double) vw.getValue(), DELTA);
		
		vw.setValue(5);
		vw.add("10.77");
		assertEquals(15.77, (Double) vw.getValue(), DELTA);
		
		vw.setValue(null);
		vw.add(5);
		assertEquals(5, vw.getValue());
		
		vw.setValue("100.01");
		vw.add("-3");
		assertEquals(97.01, (Double) vw.getValue(), DELTA);
		
		vw.setValue("Boeing747");
		assertThrows(RuntimeException.class, () -> vw.add(5));
		
		vw.setValue(null);
		vw.add(null);
		assertEquals(0, vw.getValue());
	}

	@Test
	void testSubtract() {
		ValueWrapper vw = new ValueWrapper(3.14);
		vw.subtract(3.14);
		
		assertEquals(0, (Double) vw.getValue(), DELTA);
		
		vw.setValue(5);
		vw.subtract("10.77");
		assertEquals(-5.77, (Double) vw.getValue(), DELTA);
		
		vw.setValue(null);
		vw.subtract(5);
		assertEquals(-5, vw.getValue());
		
		vw.setValue("100.01");
		vw.subtract("-3");
		assertEquals(103.01, (Double) vw.getValue(), DELTA);
		
		vw.setValue("Boeing747");
		assertThrows(RuntimeException.class, () -> vw.subtract(5));
		
		vw.setValue(null);
		vw.subtract(null);
		assertEquals(0, vw.getValue());
	}

	@Test
	void testMultiply() {
		ValueWrapper vw = new ValueWrapper(3.14);
		vw.multiply(3.14);
		
		assertEquals(9.8596, (Double) vw.getValue(), DELTA);
		
		vw.setValue(5);
		vw.multiply("10.77");
		assertEquals(53.85, (Double) vw.getValue(), DELTA);
		
		vw.setValue(null);
		vw.multiply(5);
		assertEquals(0, vw.getValue());
		
		vw.setValue("100.01");
		vw.multiply("-3");
		assertEquals(-300.03, (Double) vw.getValue(), DELTA);
		
		vw.setValue("Boeing747");
		assertThrows(RuntimeException.class, () -> vw.multiply(5));
		
		vw.setValue(null);
		vw.multiply(null);
		assertEquals(0, vw.getValue());
	}

	@Test
	void testDivide() {
		ValueWrapper vw = new ValueWrapper(3.14);
		vw.divide(3.14);
		
		assertEquals(1.0, (Double) vw.getValue(), DELTA);
		
		vw.setValue(5);
		vw.divide("10.77");
		assertEquals(0.4642526, (Double) vw.getValue(), DELTA);
		
		vw.setValue(null);
		vw.divide(5);
		assertEquals(0, vw.getValue());
		
		vw.setValue("100.01");
		vw.divide("-3");
		assertEquals(-33.33666667, (Double) vw.getValue(), DELTA);
		
		vw.setValue(0.0);
		vw.divide(0.0);
		assertEquals(Double.NaN, (Double) vw.getValue(), DELTA);
		
		vw.setValue(0.1);
		vw.divide(0.0);
		assertEquals(Double.POSITIVE_INFINITY, (Double) vw.getValue(), DELTA);
		
		vw.setValue(-0.1);
		vw.divide(0.0);
		assertEquals(Double.NEGATIVE_INFINITY, (Double) vw.getValue(), DELTA);
		
		vw.setValue("Boeing747");
		assertThrows(RuntimeException.class, () -> vw.divide(5));
		
		vw.setValue(null);
		assertThrows(RuntimeException.class, () -> vw.divide(null));
	}

	@Test
	void testNumCompare() {
		ValueWrapper vw = new ValueWrapper(3.14);
		assertEquals(true, vw.numCompare(5) < 0);
		assertEquals(true, vw.numCompare(1.111) > 0);
		assertEquals(true, vw.numCompare("3.14") == 0);
		assertEquals(false, vw.numCompare(null) < 0);
		
		ValueWrapper vw2 = new ValueWrapper("-7.11");
		assertEquals(true, vw2.numCompare(5) < 0);
		assertEquals(true, vw2.numCompare(1.111) < 0);
		assertEquals(true, vw2.numCompare(-7.11) == 0);
		assertEquals(false, vw2.numCompare(null) > 0);
		
		assertThrows(RuntimeException.class, () -> vw.numCompare("nekiBoeing"));
		assertThrows(RuntimeException.class, () -> vw.numCompare(new Object()));
		
		assertThrows(RuntimeException.class, () -> vw2.numCompare("nekiBoeing"));
		assertThrows(RuntimeException.class, () -> vw2.numCompare(new Object()));
	}

}
