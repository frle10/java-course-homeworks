package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Models a complex polynomial with complex
 * coefficients in general form as follows:
 * <p>
 * <b>f(z) = zn * z^n + z(n-1) * z^(n-1) + z2 * z^2 + z1 * z + z0</b>,
 * where <b>z0 - zn</b> are its coefficients next to corresponding
 * powers of <b>z</b>.
 * <p>
 * The above is a polynomial of polinom order <b>n</b>.
 * 
 * @author Ivan Skorupan
 */
public class ComplexPolynomial {
	
	/**
	 * Coefficients of this polynomial.
	 */
	private Complex[] factors;
	
	/**
	 * Constructs a new {@link ComplexPolynomial} object.
	 * <p>
	 * Takes this polynomial's coefficients as an argument.
	 * 
	 * @param factors - this polynomial's coefficients
	 * @throws NullPointerException if <code>factors</code> is <code>null</code>
	 */
	public ComplexPolynomial(Complex ... factors) {
		this.factors = Objects.requireNonNull(factors);
	}
	
	/**
	 * Returns the order of this polynomial.
	 * <p>
	 * E.g. for <b>f(z) = (7 + 2i) * z^3 + 2 * z^2 + 5 * z + 1</b>, 3 is returned.
	 * 
	 * @return this polynomial's order
	 */
	public short order() {
		return (short) (factors.length - 1);
	}
	
	/**
	 * Computes and returns a new polynomial equal to
	 * <b><code>this</code> * <code>p</code></b>.
	 * 
	 * @param p - polynomial to multiply this polynomial with
	 * @return result of multiplication as a new polynomial
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		short resultOrder = (short) (order() + p.order());
		Complex[] resultFactors = new Complex[resultOrder + 1];
		
		for(int i = 0; i < resultFactors.length; i++) {
			resultFactors[i] = new Complex();
		}
		
		for(int i = 0; i < factors.length; i++) {
			for(int j = 0; j < p.factors.length; j++) {
				resultFactors[i + j] = resultFactors[i + j].add(factors[i].multiply(p.factors[j]));
			}
		}
		
		return new ComplexPolynomial(resultFactors);
	}
	
	/**
	 * Computes and returns the first derivative of this polynomial.
	 * <p>
	 * For example, for:
	 * <p>
	 * <b>f(z) = (7 + 2i) * z^3 + 2 * z^2 + 5 * z + 1</b>,
	 * <p>
	 * <b>f'(z) = (21 + 6i) * z^2 + 4 * z + 5</b>
	 * <p>
	 * is returned.
	 * 
	 * @return first derivative of this polynomial
	 */
	public ComplexPolynomial derive() {
		Complex[] derivedFactors = new Complex[factors.length - 1];
		short order = order();
		
		for(int i = 0; i < factors.length - 1; i++) {
			derivedFactors[i] = factors[i].multiply(new Complex(order - i, 0));
		}
		
		return new ComplexPolynomial(derivedFactors);
	}
	
	/**
	 * Computes and returns polynomial value at given point
	 * <code>z</code>.
	 * 
	 * @param z - point at which to compute the polynomial value
	 * @return polynomial value at given point <code>z</code>
	 * @throws NullPointerException if <code>z</code> is <code>null</code>
	 */
	public Complex apply(Complex z) {
		Objects.requireNonNull(z);
		Complex value = new Complex();
		short order = order();
		
		for(int i = 0; i < factors.length; i++) {
			value = value.add(factors[i].multiply(z.power(order - i)));
		}
		
		return value;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if(factors.length > 0) {
			for(int i = 0; i < factors.length; i++) {
				if(i != factors.length - 1) sb.append(factors[i] + "*z^" + (order() - i) + "+");
				else sb.append(factors[i]);
			}
		}
		
		return sb.toString();
	}
	
}
