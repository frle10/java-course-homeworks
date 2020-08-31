package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class QueryParserTest {

	@Test
	void testQueryParser() {
		assertNotNull(new QueryParser("lastName LIKE \"something*\""));

		assertThrows(QueryParserException.class, () -> new QueryParser(null));
		assertThrows(QueryParserException.class, () -> new QueryParser("query mora bit bez naredbe"));
	}

	@Test
	void testIsDirectQuery() {
		QueryParser parser = new QueryParser("lastName LIKE \"something*\"");

		assertEquals(false, parser.isDirectQuery());

		parser = new QueryParser("jmbag = \"0000000003\"");

		assertEquals(true, parser.isDirectQuery());
	}

	@Test
	void testGetQueriedJMBAG() {
		QueryParser parser = new QueryParser("lastName LIKE \"something*\"");

		assertThrows(IllegalStateException.class, () -> parser.getQueriedJMBAG());

		QueryParser parser2 = new QueryParser("jmbag = \"0000000003\"");

		assertEquals("0000000003", parser2.getQueriedJMBAG());
	}

	@Test
	void testGetQuery() {
		QueryParser parser = new QueryParser("lastName LIKE \"something*\"");
		
		ConditionalExpression condExpr = new ConditionalExpression(FieldValueGetters.LAST_NAME
				, "something*"
				, ComparisonOperators.LIKE);
		
		List<ConditionalExpression> list = new ArrayList<>();
		list.add(condExpr);
		
		assertEquals(list, parser.getQuery());
		
		parser = new QueryParser("lastName LIKE \"something*\" AND jmbag = \"0000000003\"");
		
		ConditionalExpression condExpr2 = new ConditionalExpression(FieldValueGetters.JMBAG
				, "0000000003"
				, ComparisonOperators.EQUALS);
		
		list.add(condExpr2);
		
		assertEquals(list, parser.getQuery());
	}

}
