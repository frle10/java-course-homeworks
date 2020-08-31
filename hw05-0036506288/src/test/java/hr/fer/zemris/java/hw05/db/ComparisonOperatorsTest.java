package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComparisonOperatorsTest {

	@Test
	void testLess() {
		IComparisonOperator oper = ComparisonOperators.LESS;
		assertEquals(true, oper.satisfied("Ana", "Jasna"));
		assertEquals(true, oper.satisfied("Ana", "ana"));
		assertEquals(false, oper.satisfied("Ivan", "Ivan"));
		assertEquals(false, oper.satisfied("ivan", "IVAN"));
	}
	
	@Test
	void testLessOrEquals() {
		IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
		assertEquals(true, oper.satisfied("Ana", "Jasna"));
		assertEquals(true, oper.satisfied("Ana", "ana"));
		assertEquals(true, oper.satisfied("Ivan", "Ivan"));
		assertEquals(false, oper.satisfied("ivan", "IVAN"));
	}
	
	@Test
	void testGreater() {
		IComparisonOperator oper = ComparisonOperators.GREATER;
		assertEquals(false, oper.satisfied("Ana", "Jasna"));
		assertEquals(false, oper.satisfied("Ana", "ana"));
		assertEquals(false, oper.satisfied("Ivan", "Ivan"));
		assertEquals(true, oper.satisfied("ivan", "IVAN"));
	}
	
	@Test
	void testGreaterOrEquals() {
		IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
		assertEquals(false, oper.satisfied("Ana", "Jasna"));
		assertEquals(false, oper.satisfied("Ana", "ana"));
		assertEquals(true, oper.satisfied("Ivan", "Ivan"));
		assertEquals(true, oper.satisfied("ivan", "IVAN"));
	}
	
	@Test
	void testEquals() {
		IComparisonOperator oper = ComparisonOperators.EQUALS;
		assertEquals(false, oper.satisfied("Ana", "Jasna"));
		assertEquals(false, oper.satisfied("Ana", "ana"));
		assertEquals(true, oper.satisfied("Ivan", "Ivan"));
		assertEquals(false, oper.satisfied("IVAN", "ivan"));
	}
	
	@Test
	void testNotEquals() {
		IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;
		assertEquals(true, oper.satisfied("Ana", "Jasna"));
		assertEquals(true, oper.satisfied("Ana", "ana"));
		assertEquals(false, oper.satisfied("Ivan", "Ivan"));
		assertEquals(true, oper.satisfied("IVAN", "ivan"));
	}
	
	@Test
	void testLike() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		assertEquals(false, oper.satisfied("Ana", "Jasna"));
		assertEquals(false, oper.satisfied("Zagreb", "Aba*"));
		assertEquals(false, oper.satisfied("AAA", "AA*AA"));
		assertEquals(true, oper.satisfied("AAAA", "AA*AA"));
		assertEquals(true, oper.satisfied("Ja sam Ivan", "*n"));
		assertEquals(true, oper.satisfied("neki stinfjfdnv", "*"));
		assertEquals(false, oper.satisfied("traavia", "tra*vian"));
		assertEquals(true, oper.satisfied("opetNekiTest()",	"*ekiTest()"));
		assertEquals(true, oper.satisfied("naKrajuWildcard", "naKrajuW*"));
	}

}
