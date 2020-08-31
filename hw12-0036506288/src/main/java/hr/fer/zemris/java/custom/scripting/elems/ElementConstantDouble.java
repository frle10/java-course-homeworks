package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Models an expression that is a <code>double</code> value.
 * 
 * @author Ivan Skorupan
 */
public class ElementConstantDouble extends Element {
	
	/**
	 * The biggest difference we will allow between two doubles to still be considered equal.
	 */
	private static final double DELTA = 1e-6;
	
	/**
	 * The numeric value of this <code>double</code> expression
	 */
	private double value;
	
	/**
	 * Constructs a new {@link ElementConstantDouble} object initialized
	 * with its <code>double</code> value.
	 * 
	 * @param value - this expressions <code>double</code> value
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}
	
	/**
	 * Returns the numeric value of this <code>double</code> expression.
	 * 
	 * @return value of this expression as a <code>double</code>
	 */
	public double getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		return Double.valueOf(value).toString();
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
		if (!(obj instanceof ElementConstantDouble))
			return false;
		ElementConstantDouble other = (ElementConstantDouble) obj;
		return Math.abs(this.value - other.value) < DELTA;
	}
	
}
