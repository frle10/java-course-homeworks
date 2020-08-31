package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Models a complex polynomial with complex
 * coefficients in rooted form as follows:
 * <p>
 * <b>f(z) = z0 * (z - z1) * (z - z2) * ... * (z - zn)</b>,
 * where <b>z1 - zn</b> are its roots and <b>z0</b> is the constant.
 * <p>
 * A method is provided that converts an instance of
 * rooted polynomial into a general complex polynomial
 * form.
 * 
 * @author Ivan Skorupan
 */
public class ComplexRootedPolynomial {

	/**
	 * This rooted polynomial's constant.
	 */
	private Complex constant;
	
	/**
	 * This rooted polynomials array of roots.
	 */
	private Complex[] roots;
	
	/**
	 * Constructs a new {@link ComplexRootedPolynomial} object.
	 * <p>
	 * Takes the polynomial's constant and roots as arguments.
	 * 
	 * @param constant - this rooted polynomial's constant
	 * @param roots - this polynomial's roots
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.constant = Objects.requireNonNull(constant);
		this.roots = Objects.requireNonNull(roots);
	}
	
	/**
	 * Computes and returns the polynomial value at given
	 * point <code>z</code>.
	 * 
	 * @param z - point in complex plane to calculate the value of this polynomial in
	 * @return polynomial value at <code>z</code>
	 */
	public Complex apply(Complex z) {
		Complex result = new Complex();
		result = result.add(constant);
		
		for(Complex root : roots) {
			result = result.multiply(z.sub(root));
		}
		
		return result;
	}
	
	/**
	 * Converts this polynomial representation to
	 * {@link ComplexPolynomial} type.
	 * 
	 * @return this polynomial in {@link ComplexPolynomial} form
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial[] polynomials = new ComplexPolynomial[roots.length];
		for(int i = 0; i < polynomials.length; i++) {
			polynomials[i] = new ComplexPolynomial(Complex.ONE, roots[i].negate());
		}
		
		ComplexPolynomial result = new ComplexPolynomial(constant);
		for(ComplexPolynomial polynomial : polynomials) {
			result = result.multiply(polynomial);
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(constant + "*");
		
		for(Complex root : roots) {
			sb.append("(z-" + root + ")*");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}
	
	/**
	 * Finds index of closest root for given complex number <code>z</code>
	 * that is within <code>threshold</code>
	 * <p>
	 * If there is no such root, -1 is returned.
	 * <p>
	 * First root has index 0, second root has index 1 etc.
	 * 
	 * @param z - point in complex plane to find the closest root to
	 * @param threshold - farthest that the closest root can be from <code>z</code>
	 * in order for it to be considered as a result
	 * @return index of root closest to <code>z</code> if there is one within
	 * <code>threshold</code>, -1 otherwise
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		double closest = z.sub(roots[0]).module();
		int closestIndex = 0;
		
		for(int i = 1; i < roots.length; i++) {
			double distance = z.sub(roots[i]).module();
			if(distance < closest && distance <= threshold) {
				closest = distance;
				closestIndex = i;
			}
		}
		
		return (closest <= threshold) ? closestIndex : -1;
	}


}
