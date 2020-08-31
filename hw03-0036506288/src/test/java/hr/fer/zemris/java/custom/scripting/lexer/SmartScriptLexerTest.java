package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SmartScriptLexerTest {

	@Test
	public void testNotNull() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
	}
	
	@Test
	public void testNullInput() {
		// must throw!
		assertThrows(NullPointerException.class, () -> new SmartScriptLexer(null));
	}
	
	@Test
	public void testEmpty() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
	}
	
	@Test
	public void testGetReturnsLastNext() {
		// Calling getToken once or several times after calling nextToken must return each time what nextToken returned...
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		SmartScriptToken token = lexer.nextToken();
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
	}
	
	@Test
	public void testReadAfterEOF() {
		SmartScriptLexer lexer = new SmartScriptLexer("");

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	public void testJustEmptyChars() {
		// input consists of only spaces, tabs, newlines, ...
		SmartScriptLexer lexer = new SmartScriptLexer("   \r\n\t    ");
		
		SmartScriptToken expected = new SmartScriptToken(SmartScriptTokenType.TEXT, "   \r\n\t    ");
		assertEquals(lexer.nextToken().getType(), SmartScriptTokenType.TEXT);
		assertEquals(lexer.getToken().getValue(), expected.getValue());
	}
	
	@Test
	public void testDollarSignAtBeginning() {
		SmartScriptLexer lexer = new SmartScriptLexer("${ivan  \n skorjsyn {");
		
		SmartScriptToken expected = new SmartScriptToken(SmartScriptTokenType.TEXT, "${ivan  \n skorjsyn {");
		assertEquals(lexer.nextToken().getType(), SmartScriptTokenType.TEXT);
		assertEquals(lexer.getToken().getValue(), expected.getValue());
	}
	
	@Test
	public void testEscapedCurlyBraceAndDollarSign() {
		SmartScriptLexer lexer = new SmartScriptLexer("hmm \r možda \\{$? string\n ");
		
		SmartScriptToken expected = new SmartScriptToken(SmartScriptTokenType.TEXT, "hmm \r možda {$? string\n ");
		assertEquals(lexer.nextToken().getType(), SmartScriptTokenType.TEXT);
		assertEquals(lexer.getToken().getValue(), expected.getValue());
	}
	
	@Test
	public void testManyEscapeSeqences() {
		SmartScriptLexer lexer = new SmartScriptLexer("A kaj \n \r\\{\\\\ \\{$ mesaaa");
		
		SmartScriptToken expected = new SmartScriptToken(SmartScriptTokenType.TEXT, "A kaj \n \r{\\ {$ mesaaa");
		assertEquals(SmartScriptTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(expected.getValue(), lexer.getToken().getValue());
	}
	
	@Test
	public void testStopReadingAtTagOpening() {
		SmartScriptLexer lexer = new SmartScriptLexer("Uskoro imamo {$otvoren tag...\n");
		
		SmartScriptToken expected = new SmartScriptToken(SmartScriptTokenType.TEXT, "Uskoro imamo ");
		assertEquals(expected.getType(), lexer.nextToken().getType());
		assertEquals(expected.getValue(), lexer.getToken().getValue());
		
		expected = new SmartScriptToken(SmartScriptTokenType.TAG_OPEN, null);
		assertEquals(expected.getType(), lexer.nextToken().getType());
		assertEquals(expected.getValue(), lexer.getToken().getValue());
	}
	
	@Test
	public void testOnlyCurlyBrace() {
		SmartScriptLexer lexer = new SmartScriptLexer("{");
		
		SmartScriptToken expected = new SmartScriptToken(SmartScriptTokenType.TEXT, "{");
		assertEquals(expected.getType(), lexer.nextToken().getType());
		assertEquals(expected.getValue(), lexer.getToken().getValue());
	}
	
	@Test
	public void testTagModeTokenization() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$ FOR i -1 10 1 $}");
		
		SmartScriptToken expected = new SmartScriptToken(SmartScriptTokenType.TAG_OPEN, null);
		assertEquals(expected.getType(), lexer.nextToken().getType());
		assertEquals(expected.getValue(), lexer.getToken().getValue());
		
		lexer.setState(SmartScriptLexerState.TAG);
		
		expected = new SmartScriptToken(SmartScriptTokenType.IDENT, "FOR");
		assertEquals(expected.getType(), lexer.nextToken().getType());
		assertEquals(expected.getValue(), lexer.getToken().getValue());
	}
	
	@Test
	public void testTokenizationInBothModes() {
		String document = "This is sample text.\r\n" + 
				"{$ FOR i 1 10 1 $}\r\n" + 
				" This is {$= i $}-th time this message is generated.\r\n" + 
				"{$END$}\r\n" + 
				"{$FOR i 0 10 2 $}\r\n" + 
				" sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\r\n" + 
				"{$END$}";
		
		SmartScriptToken[] correctOutput = {
				new SmartScriptToken(SmartScriptTokenType.TEXT, "This is sample text.\r\n"), // 0
				new SmartScriptToken(SmartScriptTokenType.TAG_OPEN, null), // 1
				new SmartScriptToken(SmartScriptTokenType.IDENT, "FOR"), // 2
				new SmartScriptToken(SmartScriptTokenType.IDENT, "i"), // 3
				new SmartScriptToken(SmartScriptTokenType.INTEGER, 1), // 4
				new SmartScriptToken(SmartScriptTokenType.INTEGER, 10), // 5
				new SmartScriptToken(SmartScriptTokenType.INTEGER, 1), // 6
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSE, null), // 7
				new SmartScriptToken(SmartScriptTokenType.TEXT, "\r\n This is "), // 8
				new SmartScriptToken(SmartScriptTokenType.TAG_OPEN, null), // 9
				new SmartScriptToken(SmartScriptTokenType.SYMBOL, "="), // 10
				new SmartScriptToken(SmartScriptTokenType.IDENT, "i"), // 11
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSE, null), // 12
				new SmartScriptToken(SmartScriptTokenType.TEXT, "-th time this message is generated.\r\n"), // 13
				new SmartScriptToken(SmartScriptTokenType.TAG_OPEN, null), // 14
				new SmartScriptToken(SmartScriptTokenType.IDENT, "END"), // 15
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSE, null), // 16
				new SmartScriptToken(SmartScriptTokenType.TEXT, "\r\n"), // 17
				new SmartScriptToken(SmartScriptTokenType.TAG_OPEN, null), // 18
				new SmartScriptToken(SmartScriptTokenType.IDENT, "FOR"), // 19
				new SmartScriptToken(SmartScriptTokenType.IDENT, "i"), // 20
				new SmartScriptToken(SmartScriptTokenType.INTEGER, 0), // 21
				new SmartScriptToken(SmartScriptTokenType.INTEGER, 10), // 22
				new SmartScriptToken(SmartScriptTokenType.INTEGER, 2), // 23
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSE, null), // 24
				new SmartScriptToken(SmartScriptTokenType.TEXT, "\r\n sin("), // 25
				new SmartScriptToken(SmartScriptTokenType.TAG_OPEN, null), // 26
				new SmartScriptToken(SmartScriptTokenType.SYMBOL, "="), // 27
				new SmartScriptToken(SmartScriptTokenType.IDENT, "i"), // 28
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSE, null), // 29
				new SmartScriptToken(SmartScriptTokenType.TEXT, "^2) = "), // 30
				new SmartScriptToken(SmartScriptTokenType.TAG_OPEN, null), // 31
				new SmartScriptToken(SmartScriptTokenType.SYMBOL, "="), // 32
				new SmartScriptToken(SmartScriptTokenType.IDENT, "i"), // 33
				new SmartScriptToken(SmartScriptTokenType.IDENT, "i"), // 34
				new SmartScriptToken(SmartScriptTokenType.SYMBOL, "*"), // 35
				new SmartScriptToken(SmartScriptTokenType.FUNC, "sin"), // 36
				new SmartScriptToken(SmartScriptTokenType.TEXT, "0.000"), // 37
				new SmartScriptToken(SmartScriptTokenType.FUNC, "decfmt"), // 38
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSE, null), // 39
				new SmartScriptToken(SmartScriptTokenType.TEXT, "\r\n"), // 40
				new SmartScriptToken(SmartScriptTokenType.TAG_OPEN, null), // 41
				new SmartScriptToken(SmartScriptTokenType.IDENT, "END"), // 42
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSE, null), // 43
				new SmartScriptToken(SmartScriptTokenType.EOF, null) // 44
		};
		
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		int counter = 0;
		
		for(SmartScriptToken expected : correctOutput) {
			SmartScriptToken current = lexer.nextToken();
			
			if(current.getType() == SmartScriptTokenType.TAG_OPEN) {
				lexer.setState(SmartScriptLexerState.TAG);
			} else if(current.getType() == SmartScriptTokenType.TAG_CLOSE) {
				lexer.setState(SmartScriptLexerState.BODY);
			}
			
			String msg = "Checking token " + counter + ":";
			
			assertEquals(expected.getType(), current.getType(), msg);
			assertEquals(expected.getValue(), current.getValue(), msg);
			
			counter++;
		}
	}
	
}
