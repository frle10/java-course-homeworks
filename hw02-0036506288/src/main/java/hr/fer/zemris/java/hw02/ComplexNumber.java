package hr.fer.zemris.java.hw02;

import java.util.Objects;

/**
 * This class implements support for working with complex numbers.
 * <p>
 * It represents an unmodifiable complex number. Each method that performs some
 * kind of modification returns a new instance of this class which represents the
 * modified number.
 * <p>
 * It should be noted that the model is implemented so that two complex numbers are
 * considered equal if both their real and imaginary parts match up to a difference of
 * 10^(-6). See {@link #MINIMUM_EQUALITY_GAP}.
 * 
 * @author Ivan Skorupan
 */
public class ComplexNumber {
	
	/**
	 * A double constant representing the biggest difference that corresponding parts
	 * of two instances of {@link ComplexNumber} can have in order to still
	 * be considered equal.
	 */
	private static double MINIMUM_EQUALITY_GAP = 1e-6;
	
	/**
	 * The real part of this complex number.
	 */
	private double real;
	
	/**
	 * The imaginary part of this complex number.
	 */
	private double imaginary;
	
	/**
	 * Constructs a new {@link ComplexNumber} object with its real part equal
	 * to <code>real</code> and imaginary part equal to <code>imaginary</code>.
	 * 
	 * @param real - real part of this complex number
	 * @param imaginary - imaginary part of this complex number
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * Converts a real number given as a <code>double</code> to a complex number
	 * and returns it as a {@link ComplexNumber} object.
	 * 
	 * @param real - real number to be converted to a complex number
	 * @return a real number converted to a {@link ComplexNumber} object
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}
	
	/**
	 * Converts an imaginary number given as its <code>double</code> imaginary part
	 * to a complex number and returns it as a {@link ComplexNumber} object.
	 * 
	 * @param imaginary - imaginary number's imaginary part to be converted to a complex number
	 * @return an imaginary number converted to a {@link ComplexNumber} object
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}
	
	/**
	 * Calculates and returns a complex number given its magnitude and angle.
	 * 
	 * @param magnitude - magnitude of this complex number (absolute value squared)
	 * @param angle - angle of this complex number in radians
	 * @return a complex number constructed from given magnitude and angle
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		double realPart = magnitude * Math.cos(angle);
		double imaginaryPart = magnitude * Math.sin(angle);
		
		return new ComplexNumber(realPart, imaginaryPart);
	}
	
	/**
	 * Parses a complex number from the given string.
	 * <p>
	 * Valid complex number strings are defined as:
	 * <ul>
	 * 	<li>real numbers such as: "3", "3.14", "+1.7", "-2.13" etc.</li>
	 * 	<li>imaginary numbers such as: "3i", "3.14i", "+1.7i", "+i", "i", "-i", "-2.13i" etc.</li>
	 *  <li>full complex numbers such as: "3+3i", "1.7-3.14i", "+2+8i", "-7.56-9.57i" etc.</li>
	 * </ul>
	 * <p>
	 * Following rules also restrict the set of parsable strings:
	 * <ul>
	 * 	<li>a leading (+) is allowed for all valid complex numbers defined above</li>
	 * 	<li>plus or minus signs cannot appear next to each other, so these are not valid: "+-3i", "3.59-+7i", "++1" etc.</li>
	 *  <li>spaces in between numbers and signs are <b>not</b> allowed, so these are not valid: "3.14 - 8i", "3 + 77 . 5 i" etc.</li>
	 *  <li>spaces before and after the actual number are allowed because the string is trimmed</li>
	 *  <li>the imaginary unit cannot appear anywhere apart from the very end of the complex number, so these are not valid: "i8", "3.14+i7", "8i-3" etc.</li>
	 *  <li>in case of a real number, the imaginary unit does not appear, such as: "3", "8.1256", "+1.1", "-0" etc.</li>
	 * </ul>
	 * 
	 * @param s - a string representing a complex number to be parsed
	 * @return a parsed complex number converted to a <code>ComplexNumber</code> object
	 * @throws NullPointerException if <code>s</code> is <code>null</code>
	 * @throws NumberFormatException if <code>s</code> is empty or non-parsable
	 */
	public static ComplexNumber parse(String s) {
		Objects.requireNonNull(s);
		
		if(s.isEmpty()) {
			throw new NumberFormatException("The string is empty!");
		}
		
		String expression = s.trim(); // remove spaces before and after the complex number
		
		/*
		 * The following regular expression represents a pattern that exactly matches any complex number defined by the rules in
		 * the above Javadoc of this method.
		 * 
		 * The regex is conceptually divided in two groups: the real and the imaginary part of a complex number.
		 * 
		 * The real part: "([+-]?[0-9]*([0-9]+\\.\\d+)?)"
		 * The imaginary part: "(([+-]?[0-9]*([0-9]+\\.\\d+)?)[i])"
		 * 
		 * A negative lookahead trick is used to make complex numbers such as '3i' or '-2.5i' possible.
		 * 
		 * The negative lookahead part: "(?!(i|\\.|[0-9]+))" -> For a set of digits to be parsed as the real part of a complex
		 * number, it cannot be followed by an 'i', '.' or any digit from 0 to 9.
		 * 
		 * Both real and imaginary parts were made optional by using the '?' operator so we can parse pure real and pure imaginary numbers too
		 * as well as full complex numbers.
		 */
		String complexNumberPattern = "(([+-]?[0-9]*([0-9]+\\.\\d+)?)(?!(i|\\.|[0-9]+)))?(([+-]?[0-9]*([0-9]+\\.\\d+)?)[i])?";
		
		/*
		 * The grouping of regular expressions is done by the following rule: any opening brace '(' creates a new group and increases the groupCount by 1,
		 * unless it is followed by a '?' like this '(?' (that denotes a so called "non-capturing group").
		 * 
		 * Following that rule, we can easily see that the real part corresponds to group 2 of the regex, and the imaginary part corresponds to group 6
		 * of the regex above.
		 */
		String realPartGroup = "$2", imaginaryPartGroup = "$6";
		
		String realPart, imaginaryPart;
		double realValue = 0, imaginaryValue = 0;
		
		if(expression.matches(complexNumberPattern)) { // test if the input expression matches the regular expression
			realPart = expression.replaceAll(complexNumberPattern, realPartGroup);
			imaginaryPart = expression.replaceAll(complexNumberPattern, imaginaryPartGroup);
			
			if(!realPart.isEmpty()) { // if the real part string is not empty, we are sure to find a real number there so we can parse it
				realValue = Double.parseDouble(realPart);
			}
			
			if(expression.endsWith("i")) { // if the expression ends with 'i', then we are sure that this complex number has an imaginary part different from 0
				imaginaryValue = parseImaginaryPart(imaginaryPart);
			}
		} else { // if the expression does not match the complex number pattern
			throw new NumberFormatException("The complex number is in the wrong format!");
		}
		
		return new ComplexNumber(realValue, imaginaryValue);
	}
	
