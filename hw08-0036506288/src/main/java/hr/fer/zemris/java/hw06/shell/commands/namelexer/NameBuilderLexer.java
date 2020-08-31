package hr.fer.zemris.java.hw06.shell.commands.namelexer;

import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.commands.MassrenameShellCommand;

/**
 * A simple lexer that tokenizes the expressions used as the last argument
 * of {@link MassrenameShellCommand massrename} shell command when MODIFIER
 * is "show" or "execute".
 * 
 * @author Ivan Skorupan
 */
public class NameBuilderLexer {
	
	/**
	 * Array of characters generated from the exoression string given to this lexer.
	 */
	private char[] data;
	
	/**
	 * Current index in {@link #data} array that we are at.
	 */
	private int currentIndex;
	
	/**
	 * Last generated token.
	 */
	private NameBuilderToken token;
	
	/**
	 * Constructs a new {@link NameBuilderLexer} object.
	 * 
	 * @param expression - name generating expression to tokenize
	 * @throws NullPointerException if <code>expression</code> is <code>null</code>
	 */
	public NameBuilderLexer(String expression) {
		Objects.requireNonNull(expression);
		this.data = expression.toCharArray();
	}
	
	/**
	 * Returns the last generated token.
	 * 
	 * @return last generated token
	 */
	public NameBuilderToken getToken() {
		return token;
	}
	
	/**
	 * Generates and returns the next token.
	 * 
	 * @return the next token in given expression
	 */
	public NameBuilderToken nextToken() {
		checkEOL();
		generateNextToken();
		return token;
	}
	
	/**
	 * This method actually implements the logic for lexical analysis and
	 * new token generation.
	 * 
	 * @throws NameBuilderLexerException if an unknown character sequence was encountered
	 */
	private void generateNextToken() {
		skipEmptyContent();
		
		if(generateEndOfLine()) {
			return;
		}
		
		if(generateText()) {
			return;
		}
		
		if(generateGroupOrExtGroup()) {
			return;
		}
		
		throw new NameBuilderLexerException("An unknown character sequence was detected in the expression.");
	}
	
	/**
	 * Generates tokens of type {@link NameBuilderTokenType#GROUP GROUP} or
	 * {@link NameBuilderTokenType#EXTGROUP EXTGROUP}.
	 * 
	 * @return <code>true</code> if a token of mentioned types was generated, <code>false</code> otherwise
	 * @throws NameBuilderLexerException if the format of gorup/extgroup token is invalid
	 */
	private boolean generateGroupOrExtGroup() {
		StringBuilder sb = new StringBuilder();
		currentIndex += 2;
		
		skipEmptyContent();
		while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
			sb.append(data[currentIndex++]);
		}
		
		if(sb.length() == 0) {
			throw new NameBuilderLexerException("The substitution expression requires at least a group specifier which must be a non-negative integer!");
		}
		
		skipEmptyContent();
		if(currentIndex < data.length && data[currentIndex] == '}') {
			token = new NameBuilderToken(NameBuilderTokenType.GROUP, sb.toString());
			currentIndex++;
			return true;
		}
		
		if(currentIndex < data.length && data[currentIndex] == ',') {
			sb.append(data[currentIndex++]);
			skipEmptyContent();
			while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
				sb.append(data[currentIndex++]);
			}
			
			if(sb.charAt(sb.length() - 1) == ',') {
				throw new NameBuilderLexerException("The format specifier of substitution expression is invalid!");
			}
			
			skipEmptyContent();
			if(currentIndex < data.length && data[currentIndex] == '}') {
				token = new NameBuilderToken(NameBuilderTokenType.EXTGROUP, sb.toString());
				currentIndex++;
				return true;
			}
		}
		
		throw new NameBuilderLexerException("The substitution expression is invalid!");
	}

	/**
	 * Generates tokens of type {@link NameBuilderTokenType#TEXT TEXT}.
	 * 
	 * @return <code>true</code> if a token of mentioned type was generated, <code>false</code> otherwise
	 */
	private boolean generateText() {
		StringBuilder sb = new StringBuilder();
		while(currentIndex < data.length && !isSubTagOpen()) {
			sb.append(data[currentIndex++]);
		}
		
		if(sb.length() == 0) return false;
		
		token = new NameBuilderToken(NameBuilderTokenType.TEXT, sb.toString());
		return true;
	}
	
	/**
	 * Generates tokens of type {@link NameBuilderTokenType#EOL EOL}.
	 * 
	 * @return <code>true</code> if a token of mentioned type was generated, <code>false</code> otherwise
	 */
	private boolean generateEndOfLine() {
		if(currentIndex >= data.length) {
			token = new NameBuilderToken(NameBuilderTokenType.EOL, null);
			return true;
		}
		return false;
	}
	
	/**
	 * Tests if we just encountered an opening of a substitution tag.
	 * 
	 * @return <code>true</code> if we have a substitution tag opening, <code>false</code> otherwise
	 */
	private boolean isSubTagOpen() {
		return (currentIndex < data.length - 1) && (data[currentIndex] == '$')
				&& (data[currentIndex + 1] == '{');
	}
	
	/**
	 * Check whether the EOL token was already read and then throws an exception if so because
	 * the current call of {@link #nextToken()} is invalid.
	 * 
	 * @throws NameBuilderLexerException if EOL was already generated
	 */
	private void checkEOL() {
		if(token != null && token.getType() == NameBuilderTokenType.EOL) {
			throw new NameBuilderLexerException("The end of expression was already reached!");
		}
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
	
	/**
	 * Helper method that determines if the current character is empty content.
	 * 
	 * @return <code>true</code> if current character is a space or a tab, <code>false</code> otherwise
	 */
	private boolean isEmptyContent() {
		char c = data[currentIndex];
		if(c == ' ' || c == '\t') {
			return true;
		}
		return false;
	}
	
}
