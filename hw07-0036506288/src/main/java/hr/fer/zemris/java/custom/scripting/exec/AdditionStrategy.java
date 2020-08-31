package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * An implementation of {@link BiFunction} that can add two numbers together
 * and return the result of addition.
 * <p>
 * What's special about it is that this strategy can add two arguments of type
 * {@link Object} because it is aware of the rules described in documentation
 * of {@link ValueWrapper}.
 * 
 * @author Ivan Skorupan
 */
public class AdditionStrategy implements BiFunction<Object, Object, Object> {

	@Override
	public Object apply(Object t, Object u) {
		if(t instanceof Double && u instanceof Double) {
			return ((Double) t).doubleValue() + ((Double) u).doubleValue();
		} else if(t instanceof Double && u instanceof Integer) {
			return ((Double) t).doubleValue() + ((Integer) u).doubleValue();
		} else if(t instanceof Integer && u instanceof Double) {
			return ((Integer) t).doubleValue() + ((Double) u).doubleValue();
		} else {
			return ((Integer) t).intValue() + ((Integer) u).intValue();
		}
	}
	
}