	/**
	 * Parses the imaginary part of a complex number.
	 * <p>
	 * The argument is a string containing the imaginary part of a complex number (without the imaginary unit i).
	 * 
	 * The string is either:
	 * <ul>
	 * 	<li>empty (corresponds to the imaginary number 'i' because there are no digits in front of i)</li>
	 *  <li>contains a single '+' sign (corresponds to the number '+i' with the leading plus)</li>
	 *  <li>contains a single '-' sign (corresponds to the number '-i' with the leading minus)</li>
	 *  <li>contains a real number with an optional leading sign ('+' or '-') and is therefore parsable
	 *  by the {@link Double#parseDouble(String)} method</li>
	 * </ul>
	 * 
	 * <p>
	 * In case the string is empty or contains a single '+' sign, the value of 1 is returned.
	 * <p>
	 * In case the string contains a single '-' sign, the value of -1 is returned.
	 * 
	 * @param imaginaryPart - a string representing the imaginary part of a complex number
	 * @return the parsed imaginary part of a complex number as a double value
	 */
	private static double parseImaginaryPart(String imaginaryPart) {
		if(!imaginaryPart.isEmpty() && !imaginaryPart.equals("+") && !imaginaryPart.equals("-")) {
			return Double.parseDouble(imaginaryPart); // there are digits in front of imaginary unit i, so we can parse it as a double
		} else {
			return (imaginaryPart.isEmpty() || imaginaryPart.equals("+")) ? 1. : -1.; // the string parsed contains either i, +i or -i
		}
	}
	
