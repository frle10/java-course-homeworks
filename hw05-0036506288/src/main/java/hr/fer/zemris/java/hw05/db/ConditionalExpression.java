package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Models a complete conditional expression that contains a reference to
 * {@link IFieldValueGetter} strategy, a reference to string literal and
 * a reference to {@link IComparisonOperator} strategy.
 * 
 * @author Ivan Skorupan
 */
public class ConditionalExpression {
	
	/**
	 * A reference to strategy that gets a certain field from database record.
	 */
	private IFieldValueGetter fieldGetter;
	
	/**
	 * A string literal that is being checked against in this conditional expression.
	 */
	private String stringLiteral;
	
	/**
	 * A reference to strategy that implements a comparison operator's behavior.
	 */
	private IComparisonOperator comparisonOperator;
	
	/**
	 * Constructs a new {@link ConditionalExpression} object.
	 * 
	 * @param fieldGetter - a reference to {@link IFieldValueGetter} strategy
	 * @param stringLiteral - a string literal that is being checked against
	 * @param comparisonOperator - a reference to {@link IComparisonOperator} strategy
	 * @throws NullPointerException if any of the parameters is null
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral, IComparisonOperator comparisonOperator) {
		this.fieldGetter = Objects.requireNonNull(fieldGetter);
		this.stringLiteral = Objects.requireNonNull(stringLiteral);
		this.comparisonOperator = Objects.requireNonNull(comparisonOperator);
	}

	/**
	 * Getter for {@link #fieldGetter}.
	 * 
	 * @return reference to {@link IFieldValueGetter} strategy
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}
	
	/**
	 * Getter for {@link #stringLiteral}.
	 * 
	 * @return this condition's string literal
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}
	
	/**
	 * Getter for {@link #comparisonOperator}.
	 * 
	 * @return reference to {@link IComparisonOperator} strategy
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comparisonOperator, fieldGetter, stringLiteral);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ConditionalExpression))
			return false;
		ConditionalExpression other = (ConditionalExpression) obj;
		return Objects.equals(comparisonOperator, other.comparisonOperator)
				&& Objects.equals(fieldGetter, other.fieldGetter) && Objects.equals(stringLiteral, other.stringLiteral);
	}
	
}
