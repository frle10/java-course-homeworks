package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * A lexical analyzer for structured document format described in problem 3 of
 * the 3rd homework.
 * <p>
 * Used by {@link SmartScriptParser} class.
 * 
 * @author Ivan Skorupan
 */
public class SmartScriptLexer {

	/**
	 * Input text to be lexically analyzed as an array of characters.
	 */
	private char[] data;

	/**
	 * The current token.
	 */
	private SmartScriptToken token;

	/**
	 * Index of first unprocessed character in the {@link #data} array.
	 */
	private int currentIndex;

	/**
	 * Current lexer state.
	 */
	private SmartScriptLexerState state;

	/**
	 * Constructs a new {@link SmartScriptLexer} object. Takes input text
	 * to be tokenized as a {@link String} argument.
	 * 
	 * @param text - text to be lexically analyzed
	 * @throws NullPointerException if <code>text</code> is <code>null</code>
	 */
	public SmartScriptLexer(String text) {
		Objects.requireNonNull(text);
		this.data = text.toCharArray();
		this.currentIndex = 0;
		this.state = SmartScriptLexerState.BODY;
	}

	/**
	 * Sets the lexer in a desired state from {@link LexerState} enumeration.
	 * 
	 * @param state - a state to which to set the lexer
	 * @throws NullPointerException if <code>state</code> is <code>null</code>
	 */
	public void setState(SmartScriptLexerState state) {
		this.state = Objects.requireNonNull(state);
	}

	/**
	 * Returns the last generated token.
	 * <p>
	 * Can be called multiple times. Does not run the generation
	 * of next token.
	 * 
	 * @return the last generated token
	 */
	public SmartScriptToken getToken() {
		return this.token;
	}

	/**
	 * Generates, sets and returns the next token.
	 * 
	 * @return the next token in input text
	 * @throws SmartScriptLexerException if there is an error while generating the next token
	 */
	public SmartScriptToken nextToken() {
		// if we already read EOF, then this method call is invalid and we throw an exception
		checkEOF();

		if(this.state == SmartScriptLexerState.BODY) {
			generateNextTokenBody();
		} else if(this.state == SmartScriptLexerState.TAG) {
			generateNextTokenTag();
		}

		return getToken();
	}

	/**
	 * Helper method which contains actual logic required for generating the next token in the input text.
	 * <p>
	 * This method is called when the lexer is in {@link SmartScriptLexerState#BODY body} mode.
	 * 
	 * @throws SmartScriptLexerException if there is an error while generating the next token
	 */
	private void generateNextTokenBody() {
		if(generateEndOfFile()) {
			return;
		}

		if(generateOpeningTag()) {
			return;
		}

		if(generateBodyText()) {
			return;
		}

		// if we come here, there is an error because an unknown situation happened
		throw new SmartScriptLexerException("An unknown character sequence was detected in the document body!");
	}

	/**
	 * Helper method which contains actual logic required for generating the next token in the input text.
	 * <p>
	 * This method is called when the lexer is in {@link SmartScriptLexerState#TAG tag} mode.
	 * 
	 * @throws SmartScriptLexerException if there is an error while generating the next token
	 */
	private void generateNextTokenTag() {
		skipEmptyContent();

		if(generateEndOfFile()) {
			return;
		}

		if(generateClosingTag()) {
			return;
		}

		if(generateOpeningTag()) {
			return;
		}

		if(generateIdentifier()) {
			return;
		}

		if(generateFunction()) {
			return;
		}

		if(generateNumber()) {
			return;
		}

		if(generateTextInsideTag()) {
			return;
		}

		// if nothing above, we have some symbol
		token = new SmartScriptToken(SmartScriptTokenType.SYMBOL, String.valueOf(data[currentIndex++]));
		return;
	}

	/**
	 * Check whether the EOF token was already read and then throws an exception if so because
	 * the current call of {@link #nextToken()} is invalid.
	 * 
	 * @throws SmartScriptLexerException if EOF was already read
	 */
	private void checkEOF() {
		if(token != null && token.getType() == SmartScriptTokenType.EOF) {
			throw new SmartScriptLexerException("The end of file was already reached!");
		}
	}

