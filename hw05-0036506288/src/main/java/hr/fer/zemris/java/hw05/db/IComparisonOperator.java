package hr.fer.zemris.java.hw05.db;

/**
 * A strategy for a comparison operator (such as "<" or ">=").
 * 
 * @author Ivan Skorupan
 */
public interface IComparisonOperator {
	
	/**
	 * Tests the boolean outcome of applying a desired comparison operator on strings
	 * <code>value1</code> and <code>value2</code>.
	 * 
	 * @param value1 - first string
	 * @param value2 - second string
	 * @return <code>true</code> if (<code>value1</code> OPERATOR <code>value2</code>) is correct, <code>false</code> otherwise
	 */
	public boolean satisfied(String value1, String value2);
	
}
