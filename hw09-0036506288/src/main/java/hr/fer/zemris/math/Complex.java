package hr.fer.zemris.math;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class implements support for working with complex numbers.
 * <p>
 * It represents an unmodifiable complex number. Each method that performs some
 * kind of modification returns a new instance of this class which represents the
 * modified number.
 * 
 * @author Ivan Skorupan
 */
public class Complex {

	/**
	 * The real part of this complex number.
	 */
	private double real;
	
	/**
	 * The imaginary part of this complex number.
	 */
	private double imaginary;
	
	/**
	 * Complex constant 0+0i.
	 */
	public static final Complex ZERO = new Complex(0,0);
	
	/**
	 * Complex constant 1+0i.
	 */
	public static final Complex ONE = new Complex(1,0);
	
	/**
	 * Complex constant -1+0i.
	 */
	public static final Complex ONE_NEG = new Complex(-1,0);
	
	/**
	 * Complex constant 0+1i.
	 */
	public static final Complex IM = new Complex(0,1);
	
	/**
	 * Complex constant 0-1i.
	 */
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/**
	 * Empty constructor that constructs a new {@link Complex}
	 * object with real and imaginary part equal to 0.
	 */
	public Complex() {
		this(0, 0);
	}
	
	/**
	 * Constructs a new {@link Complex} object with its real part equal
	 * to <code>real</code> and imaginary part equal to <code>imaginary</code>.
	 * 
	 * @param real - real part of this complex number
	 * @param imaginary - imaginary part of this complex number
	 */
	public Complex(double re, double im) {
		this.real = re;
		this.imaginary = im;
	}
	
	/**
	 * Calculates and returns the module (magnitude) of this complex number.
	 * 
	 * @return module (magnitude) of this complex number
	 */
	public double module() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}
	
	/**
	 * Multiplies two complex numbers and returns the result.
	 * 
	 * @param c - complex number to multiply this number with
	 * @return the result of multiplication
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public Complex multiply(Complex c) {
		Objects.requireNonNull(c);
		double realPart = (real * c.real) - (imaginary * c.imaginary);
		double imaginaryPart = (real * c.imaginary) + (imaginary * c.real);
		
		return new Complex(realPart, imaginaryPart);
	}
	
	/**
	 * Divides two complex numbers and returns the quotient.
	 * 
	 * @param c - complex number to divide this number with
	 * @return the quotient of this complex number and <code>c</code>
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public Complex divide(Complex c) {
		Objects.requireNonNull(c);
		double denominatorMagnitudeSquared = Math.pow(c.module(), 2);
		double realPart = (real * c.real + imaginary * c.imaginary) / denominatorMagnitudeSquared;
		double imaginaryPart = (imaginary * c.real - real * c.imaginary) / denominatorMagnitudeSquared;
		
		return new Complex(realPart, imaginaryPart);
	}
	
	/**
	 * Adds two complex numbers and returns the sum.
	 * 
	 * @param c - complex number to add to this number
	 * @return the sum of <code>c</code> and this complex number
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public Complex add(Complex c) {
		Objects.requireNonNull(c);
		return new Complex(real + c.real, imaginary + c.imaginary);
	}
	
	/**
	 * Subtracts two complex numbers and returns the difference.
	 * 
	 * @param c - complex number to subtract from this number
	 * @return the difference between this complex number and <code>c</code>
	 * @throws NullPointerException if <code>c</code> is <code>null</code>
	 */
	public Complex sub(Complex c) {
		Objects.requireNonNull(c);
		return new Complex(real - c.real, imaginary - c.imaginary);
	}
	
	/**
	 * Returns a new complex number representing this complex number
	 * negated.
	 * <p>
	 * If <b>z</b> is this complex number, then <b>-z</b> is returned.
	 * 
	 * @return this complex number negated
	 */
	public Complex negate() {
		return new Complex(-real, -imaginary);
	}
	
	/**
	 * Raises a complex number to the n-th power using the De Moivre's formula.
	 * 
	 * @param n - the power to which to raise this complex number
	 * @return this complex number raised to the n-th power
	 * @throws IllegalArgumentException if <code>n</code> is negative
	 */
	public Complex power(int n) {
		if(n < 0) {
			throw new IllegalArgumentException("The exponent must be a non-negative integer!");
		}
		
		double magnitude = Math.pow(module(), n);
		double angle = n * angle();
		
		return new Complex(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	/**
	 * Calculates all the n-th roots of this complex number using a method derived from
	 * De Moivre's formula and returns them in an array.
	 * 
	 * @param n - root degree to use for calculation
	 * @return an array containing all the n-th roots of this complex number
	 * @throws IllegalArgumentException if <code>n</code> is less than or equal to 0
	 */
	public List<Complex> root(int n) {
		if(n <= 0) {
			throw new IllegalArgumentException("The root degree must be greater than 0!");
		}
		
		List<Complex> roots = new ArrayList<>();
		double magnitude = Math.pow(module(), 1./n);
		
		for(int i = 0; i < n; i++) {
			double trigArgument = (angle() + 2. * i * Math.PI) / n;
			
			roots.add(new Complex(magnitude * Math.cos(trigArgument), magnitude * Math.sin(trigArgument)));
		}
		
		return roots;
	}
	
	/**
	 * Calculates and returns the angle in radians of this complex number.
	 * <p>
	 * The interval of returned angle is [0, 2 * PI].
	 * 
	 * @return angle of this complex number
	 */
	public double angle() {
		double arcTangentAngle = Math.atan2(imaginary, real);
		return (arcTangentAngle < 0) ? (arcTangentAngle + 2. * Math.PI) : arcTangentAngle;
	}

	/**
	 * Getter for real part of this complex number.
	 * 
	 * @return real part of this complex number
	 */
	public double getReal() {
		return real;
	}

	/**
	 * Getter for imaginary part of this complex number.
	 * 
	 * @return imaginary part of this complex number
	 */
	public double getImaginary() {
		return imaginary;
	}

	@Override
	public String toString() {
		return String.format("(%.1f%si%.1f)", (real == -0.0) ? abs(real) : real, (imaginary >= 0) ? "+" : "-", abs(imaginary));
	}

}
