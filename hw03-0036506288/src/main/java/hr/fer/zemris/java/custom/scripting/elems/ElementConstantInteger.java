package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Models an expression that is an <code>int</code> value.
 * 
 * @author Ivan Skorupan
 */
public class ElementConstantInteger extends Element {
	
	/**
	 * The numeric value of this <code>int</code> expression
	 */
	private int value;
	
	/**
	 * Constructs a new {@link ElementConstantInteger} object initialized
	 * with its <code>int</code> value.
	 * 
	 * @param value - this expressions's <code>int</code> value
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * Returns the numeric value of this <code>int</code> expression.
	 * 
	 * @return value of this expression as an <code>int</code>
	 */
	public int getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return Integer.valueOf(value).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ElementConstantInteger))
			return false;
		ElementConstantInteger other = (ElementConstantInteger) obj;
		return value == other.value;
	}
	
}
