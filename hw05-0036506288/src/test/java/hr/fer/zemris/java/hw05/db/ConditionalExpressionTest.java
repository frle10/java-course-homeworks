package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConditionalExpressionTest {

	@Test
	void testConditionalExpressionCodeSnippet() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bos*", ComparisonOperators.LIKE);

		StudentRecord record = new StudentRecord("12345678", "Bosnjak", "Ivan", 4);

		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record), // returns lastName from given record
				expr.getStringLiteral() // returns "Bos*"
				);

		assertEquals(true, recordSatisfies);
	}

	@Test
	void testGetFieldGetter() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bos*", ComparisonOperators.LIKE);

		IFieldValueGetter fieldGetter = expr.getFieldGetter();

		assertEquals(FieldValueGetters.LAST_NAME, fieldGetter);
	}

	@Test
	void testGetStringLiteral() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bos*", ComparisonOperators.LIKE);

		String stringLiteral = expr.getStringLiteral();

		assertEquals("Bos*", stringLiteral);
	}

	@Test
	void testGetComparisonOperator() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bos*", ComparisonOperators.LIKE);

		IComparisonOperator compOper = expr.getComparisonOperator();

		assertEquals(ComparisonOperators.LIKE, compOper);
	}
	
	@Test
	void testConstructor() {
		assertNotNull(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000045", ComparisonOperators.EQUALS));
		assertThrows(NullPointerException.class, () -> new ConditionalExpression(null, "Bok", ComparisonOperators.LESS_OR_EQUALS));
	}

}
