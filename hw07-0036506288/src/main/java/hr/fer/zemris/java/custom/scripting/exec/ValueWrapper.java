package hr.fer.zemris.java.custom.scripting.exec;

import java.util.Objects;

/**
 * Models objects that can wrap any kind of object as
 * their value (it is stored internally).
 * <p>
 * When wrapped, any kind of operation can be done on
 * the stored object.
 * <p>
 * This class implements arithmetic and comparison operations
 * for stored objects (but can only be applied if the current
 * stored object instance is of certain prescribed type).
 * <p>
 * The rules for applying implemented mathematical calculations are
 * as follows:
 * <ol>
 * 	<li>If either current value or argument is <code>null</code>, that value is treated as being
 * equal to {@link Integer} with value 0.</li>
 * 	<li> If current value and argument are not null, they can be instances of {@link Integer}, {@link Double} or {@link String}.
 * For each value that is a {@link String}, it is checked if string literal is a decimal value (i.e. if somewhere
 * it has a symbol '.' or 'E'). If it is a decimal value, it is treated as such; otherwise, it is treated as an Integer.
 * </li>
 * 	<li>If either current value or argument is {@link Double}, operation is performed on Doubles, and
 * the result is stored as an instance of {@link Double}. If not, both arguments must be Integers so the
 * operation is performed on Integers and the result stored as an {@link Integer}.</li>
 * </ol>
 * Additional rules for comparison operation is:
 * <ul>
 * 	<li>If both values are <code>null</code>, they are treated as equal.</li>
 * 	<li>If one is <code>null</code> and the other is not, the null-value is treated as being equal to an integer with value 0.</li>
 * 	<li>Otherwise, both values are promoted to the same type as described for arithmetic methods and then the comparison is performed.</li>
 * </ul>
 * 
 * @author Ivan Skorupan
 */
public class ValueWrapper {

	/**
	 * Internally stored wrapped object.
	 */
	private Object value;

	/**
	 * Constructs a new {@link ValueWrapper} object.
	 * <p>
	 * The argument to the constructor is the value to be
	 * wrapped by this wrapper. That value can be <code>null</code>.
	 * 
	 * @param value
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Getter for wrapped object.
	 * 
	 * @return internally stored object reference
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Setter for wrapped object.
	 * 
	 * @param value - object reference to set as the new wrapped value in this wrapper
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Adds the current value with provided argument value.
	 * <p>
	 * This method modifies the current wrapped value.
	 * <p>
	 * Since both the stored value and argument are of type
	 * {@link Object}, addition will not always be possible.
	 * <p>
	 * The rules are described in documentation of
	 * {@link ValueWrapper}.
	 * 
	 * @param incValue - value to be added to current wrapped value
	 * @throws RuntimeException if it is established that one of the addition
	 * operands are not valid
	 */
	public void add(Object incValue) {
		value = checkAndPrepareInstance(value);
		incValue = checkAndPrepareInstance(incValue);

		AdditionStrategy as = new AdditionStrategy();
		value = as.apply(value, incValue);
	}

	/**
	 * Subtracts the argument value from current value.
	 * <p>
	 * This method modifies the current wrapped value.
	 * <p>
	 * Since both the stored value and argument are of type
	 * {@link Object}, subtraction will not always be possible.
	 * <p>
	 * The rules are described in documentation of
	 * {@link ValueWrapper}.
	 * 
	 * @param decValue - value to subtract from current wrapped value
	 * @throws RuntimeException if it is established that one of the subtraction
	 * operands are not valid
	 */
	public void subtract(Object decValue) {
		value = checkAndPrepareInstance(value);
		decValue = checkAndPrepareInstance(decValue);

		SubtractionStrategy ss = new SubtractionStrategy();
		value = ss.apply(value, decValue);
	}

	/**
	 * Multiplies the current value by provided argument value.
	 * <p>
	 * This method modifies the current wrapped value.
	 * <p>
	 * Since both the stored value and argument are of type
	 * {@link Object}, multiplication will not always be possible.
	 * <p>
	 * The rules are described in documentation of
	 * {@link ValueWrapper}.
	 * 
	 * @param mulValue - value to multiply the current wrapped value by
	 * @throws RuntimeException if it is established that one of the multiplication
	 * operands are not valid
	 */
	public void multiply(Object mulValue) {
		value = checkAndPrepareInstance(value);
		mulValue = checkAndPrepareInstance(mulValue);

		MultiplicationStrategy ms = new MultiplicationStrategy();
		value = ms.apply(value, mulValue);
	}

