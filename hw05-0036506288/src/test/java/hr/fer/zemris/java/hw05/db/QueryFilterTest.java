package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class QueryFilterTest {

	@Test
	void testQueryFilter() {
		List<ConditionalExpression> condExprs = new ArrayList<>();

		condExprs.add(new ConditionalExpression(FieldValueGetters.JMBAG
				, "0000000005"
				, ComparisonOperators.LESS));

		assertNotNull(new QueryFilter(condExprs));
		assertThrows(NullPointerException.class, () -> new QueryFilter(null));
	}

	@Test
	void testAccepts() {
		List<ConditionalExpression> condExprs = new ArrayList<>();

		condExprs.add(new ConditionalExpression(FieldValueGetters.JMBAG
				, "0000000005"
				, ComparisonOperators.LESS));
		
		QueryFilter filter = new QueryFilter(condExprs);
		StudentRecord record = new StudentRecord("0000000004", "Frle", "Skoki", 5);
		StudentRecord record2 = new StudentRecord("0000000007", "Mogulić", "Marko", 1);
		
		assertEquals(true, filter.accepts(record));
		assertEquals(false, filter.accepts(record2));
	}

}
