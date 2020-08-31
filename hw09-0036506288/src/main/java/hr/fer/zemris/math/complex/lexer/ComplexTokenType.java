package hr.fer.zemris.math.complex.lexer;

/**
 * Enumeration of types of tokens that {@link ComplexLexer} can generate.
 * 
 * @author Ivan Skorupan
 */
public enum ComplexTokenType {
	/** Marks that there are no more tokens. **/
	EOL,
	/** Denotes an integer or a double. **/
	NUMBER,
	/** Denotes the imaginary unit 'i'. **/
	IMAGINARY_UNIT,
	/** Any single-character symbol. **/
	SYMBOL;
}
