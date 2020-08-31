package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * This program takes a non-negative integer as input and calculates its factorial using a dedicated method.
 * 
 * @author Ivan Skorupan
 */
public class Factorial {
	
	private static final int MINIMUM_ALLOWED_INPUT_VALUE = 3; // minimum allowed input value whose factorial we will calculate
	private static final int MAXIMUM_ALLOWED_INPUT_VALUE = 20; // maximum input value whose factorial we will calculate
	
	/**
	 * This method is the starting point when running the program. It takes user input using a Scanner object and tries to parse a valid integer.
	 * Afterwards, it calls a method for calculating the input value's factorial.
	 * 
	 * @param args command line arguments, not used in this case
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in); // a scanner object to use for user input
		
		while(true) {
			System.out.print("Unesite broj > ");
			String inputLine = scanner.next(); // read the line from user
			
			if(!inputLine.equals("kraj")) {
				int inputNumber = 0;
				try {
					inputNumber = Integer.parseInt(inputLine); // try to parse a valid integer
				} catch(NumberFormatException ex) {
					System.out.printf("'%s' nije cijeli broj.%n", inputLine);
					continue; // if the input line is an incorrect value (not an integer readable by Integer.parseInt()), repeat the input process
				}
				
				// test if the input value is outside allowed range for this task
				if(inputNumber < MINIMUM_ALLOWED_INPUT_VALUE || inputNumber > MAXIMUM_ALLOWED_INPUT_VALUE) {
					System.out.printf("'%d' nije broj u dozvoljenom rasponu.%n", inputNumber);
					continue; // if outside allowed range, repeat the input process
				}
				
				long factorial = calculateFactorial(inputNumber); // calculate the factorial and store the result in a long integer variable (numbers can get big)
				System.out.printf("%d! = %d%n", inputNumber, factorial);
			} else {
				System.out.println("Doviđenja.");
				break;
			}
		}
		
		scanner.close(); // close the scanner object to correctly release resources
	}
	
	/**
	 * This method calculates the factorial of a given non-negative integer.
	 * 
	 * @param number a number whose factorial this method will attempt to calculate
	 * @return factorial of number (number!)
	 */
	public static long calculateFactorial(int number) {
		/*
		 * This method is able to calculate the factorial of any non-negative integer (as long as it's not too big).
		 * Even though in the main method we only allow input values from [3, 20], this method is constructed for general
		 * use where there is no such restriction.
		 * */
		
		// test if the argument is outside allowed range for this method
		if(number < 0) {
			throw new IllegalArgumentException("Broj mora biti nenegativan da bi se mogla izvršiti kalkulacija faktorijela!");
		}
		
		long factorial = 1; // set the initial value to one, so if the number argument is 0 or 1, the correct factorial will be returned
		while(number > 1) {
			factorial *= number--;
		}
		
		return factorial;
	}

}
