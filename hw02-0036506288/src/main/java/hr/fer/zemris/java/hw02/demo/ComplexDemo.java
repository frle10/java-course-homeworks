package hr.fer.zemris.java.hw02.demo;

import hr.fer.zemris.java.hw02.ComplexNumber;

/**
 * A class that demos the use of {@link ComplexNumber} class implemented
 * in {@link hr.fer.zemris.java.hw02} package.
 * <p>
 * The program does not expect any command line arguments.
 * 
 * @author Ivan Skorupan
 */
public class ComplexDemo {

	/**
	 * Implements a small demo to showcase the functionality of {@link ComplexNumber} class.
	 * 
	 * @param args - command line arguments, not used
	 */
	public static void main(String[] args) {
		ComplexNumber c1 = new ComplexNumber(2, 3);
		ComplexNumber c2 = ComplexNumber.parse("2.5-3i");
		ComplexNumber c3 = c1.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57)).div(c2).power(3).root(2)[1];
		
		System.out.println(c3);
		System.out.println();
		System.out.println(ComplexNumber.parse("1+i").getAngle());
		System.out.println(ComplexNumber.parse("-1+i").getAngle());
		System.out.println(ComplexNumber.parse("-1-i").getAngle());
		System.out.println(ComplexNumber.parse("1-i").getAngle());
	}
	
}
