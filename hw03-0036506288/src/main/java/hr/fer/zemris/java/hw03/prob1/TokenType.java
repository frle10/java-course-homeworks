package hr.fer.zemris.java.hw03.prob1;

/**
 * A list of token types that can be generated while using the {@link Lexer} class.
 * 
 * @author Ivan Skorupan
 */
public enum TokenType {
	/** End of file **/
	EOF,
	/** Any string of letters is a valid word **/
	WORD,
	/** Anything representable by {@link Long} class **/
	NUMBER,
	/** Any single character that's not a word, number or a space character. **/
	SYMBOL;
}