	/**
	 * Adds two complex numbers and returns the sum.
	 * 
	 * @param c - complex number to add to this number
	 * @return the sum of <code>c</code> and this complex number
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public ComplexNumber add(ComplexNumber c) {
		Objects.requireNonNull(c);
		return new ComplexNumber(real + c.real, imaginary + c.imaginary);
	}
	
	/**
	 * Subtracts two complex numbers and returns the difference.
	 * 
	 * @param c - complex number to subtract from this number
	 * @return the difference between this complex number and <code>c</code>
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public ComplexNumber sub(ComplexNumber c) {
		Objects.requireNonNull(c);
		return new ComplexNumber(real - c.real, imaginary - c.imaginary);
	}
	
	/**
	 * Multiplies two complex numbers and returns the result.
	 * 
	 * @param c - complex number to multiply this number with
	 * @return the result of multiplication
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public ComplexNumber mul(ComplexNumber c) {
		Objects.requireNonNull(c);
		double realPart = (real * c.real) - (imaginary * c.imaginary);
		double imaginaryPart = (real * c.imaginary) + (imaginary * c.real);
		
		return new ComplexNumber(realPart, imaginaryPart);
	}
	
	/**
	 * Divides two complex numbers and returns the quotient.
	 * 
	 * @param c - complex number to divide this number with
	 * @return the quotient of this complex number and <code>c</code>
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public ComplexNumber div(ComplexNumber c) {
		Objects.requireNonNull(c);
		double denominatorMagnitudeSquared = Math.pow(c.getMagnitude(), 2);
		double realPart = (real * c.real + imaginary * c.imaginary) / denominatorMagnitudeSquared;
		double imaginaryPart = (imaginary * c.real - real * c.imaginary) / denominatorMagnitudeSquared;
		
		return new ComplexNumber(realPart, imaginaryPart);
	}
	
	/**
	 * Raises a complex number to the n-th power using the De Moivre's formula.
	 * 
	 * @param n - the power to which to raise this complex number
	 * @return this complex number raised to the n-th power
	 * @throws IllegalArgumentException if <code>n</code> is negative
	 */
	public ComplexNumber power(int n) {
		if(n < 0) {
			throw new IllegalArgumentException("The exponent must be a non-negative integer!");
		}
		
		double magnitude = Math.pow(getMagnitude(), n);
		double angle = n * getAngle();
		
		return new ComplexNumber(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	/**
	 * Calculates all the n-th roots of this complex number using a method derived from
	 * De Moivre's formula and returns them in an array.
	 * 
	 * @param n - root degree to use for calculation
	 * @return an array containing all the n-th roots of this complex number
	 * @throws IllegalArgumentException if <code>n</code> is less than or equal to 0
	 */
	public ComplexNumber[] root(int n) {
		if(n <= 0) {
			throw new IllegalArgumentException("The root degree must be greater than 0!");
		}
		
		ComplexNumber[] roots = new ComplexNumber[n];
		double magnitude = Math.pow(getMagnitude(), 1./n);
		
		for(int i = 0; i < n; i++) {
			double trigArgument = (getAngle() + 2. * i * Math.PI) / n;
			
			roots[i] = new ComplexNumber(magnitude * Math.cos(trigArgument), magnitude * Math.sin(trigArgument));
		}
		
		return roots;
	}

	/**
	 * Gets the real part of this complex number and returns it.
	 * 
	 * @return real part of this complex number
	 */
	public double getReal() {
		return real;
	}

	/**
	 * Gets the imaginary part of this complex number and returns it.
	 * 
	 * @return imaginary part of this complex number
	 */
	public double getImaginary() {
		return imaginary;
	}
	
	/**
	 * Calculates and returns the magnitude of this complex number.
	 * 
	 * @return magnitude of this complex number
	 */
	public double getMagnitude() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}
	
	/**
	 * Calculates and returns the angle in radians of this complex number.
	 * <p>
	 * The interval of returned angle is [0, 2 * PI].
	 * 
	 * @return angle of this complex number
	 */
	public double getAngle() {
		double arcTangentAngle = Math.atan2(imaginary, real);
		return (arcTangentAngle < 0) ? (arcTangentAngle + 2. * Math.PI) : arcTangentAngle;
	}

	@Override
	public String toString() {
		if(imaginary == 0) {
			return String.format("%.3f", real);
		}
		else if(real == 0) {
			return String.format("%.3fi", imaginary);
		}
		else {
			return String.format("%.3f%s%.3fi", real, (imaginary >= 0) ? "+" : "", imaginary);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(imaginary);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(real);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Two complex numbers are considered equal if their real and imaginary parts match. The smallest difference between their real and imaginary parts
	 * (due to floating point memory limitations) is defined with {@link #MINIMUM_EQUALITY_GAP}.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ComplexNumber))
			return false;
		
		ComplexNumber other = (ComplexNumber) obj;
		if((Math.abs(real - other.real) > MINIMUM_EQUALITY_GAP) || (Math.abs(imaginary - other.imaginary) > MINIMUM_EQUALITY_GAP)) {
			return false;
		}
		
		return true;
	}
	
	
}
