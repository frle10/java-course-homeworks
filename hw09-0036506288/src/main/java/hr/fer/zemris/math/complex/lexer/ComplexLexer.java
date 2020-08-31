package hr.fer.zemris.math.complex.lexer;

import java.util.Objects;

/**
 * A simple lexical analyzer for complex numbers of form described in
 * problem 2 of the 9th homework.
 * <p>
 * Used by {@link ComplexParser} class.
 * 
 * @author Ivan Skorupan
 */
public class ComplexLexer {

	/**
	 * Input text to be lexically analyzed as an array of characters.
	 */
	private char[] data;

	/**
	 * The current token.
	 */
	private ComplexToken token;

	/**
	 * Index of first unprocessed character in the {@link #data} array.
	 */
	private int currentIndex;

	/**
	 * Constructs a new {@link ComplexLexer} object. Takes input text
	 * to be tokenized as a {@link String} argument.
	 * 
	 * @param input - complex number string to be lexically analyzed
	 * @throws NullPointerException if <code>input</code> is <code>null</code>
	 */
	public ComplexLexer(String input) {
		Objects.requireNonNull(input);
		this.data = input.toCharArray();
	}

	/**
	 * Returns the last generated token.
	 * <p>
	 * Can be called multiple times. Does not run the generation
	 * of next token.
	 * 
	 * @return the last generated token
	 */
	public ComplexToken getToken() {
		return token;
	}

	/**
	 * Generates, sets and returns the next token.
	 * 
	 * @return the next token in input text
	 * @throws ComplexLexerException if there is an error while generating the next token
	 */
	public ComplexToken nextToken() {
		// if we already read EOL, then this method call is invalid and we throw an exception
		checkEOL();
		generateNextToken();
		return token;
	}

	/**
	 * Helper method that contains actual logic for token generation.
	 */
	private void generateNextToken() {
		if(token != null && token.getType() != ComplexTokenType.IMAGINARY_UNIT) {
			skipEmptyContent();
		}
		
		if(generateEndOfLine()) {
			return;
		}

		if(generateNumber()) {
			return;
		}

		if(generateImaginaryUnit()) {
			return;
		}

		if(generateSymbol()) {
			return;
		}

		// if we come here, there is an error because an unknown situation happened
		throw new ComplexLexerException("An unknown character sequence was detected in the complex number string!");
	}

	/**
	 * Checks for and generates an imaginary unit type token, a.k.a
	 * {@link ComplexTokenType#IMAGINARY_UNIT IMAGINARY_UNIT}.
	 * 
	 * @return <code>true</code> if {@link ComplexTokenType#IMAGINARY_UNIT IMAGINARY_UNIT} token was generated, <code>false</code> otherwise
	 */
	private boolean generateImaginaryUnit() {
		if(data[currentIndex] >= data.length && data[currentIndex] == 'i') {
			token = new ComplexToken(ComplexTokenType.IMAGINARY_UNIT, null);
			currentIndex++;
			return true;
		}
		
		return false;
	}

	/**
	 * Checks for and generates a symbol type token, a.k.a
	 * {@link ComplexTokenType#SYMBOL SYMBOL}.
	 * 
	 * @return <code>true</code> if {@link ComplexTokenType#SYMBOL SYMBOL} token was generated, <code>false</code> otherwise
	 */
	private boolean generateSymbol() {
		if(data[currentIndex] >= data.length) {
			token = new ComplexToken(ComplexTokenType.SYMBOL, String.valueOf(data[currentIndex++]));
			return true;
		}
		
		return false;
	}

	/**
	 * Check whether the EOF token was already read and then throws an exception if so because
	 * the current call of {@link #nextToken()} is invalid.
	 * 
	 * @throws ComplexLexerException if EOL was already generated
	 */
	private void checkEOL() {
		if(token != null && token.getType() == ComplexTokenType.EOL) {
			throw new ComplexLexerException("The end of line was already reached!");
		}
	}

	/**
	 * Checks whether the current token is EOF and then creates the
	 * {@link ComplexTokenType#EOF EOF} token if so.
	 * 
	 * @return <code>true</code> if {@link ComplexTokenType#EOL EOL} was generated, <code>false</code> otherwise
	 */
	private boolean generateEndOfLine() {
		// if we are at the end of input text, we generate the EOF token
		if(currentIndex >= data.length) {
			token = new ComplexToken(ComplexTokenType.EOL, null);
			return true;
		}

		return false;
	}

	/**
	 * Checks whether the current token is an integer or a double and then creates the
	 * {@link ComplexTokenType#INTEGER INTEGER} or {@link ComplexTokenType#DOUBLE DOUBLE}
	 * token if so.
	 * 
	 * @return <code>true</code> if {@link ComplexTokenType#NUMBER NUMBER} token was generated, <code>false</code> otherwise
	 */
	private boolean generateNumber() {
		StringBuilder value = new StringBuilder();

		// check if there is a number with minus sign directly in front of it
		if((currentIndex < data.length - 1) && (data[currentIndex] == '-') && (Character.isDigit(data[currentIndex + 1]))) {
			value.append(data[currentIndex++]);
		}
		
		// parse a number if there is one (explicit sign taken care of in the last if statement)
		if(Character.isDigit(data[currentIndex])) {
			value.append(parseNumber().toString());
			token = new ComplexToken(ComplexTokenType.NUMBER, Double.parseDouble(value.toString()));
			return true;
		}

		return false;
	}

	/**
	 * Parses a detected number and returns it in a textual form.
	 * 
	 * @return numeric value of this number as a {@link StringBuilder} object
	 */
	private StringBuilder parseNumber() {
		StringBuilder value = new StringBuilder();
		
		boolean floatingPointEncountered = false;
		while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
			if(currentIndex < data.length - 1 && data[currentIndex] == '0' && Character.isDigit(data[currentIndex + 1])) {
				throw new ComplexLexerException("Leading zeros in a number are not allowed!");
			}
			
			value.append(data[currentIndex++]);
			if((currentIndex < data.length - 1) && (data[currentIndex] == '.') && (Character.isDigit(data[currentIndex + 1])) && !floatingPointEncountered) {
				value.append(data[currentIndex++]);
				floatingPointEncountered = true;
			}
		}

		return value;
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
	}

}
