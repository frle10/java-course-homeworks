package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * Models one token of the input text.
 * 
 * @author Ivan Skorupan
 */
public class Token {
	
	/**
	 * This token's type.
	 */
	private TokenType type;
	
	/**
	 * This token's value.
	 */
	private Object value;
	
	/**
	 * Constructs a new {@link Token} object.
	 * 
	 * @param type - type of this token
	 * @param value - value of this token
	 */
	public Token(TokenType type, Object value) {
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
	public TokenType getType() {
		return this.type;
	}
	
}
