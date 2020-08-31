package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * An implementation of a simple lexical analyzer.
 * <p>
 * It is implemented as a "lazy" lexer, which means it extracts tokens only
 * when explicitly asked to do so using a method for fetching the next token.
 * 
 * @author Ivan Skorupan
 */
public class Lexer {
	
	/**
	 * Input text to be lexically analyzed as an array of characters.
	 */
	private char[] data;
	
	/**
	 * The current token.
	 */
	private Token token;
	
	/**
	 * Index of first unprocessed character in the {@link #data} array.
	 */
	private int currentIndex;
	
	/**
	 * Current lexer state.
	 */
	private LexerState state;
	
	/**
	 * Constructs a new {@link Lexer} object. Takes input text
	 * to be tokenized as a {@link String} argument.
	 * 
	 * @param text - text to be lexically analyzed
	 * @throws NullPointerException if <code>text</code> is <code>null</code>
	 */
	public Lexer(String text) {
		Objects.requireNonNull(text);
		data = text.toCharArray();
		this.token = null;
		this.currentIndex = 0;
		this.state = LexerState.BASIC;
	}
	
	/**
	 * Sets the lexer in a desired state from {@link LexerState} enumeration.
	 * 
	 * @param state - a state to which to set the lexer
	 * @throws NullPointerException if <code>state</code> is <code>null</code>
	 */
	public void setState(LexerState state) {
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
	public Token getToken() {
		return this.token;
	}
	
	/**
	 * Generates, sets and returns the next token.
	 * 
	 * @return the next token in input text
	 * @throws LexerException if there is an error while generating the next token
	 */
	public Token nextToken() {
		generateNextToken();
		return getToken();
	}
	
	/**
	 * Helper method which contains actual logic required for generating the next token in the input text.
	 * 
	 * @throws LexerException if there is an error while generating the next token
	 */
	private void generateNextToken() {
		// if we already read EOF, then this method call is invalid and we throw an exception
		if(token != null && token.getType() == TokenType.EOF) {
			throw new LexerException();
		}
		
		// skip all blanks until next point of interest
		skipEmptyContent();
		
		// if we are at the end of input text, we generate the EOF token
		if(currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		// if we are in extended mode, change the reading rules
		if(state == LexerState.EXTENDED) {
			if(data[currentIndex] == '#') {
				token = new Token(TokenType.SYMBOL, Character.valueOf(data[currentIndex]));
				currentIndex++;
				return;
			}
			
			StringBuilder value = new StringBuilder();
			while(currentIndex < data.length && !isEmptyContent() && !(data[currentIndex] == '#')) {
				value.append(data[currentIndex]);
				currentIndex++;
			}
			
			token = new Token(TokenType.WORD, value.toString());
			return;
		}
		
		// test if we have a letter or an escape sequence (start of a word)
		if(Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
			StringBuilder value = new StringBuilder();
			
			while(currentIndex < data.length && (Character.isLetter(data[currentIndex]) || checkEscapeSequence() == true)) {
				value.append(data[currentIndex]);
				currentIndex++;
			}
			
			token = new Token(TokenType.WORD, value.toString());
			return;
		}
		
		// test if we have a number
		if(Character.isDigit(data[currentIndex])) {
			StringBuilder value = new StringBuilder();
			
			while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
				value.append(data[currentIndex]);
				currentIndex++;
			}
			
			long number = 0;
			try {
				number = Long.parseLong(value.toString());
			} catch(NumberFormatException ex) {
				throw new LexerException();
			}
			
			token = new Token(TokenType.NUMBER, Long.valueOf(number));
			return;
		}
		
		// if not a word, number or EOF, it is a symbol
		token = new Token(TokenType.SYMBOL, Character.valueOf(data[currentIndex]));
		currentIndex++;
		return;
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
	
	/**
	 * Helper method that checks if the current character is a backslash to enable support for escape sequences.
	 * 
	 * @return <code>true</code> if a valid escape sequence is found, <code>false</code> otherwise
	 */
	private boolean checkEscapeSequence() {
		if(data[currentIndex] == '\\') {
			if((currentIndex + 1 >= data.length) || (!Character.isDigit(data[currentIndex + 1]) && data[currentIndex + 1] != '\\')) {
				throw new LexerException();
			}
			currentIndex++;
			return true;
		}
		
		return false;
	}
	
}
