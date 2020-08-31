package hr.fer.zemris.java.hw05.db;

/**
 * Implements concrete strategies for each comparison operator we are required to support.
 * 
 * @author Ivan Skorupan
 */
public class ComparisonOperators {
	
	/**
	 * A wild-card character.
	 */
	private static final char WILDCARD = '*';
	
	/**
	 * Operator that checks if one string is less than another string.
	 */
	public static final IComparisonOperator LESS = (v1, v2) -> v1.compareTo(v2) < 0;
	
	/**
	 * Operator that checks if one string is less than or equal to another string.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (v1, v2) -> v1.compareTo(v2) <= 0;
	
	/**
	 * Operator that checks if one string is greater than another string.
	 */
	public static final IComparisonOperator GREATER = (v1, v2) -> v1.compareTo(v2) > 0;
	
	/**
	 * Operator that checks if one string is greater than or equal to another string.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (v1, v2) -> v1.compareTo(v2) >= 0;
	
	/**
	 * Operator that checks if one string is equal to another string.
	 */
	public static final IComparisonOperator EQUALS = (v1, v2) -> v1.equals(v2);
	
	/**
	 * Operator that checks if one string is not equal to another string.
	 */
	public static final IComparisonOperator NOT_EQUALS = (v1, v2) -> !v1.equals(v2);
	
	/**
	 * Operator that checks if one string is of another string's form (is "like" another string).
	 */
	public static final IComparisonOperator LIKE;
	
	static {
		LIKE = new IComparisonOperator() {
			
			@Override
			public boolean satisfied(String value1, String value2) {
				// first count how many wild-card characters there are in value2
				char[] value2Content = value2.toCharArray();
				int numberOfWildcards = 0;
				for(char character : value2Content) {
					if(character == '*') {
						numberOfWildcards++;
					}
				}
				
				// value2 can contain a maximum of 1 wild-card character
				if(numberOfWildcards > 1) {
					throw new IllegalArgumentException("The second string literal cannot contain more than one wildcard!");
				}
				
				// if the second string is exactly a wild-card, return true
				if(value2.equals(String.valueOf(WILDCARD))) {
					return true;
				}
				
				// if the second string does not contain a wild-card, situation is trivial
				if(!value2.contains(String.valueOf(WILDCARD))) {
					return value1.compareTo(value2) == 0;
				}
				
				// if the second string does contain a wild-card
				String[] prefixAndSuffix = value2.split("\\*");
				
				if(value2.startsWith(String.valueOf(WILDCARD))) {
					if(value1.endsWith(prefixAndSuffix[0])) {
						return true;
					}
				} else if(value2.endsWith(String.valueOf(WILDCARD))) {
					if(value1.startsWith(prefixAndSuffix[0])) {
						return true;
					}
				} else {
					String prefix = prefixAndSuffix[0];
					String suffix = prefixAndSuffix[1];
					boolean sumOfPrefixAndSuffixCorrect = (prefix.length() + suffix.length()) <= (value1.length());
					
					if(value1.startsWith(prefix) && value1.endsWith(suffix) && sumOfPrefixAndSuffixCorrect) {
						return true;
					}
				}
				
				return false;
			}
			
		};
	}
	
}
