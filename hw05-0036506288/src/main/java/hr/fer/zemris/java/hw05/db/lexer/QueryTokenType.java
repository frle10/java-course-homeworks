package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Enumeration of types of tokens that {@link QueryLexer} can generate.
 * 
 * @author Ivan Skorupan
 */
public enum QueryTokenType {
	/** End of line **/
	EOL,
	/** A valid command, logical operator or attribute name **/
	IDENTIFIER,
	/** A string literal **/
	LITERAL,
	/** Supported operators (e.g. =, >, <, >=, <=, !=) **/
	OPERATOR;
}