	/**
	 * Checks whether the current token is a function name and then creates the
	 * {@link SmartScriptTokenType#FUNC FUNC} token if so.
	 * 
	 * @return <code>true</code> if function token was generated, <code>false</code> otherwise
	 */
	private boolean generateFunction() {
		if((currentIndex < data.length - 1) && (data[currentIndex] == '@') && (Character.isLetter(data[currentIndex + 1]))) {
			StringBuilder value = new StringBuilder();
			currentIndex++;

			value.append(parseIdentifier().toString());

			token = new SmartScriptToken(SmartScriptTokenType.FUNC, value.toString());
			return true;
		}

		return false;
	}

	/**
	 * Checks whether the current token is a variable name and then creates the
	 * {@link SmartScriptTokenType#IDENT IDENT} token if so.
	 * 
	 * @return <code>true</code> if identifier token was generated, <code>false</code> otherwise
	 */
	private boolean generateIdentifier() {
		if(Character.isLetter(data[currentIndex])) {
			StringBuilder value = parseIdentifier();

			token = new SmartScriptToken(SmartScriptTokenType.IDENT, value.toString());
			return true;
		}

		return false;
	}

	/**
	 * If {@link #isIdentifier()} method is called, this method will parse the actual variable name
	 * and return it.
	 * 
	 * @return detected variable's name as a {@link StringBuilder} object
	 */
	private StringBuilder parseIdentifier() {
		StringBuilder value = new StringBuilder();

		while(Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_') {
			value.append(data[currentIndex++]);
		}

		return value;
	}

	/**
	 * Checks whether the current token is EOF and then creates the
	 * {@link SmartScriptTokenType#EOF EOF} token if so.
	 * 
	 * @return <code>true</code> if end of file (EOF) token was generated, <code>false</code> otherwise
	 */
	private boolean generateEndOfFile() {
		// if we are at the end of input text, we generate the EOF token
		if(currentIndex >= data.length) {
			token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
			return true;
		}

		return false;
	}

