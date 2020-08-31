package hr.fer.zemris.java.custom.scripting.exec;

import java.util.Comparator;

/**
 * An implementation of {@link Comparator} that can compare two numbers
 * and return the result of comparison.
 * <p>
 * What's special about it is that this strategy can compare two arguments of type
 * {@link Object} because it is aware of the rules described in documentation
 * of {@link ValueWrapper}.
 * 
 * @author Ivan Skorupan
 */
public class ComparisonStrategy implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		if(o1 instanceof Double && o2 instanceof Double) {
			return ((Double) o1).compareTo((Double) o2);
		} else if(o1 instanceof Double && o2 instanceof Integer) {
			Double o1Val = ((Double) o1).doubleValue();
			Double o2Val = ((Integer) o2).doubleValue();
			return o1Val.compareTo(o2Val);
		} else if(o1 instanceof Integer && o2 instanceof Double) {
			Double o1Val = ((Integer) o1).doubleValue();
			Double o2Val = ((Double) o2).doubleValue();
			return o1Val.compareTo(o2Val);
		} else {
			return ((Integer) o1).compareTo((Integer) o2);
		}
	}
	
}
