package hr.fer.zemris.java.hw03.prob1;

/**
 * A list of states which define a "mode" in which a {@link Lexer} object is working.
 * 
 * @author Ivan Skorupan
 */
public enum LexerState {
	/** Lexer state in which we generate all types of tokens in existence. **/
	BASIC,
	/** Lexer state in which we only generate text token type, until we reach the '#' symbol **/
	EXTENDED;
}
