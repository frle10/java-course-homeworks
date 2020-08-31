package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Enumeration of types of tokens that {@link SmartScriptLexer} can generate.
 * 
 * @author Ivan Skorupan
 */
public enum SmartScriptTokenType {
	/** Marks that there are no more tokens. **/
	EOF,
	/** Normal body text or text inside tags enclosed in quotes. **/
	TEXT,
	/** A whole number **/
	INTEGER,
	/** A real number in digits-dot-digits format. **/
	DOUBLE,
	/** A valid variable name **/
	IDENT,
	/** A function **/
	FUNC,
	/** Single characters that are not numbers, text or valid variable names **/
	SYMBOL,
	/** An opening tag string ("{$") **/
	TAG_OPEN,
	/** A closing tag string ("$}") **/
	TAG_CLOSE;
}
