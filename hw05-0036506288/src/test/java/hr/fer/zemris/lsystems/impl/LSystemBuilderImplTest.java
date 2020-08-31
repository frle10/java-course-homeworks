package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;

class LSystemBuilderImplTest {

	@Test
	void testGenerateExample() {
		LSystemBuilder builder = new LSystemBuilderImpl()
				.setAxiom("F")
				.registerProduction('F', "F+F--F+F");
		
		LSystem system = builder.build();
		
		String levelZeroExpected = "F";
		String levelOneExpected = "F+F--F+F";
		String levelTwoExpected = "F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F";
		
		assertEquals(levelZeroExpected, system.generate(0));
		assertEquals(levelOneExpected, system.generate(1));
		assertEquals(levelTwoExpected, system.generate(2));
	}

}