	/**
	 * Divides the current value with provided argument value.
	 * <p>
	 * This method modifies the current wrapped value.
	 * <p>
	 * Since both the stored value and argument are of type
	 * {@link Object}, division will not always be possible.
	 * <p>
	 * The rules are described in documentation of
	 * {@link ValueWrapper}.
	 * 
	 * @param divValue - value to divide the current wrapped value by
	 * @throws RuntimeException if it is established that one of the division
	 * operands are not valid
	 */
	public void divide(Object divValue) {
		value = checkAndPrepareInstance(value);
		divValue = checkAndPrepareInstance(divValue);

		DivisionStrategy ds = new DivisionStrategy();
		value = ds.apply(value, divValue);
	}

	/**
	 * Compares the current value with provided argument value.
	 * <p>
	 * Since both the stored value and argument are of type
	 * {@link Object}, comparison will not always be possible.
	 * <p>
	 * The rules are described in documentation of
	 * {@link ValueWrapper}.
	 * 
	 * @param withValue - value to compare the current wrapped value with
	 * @throws RuntimeException if it is established that one of the comparison
	 * operands are not valid
	 */
	public int numCompare(Object withValue) {
		value = checkAndPrepareInstance(value);
		withValue = checkAndPrepareInstance(withValue);

		ComparisonStrategy cs = new ComparisonStrategy();
		return cs.compare(value, withValue);
	}
	
	/**
	 * Checks if the provided object reference is of valid type
	 * in order to perform an arithmetic or comparison operation
	 * with it.
	 * <p>
	 * Returns a reference to an object using following rules:
	 * <ul>
	 * 	<li>If <code>o</code> is <code>null</code>, it returns an {@link Integer} of value 0.</li>
	 * 	<li>If <code>o</code> is a {@link String}, it tries to parse a numeric value from it and returns it.</li>
	 * 	<li>Otherwise, <code>o</code> itself is returned unchanged.</li>
	 * </ul>
	 * 
	 * @param o - object to check and prepare
	 * @return object reference based on above-mentioned rules
	 * @throws RuntimeException if <code>o</code> is not of valid type for mathematical and comparison operations
	 * or if there was an error parsing the numerical value from <code>o</code> in case it is a {@link String}.
	 */
	private Object checkAndPrepareInstance(Object o) {
		if(!isOfValidInstance(o)) {
			throw new RuntimeException("One of the operands is not of appropriate type for this operation to be performed!");
		}

		if(o == null) return Integer.valueOf(0);
		if(o instanceof String) return parseString((String) o);
		return o;
	}

	/**
	 * Tests if <code>o</code> is of valid type for performing arithmetic or comparison operation
	 * with it.
	 * 
	 * @param o - object to check the instance of
	 * @return <code>true</code> if <code>o</code> is of valid type for mathematical and conparison operations,
	 * <code>false</code> otherwise
	 */
	private boolean isOfValidInstance(Object o) {
		return !(o != null && !(o instanceof Integer) && !(o instanceof Double) && !(o instanceof String));
	}

	/**
	 * Parses a numeric value ({@link Double}, {@link Integer}) from the passed string.
	 * <p>
	 * If no numeric value could be parsed, an exception is thrown.
	 * 
	 * @param o - string to parse the numeric value from
	 * @return parsed numeric value of the given string
	 * @throws RuntimeException if parsing resulted in failure because the string is not a valid number
	 * @throws NullPointerException if <code>o</code> is <code>null</code>
	 */
	private Number parseString(String o) {
		Objects.requireNonNull(o);
		
		if(o.contains(".") || o.toUpperCase().contains("E")) {
			try {
				return Double.parseDouble(o);
			} catch(NumberFormatException ex) {
				throw new RuntimeException("Cannot parse a double value from an operand because the string is not a valid floating point number!");
			}
		} else {
			try {
				return Integer.parseInt(o);
			} catch(NumberFormatException ex) {
				throw new RuntimeException("Cannot parse an integer value from an operand because the string is not a valid integer!");
			}
		}
	}

}
