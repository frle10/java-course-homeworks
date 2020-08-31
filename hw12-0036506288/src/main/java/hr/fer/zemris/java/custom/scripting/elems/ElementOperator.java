package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Models an expression that is a valid mathematical operator.
 * 
 * @author Ivan Skorupan
 */
public class ElementOperator extends Element {
	
	/**
	 * Symbol of this operator.
	 */
	private String symbol;
	
	/**
	 * Constructs a new {@link ElementOperator} object initialized with
	 * its {@link #symbol}.
	 * 
	 * @param symbol - symbol of this operator
	 */
	public ElementOperator(String symbol) {
		this.symbol = Objects.requireNonNull(symbol);
	}
	
	/**
	 * Returns this operator's {@link #symbol}.
	 * 
	 * @return {@link #symbol} of this operator
	 */
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String asText() {
		return symbol;
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ElementOperator))
			return false;
		ElementOperator other = (ElementOperator) obj;
		return Objects.equals(symbol, other.symbol);
	}
	
}
