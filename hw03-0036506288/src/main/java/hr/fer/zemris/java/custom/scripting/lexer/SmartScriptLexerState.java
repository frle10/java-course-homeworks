package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * This is an enumeration of states that a {@link SmartScriptLexer} object can be set to.
 * 
 * @author Ivan Skorupan
 */
public enum SmartScriptLexerState {
	/** State that the lexer is in when we are inside body text (outside of tags) **/
	BODY,
	/** State that the lexer is in when we are inside tags **/
	TAG
}