	/**
	 * Checks whether the current token is an integer or a double and then creates the
	 * {@link SmartScriptTokenType#INTEGER INTEGER} or {@link SmartScriptTokenType#DOUBLE DOUBLE}
	 * token if so.
	 * 
	 * @return <code>true</code> if number (integer or double) token was generated, <code>false</code> otherwise
	 */
	private boolean generateNumber() {
		StringBuilder value = new StringBuilder();

		// check if there is a number with minus sign directly in front of it
		if((currentIndex < data.length - 1) & (data[currentIndex] == '-') && (Character.isDigit(data[currentIndex + 1]))) {
			value.append(data[currentIndex++]);
		}

		// parse a number if there is one (explicit sign taken care of in the last if statement)
		if(Character.isDigit(data[currentIndex])) {
			value.append(parseNumber().toString());

			// check decimal or whole
			if(value.toString().contains(".")) {
				token = new SmartScriptToken(SmartScriptTokenType.DOUBLE, Double.parseDouble(value.toString()));
			} else {
				token = new SmartScriptToken(SmartScriptTokenType.INTEGER, Integer.parseInt(value.toString()));
			}

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

		while(Character.isDigit(data[currentIndex])) {
			value.append(data[currentIndex++]);
			if((currentIndex < data.length - 1) && (data[currentIndex] == '.') && (Character.isDigit(data[currentIndex + 1]))) {
				value.append(data[currentIndex++]);
			}
		}

		return value;
	}

	/**
	 * Checks whether this token is a string enclosed in quotes (valid
	 * text inside a tag) and then creates the {@link SmartScriptTokenType#TEXT TEXT} token if so.
	 * 
	 * @return <code>true</code> if text token (text inside tag) was generated, <code>false</code> otherwise
	 */
	private boolean generateTextInsideTag() {
		if(data[currentIndex] == '\"') {
			StringBuilder value = new StringBuilder();
			currentIndex++;

			while((currentIndex < data.length) && (data[currentIndex] != '\"')) {
				value.append(checkEscapeSequenceTag());
			}

			currentIndex++;
			token = new SmartScriptToken(SmartScriptTokenType.TEXT, value.toString());
			return true;
		}

		return false;
	}

	/**
	 * Checks whether the current token is normal body text (text outside of tags)
	 * and then creates the {@link SmartScriptTokenType#TEXT TEXT} token if so.
	 * 
	 * @return <code>true</code> if text token (body text) was generated, <code>false</code> otherwise
	 */
	private boolean generateBodyText() {
		StringBuilder text = new StringBuilder();

		while(true) {
			boolean escapeSequence = false;

			while(currentIndex < data.length && (data[currentIndex] != '$' || escapeSequence == true)) {
				escapeSequence = checkEscapeSequenceBody();
				text.append(data[currentIndex++]);
			}

			// if reached end of document, generate and set the next token to TEXT
			if(currentIndex >= data.length) {
				token = new SmartScriptToken(SmartScriptTokenType.TEXT, text.toString());
				return true;
			}

			// if exited while loop because of '$', check if '{' is in front (beginning of a tag)
			if((text.length() > 1) && (text.charAt(text.length() - 1) == '{')) {
				// delete the last read character (because we read a '{')
				text.deleteCharAt(text.length() - 1);
				token = new SmartScriptToken(SmartScriptTokenType.TEXT, text.toString());
				// go one step back we had a opening tag (so on the next nextToken() call it can be properly read)
				currentIndex--;
				return true;
			}

			// else, the '$' is not a part of opening tag, so append it and continue reading text
			text.append(data[currentIndex++]);
		}
	}

	/**
	 * Checks whether the current token is an opening of a tag and then creates the
	 * {@link SmartScriptTokenType#TAG_OPEN TAG_OPEN} token if so.
	 * 
	 * @return <code>true</code> if an opening tag token was generated, <code>false</code> otherwise
	 */
	private boolean generateOpeningTag() {
		if((currentIndex < data.length - 1) && (data[currentIndex] == '{') && (data[currentIndex + 1] == '$')) {
			token = new SmartScriptToken(SmartScriptTokenType.TAG_OPEN, null);
			currentIndex += 2;
			return true;
		}

		return false;
	}

	/**
	 * Checks whether the current token is a tag being closed and then creates the
	 * {@link SmartScriptTokenType#TAG_CLOSE TAG_CLOSE} token if so.
	 * 
	 * @return <code>true</code> if a closing tag token was generated, <code>false</code> otherwise
	 */
	private boolean generateClosingTag() {
		if((currentIndex < data.length - 1) && (data[currentIndex] == '$') && (data[currentIndex + 1] == '}')) {
			token = new SmartScriptToken(SmartScriptTokenType.TAG_CLOSE, null);
			currentIndex += 2;
			return true;
		}

		return false;
	}

	/**
	 * Checks if there is an escape sequence at current position and then checks its validity (in tag mode of this lexer).
	 * 
	 * @return <code>true</code> if the escape sequence is valid, <code>false</code> if there is no escape sequence detected
	 * @throws SmartScriptLexerException if the escape sequence is invalid
	 */
	private String checkEscapeSequenceTag() {
		StringBuilder escapeSequenceValue = new StringBuilder();
		// index of the next character in data array, using this so the if statement looks less clogged
		int i = currentIndex + 1;

		if(data[currentIndex] == '\\') {
			if((i >= data.length) || (data[i] != '\\' && data[i] != '\"' && data[i] != 'n' && data[i] != 't' && data[i] != 'r')) {
				throw new SmartScriptLexerException("An invalid escape sequence was detected inside a tag!");
			}

			currentIndex++;

			if(currentIndex < data.length) {
				if(data[currentIndex] == 'n') {
					escapeSequenceValue.append('\n');
				} else if(data[currentIndex] == 'r') {
					escapeSequenceValue.append('\r');
				} else if(data[currentIndex] == 't') {
					escapeSequenceValue.append('\t');
				} else {
					escapeSequenceValue.append(data[currentIndex]);
				}
			}
			
			currentIndex++;
			return escapeSequenceValue.toString();
		}
		
		return escapeSequenceValue.append(data[currentIndex++]).toString();
	}

	/**
	 * Checks if there is an escape sequence at current position and then checks its validity (in body mode of this lexer).
	 * 
	 * @return <code>true</code> if the escape sequence is valid, <code>false</code> if there is no escape sequence detected
	 * @throws SmartScriptLexerException if the escape sequence is invalid
	 */
	private boolean checkEscapeSequenceBody() {
		// index of the next character in data array, using this so the if statement looks less clogged
		int i = currentIndex + 1;
		
		if(data[currentIndex] == '\\') {
			if((i >= data.length) || (data[i] != '\\' && data[i] != '{')) {
				throw new SmartScriptLexerException("An invalid escape sequence was detected inside document body!");
			}
			currentIndex++;
			return true;
		}

		return false;
	}

	/**
	 * Helper method that determines if the current character is empty content.
	 * 
	 * @return <code>true</code> if current character is a space, tab or a newline, <code>false</code> otherwise
	 */
	private boolean isEmptyContent() {
		char c = data[currentIndex];
		if(c == ' ' || c == '\t' || c == '\r' || c == '\n') {
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
