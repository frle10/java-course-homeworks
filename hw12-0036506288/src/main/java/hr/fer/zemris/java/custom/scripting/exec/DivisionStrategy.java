package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * An implementation of {@link BiFunction} that can divide two numbers
 * and return the result of division.
 * <p>
 * What's special about it is that this strategy can divide two arguments of type
 * {@link Object} because it is aware of the rules described in documentation
 * of {@link ValueWrapper}.
 * 
 * @author Ivan Skorupan
 */
public class DivisionStrategy implements BiFunction<Object, Object, Object> {

	@Override
	public Object apply(Object t, Object u) {
		if(t instanceof Double && u instanceof Double) {
			return ((Double) t).doubleValue() / ((Double) u).doubleValue();
		} else if(t instanceof Double && u instanceof Integer) {
			return ((Double) t).doubleValue() / ((Integer) u).doubleValue();
		} else if(t instanceof Integer && u instanceof Double) {
			return ((Integer) t).doubleValue() / ((Double) u).doubleValue();
		} else {
			if(((Integer) u).intValue() == 0) {
				throw new RuntimeException("An integer cannot be divided by 0!");
			}
			return ((Integer) t).intValue() / ((Integer) u).intValue();
		}
	}
	
}
