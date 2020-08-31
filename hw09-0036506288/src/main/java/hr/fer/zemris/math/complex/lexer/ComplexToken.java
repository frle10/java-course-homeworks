package hr.fer.zemris.math.complex.lexer;

import java.util.Objects;

/**
 * A model of a single token generated by {@link ComplexLexer} object.
 * 
 * @author Ivan Skorupan
 */
public class ComplexToken {
	
	/**
	 * This token's type.
	 */
	private ComplexTokenType type;
	
	/**
	 * This token's value.
	 */
	private Object value;
	
	/**
	 * Constructs a new {@link ComplexToken} object.
	 * 
	 * @param type - type of this token
	 * @param value - value of this token
	 */
	public ComplexToken(ComplexTokenType type, Object value) {
		this.type = Objects.requireNonNull(type);
		this.value = value;
	}
	
	/**
	 * Getter for this token's <code>value</code>.
	 * 
	 * @return this token's <code>value</code>
	 */
	public Object getValue() {
		return this.value;
	}
	
	/**
	 * Getter for this token's <code>type</code>.
	 * 
	 * @return this token's <code>type</code>
	 */
	public ComplexTokenType getType() {
		return this.type;
	}
	
}
