package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

import hr.fer.zemris.java.hw05.db.ComparisonOperators;

/**
 * A lexical analyzer for queries described in problem 2 of the 5th homework.
 * <p>
 * Used by {@link QueryParser} class.
 * 
 * @author Ivan Skorupan
 */
public class QueryLexer {
	
	/**
	 * Input text (a query line) to be lexically analyzed as an array of characters.
	 */
	private char[] data;

	/**
	 * The current token.
	 */
	private QueryToken token;

	/**
	 * Index of first unprocessed character in the {@link #data} array.
	 */
	private int currentIndex;

	/**
	 * Constructs a new {@link QueryLexer} object. Takes input text
	 * to be tokenized as a {@link String} argument.
	 * 
	 * @param text - query line to be lexically analyzed
	 * @throws NullPointerException if <code>text</code> is <code>null</code>
	 */
	public QueryLexer(String text) {
		Objects.requireNonNull(text);
		this.data = text.toCharArray();
		this.currentIndex = 0;
	}
	
	/**
	 * Returns the last generated token.
	 * <p>
	 * Can be called multiple times. Does not run the generation
	 * of next token.
	 * 
	 * @return the last generated token
	 */
	public QueryToken getToken() {
		return this.token;
	}
	
	/**
	 * Generates, sets and returns the next token.
	 * 
	 * @return the next token in input text
	 * @throws QueryLexerException if there is an error while generating the next token
	 */
	public QueryToken nextToken() {
		// if we already read EOL, then this method call is invalid and we throw an exception
		checkEOL();

		generateNextToken();

		return getToken();
	}
	
	/**
	 * Helper method which contains actual logic required for generating the next token in the input text.
	 * 
	 * @throws QueryLexerException if an unsupported character (token) has been encountered
	 */
	private void generateNextToken() {
		skipEmptyContent();
		
		if(generateEndOfLine()) {
			return;
		}
		
		if(generateIdentifier()) {
			return;
		}
		
		if(generateLiteral()) {
			return;
		}
		
		if(generateOperator()) {
			return;
		}
		
		throw new QueryLexerException("An unsupported character has been encountered: " + data[currentIndex]);
	}
	
	private boolean generateOperator() {
		
		if(generateSingleOrDoubleCharacterOperator('<', '=')) {
			return true;
		}
		
		if(generateSingleOrDoubleCharacterOperator('>', '=')) {
			return true;
		}
		
		if(generateSingleOrDoubleCharacterOperator('!', '=')) {
			return true;
		}
		
		if(data[currentIndex] == '=') {
			token = new QueryToken(QueryTokenType.OPERATOR, "=");
			currentIndex++;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Generates a {@link QueryTokenType#OPERATOR operator} token. Since supported operators can be two characters long as well as one,
	 * we need to check the next character in case it's a double character operator.
	 * 
	 * @param firstCharacter - the first character of this operator to check
	 * @param secondCharacter - the second character of this operator to check
	 * @return <code>true</code> if an operator was generated, <code>false</code> otherwise
	 */
	private boolean generateSingleOrDoubleCharacterOperator(char firstCharacter, char secondCharacter) {
		int c = currentIndex;
		
		if(data[c] == firstCharacter) {
			if(c + 1 < data.length && data[c + 1] == secondCharacter) {
				token = new QueryToken(QueryTokenType.OPERATOR, String.valueOf(firstCharacter + secondCharacter));
				currentIndex += 2;
				return true;
			}
			
			token = new QueryToken(QueryTokenType.OPERATOR, String.valueOf(firstCharacter));
			currentIndex++;
			return true;
		}
		
		return false;
	}

	/**
	 * Generates a {@link QueryTokenType#LITERAL literal} token.
	 * 
	 * @return <code>true</code> if a string literal token was generated, <code>false</code> otherwise
	 */
	private boolean generateLiteral() {
		if(data[currentIndex] == '\"') {
			StringBuilder value = new StringBuilder();
			currentIndex++;

			while((currentIndex < data.length) && (data[currentIndex] != '\"')) {
				value.append(data[currentIndex++]);
			}

			currentIndex++;
			token = new QueryToken(QueryTokenType.LITERAL, value.toString());
			return true;
		}

		return false;
	}

	/**
	 * Generates a {@link QueryTokenType#IDENTIFIER identifier} token.
	 * 
	 * @return <code>true</code> if an identifier token was generated, <code>false</code> otherwise
	 */
	private boolean generateIdentifier() {
		if(Character.isLetter(data[currentIndex])) {
			StringBuilder value = parseIdentifier();
			
			if(generateOperatorIfLike(value.toString())) {
				return true;
			}

			token = new QueryToken(QueryTokenType.IDENTIFIER, value.toString());
			return true;
		}

		return false;
	}

	/**
	 * Generates an {@link QueryTokenType#OPERATOR operator} token, but only if it is a {@link ComparisonOperators#LIKE LIKE} operator.
	 * 
	 * @return <code>true</code> if a LIKE operator token was generated, <code>false</code> otherwise
	 */
	private boolean generateOperatorIfLike(String identifier) {
		if(identifier.equals("LIKE")) {
			token = new QueryToken(QueryTokenType.OPERATOR, identifier);
			return true;
		}
		
		return false;
	}

	/**
	 * Parses and returns the identifier name.
	 * 
	 * @return identifier name string
	 */
	private StringBuilder parseIdentifier() {
		StringBuilder value = new StringBuilder();

		while(Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_') {
			value.append(data[currentIndex++]);
			
			if(currentIndex >= data.length) {
				return value;
			}
		}

		return value;
	}

	/**
	 * Checks whether the current token is EOL and then creates the
	 * {@link QUeryTokenType#EOL EOL} token if so.
	 * 
	 * @return <code>true</code> if end of line (EOL) token was generated, <code>false</code> otherwise
	 */
	private boolean generateEndOfLine() {
		// if we are at the end of input text, we generate the EOL token
		if(currentIndex >= data.length) {
			token = new QueryToken(QueryTokenType.EOL, null);
			return true;
		}

		return false;
	}

	private void checkEOL() {
		if(token != null && token.getType() == QueryTokenType.EOL) {
			throw new QueryLexerException("The end of line was already reached!");
		}
	}
	
	/**
	 * Helper method that determines if the current character is empty content.
	 * 
	 * @return <code>true</code> if current character is a space, tab or a newline, <code>false</code> otherwise
	 */
	private boolean isEmptyContent() {
		char c = data[currentIndex];
		if(c == ' ' || c == '\t') {
			return true;
		}

		return false;
	}

	/**
	 * Helper method used for skipping all blank characters in input text.
	 */
	private void skipEmptyContent() {
		while(currentIndex < data.length) {
			if(isEmptyContent()) {
				currentIndex++;
				continue;
			}
			break;
		}
		return;
	}
	
}
