package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * This program takes input either from command line arguments or from the keyboard.
 * The parameters are the width and height of a rectangle and its area and perimeter are then calculated.
 * 
 * @author Ivan Skorupan
 */
public class Rectangle {

	/**
	 * This method is the starting point when running the program. It checks where to take input from and then parses width and height,
	 * after which it calculates rectangle's area and perimeter and prints them out on the screen.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		double width = 0, height = 0;

		if(args.length == 2) { // if there are two command line arguments, we will take them as input
			try {
				width = getRectangleDimensionFromCommandLine(args[0]);
				height = getRectangleDimensionFromCommandLine(args[1]);
			} catch(IllegalArgumentException ex) {
				return; // if command line parameters are incorrect, exit the program
			}
		} else if(args.length == 0) { // if no command line arguments are present, take input from user using a Scanner object
			Scanner scanner = new Scanner(System.in);
			width = getRectangleDimensionFromUser(scanner, "širinu");
			height = getRectangleDimensionFromUser(scanner, "visinu");
			scanner.close();
		} else { // otherwise, exit the program because there is an incorrect number of command line arguments
			System.out.println("Krivi broj argumenata.");
			return;
		}

		double perimeter = calculateRectanglePerimeter(width, height); // calculate the perimeter
		double area = calculateRectangleArea(width, height); // calculate the area

		System.out.printf("Pravokutnik širine %.1f i visine %.1f ima površinu %.1f te opseg %.1f.%n", width, height, area, perimeter);
	}

	/**
	 * This method will calculate the area of a rectangle with given dimensions using the formula --> area = width * height.
	 * The dimensions must be non-negative real numbers, otherwise the method throws an IllegalArgumentException.
	 * If either width or height is equal to zero (or both), the result is zero since such an object has no area.
	 * 
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @return area of the rectangle
	 */
	public static double calculateRectangleArea(double width, double height) {
		if(!checkRectangleValidity(width, height)) {
			throw new IllegalArgumentException("Širina i visina pravokutnika moraju biti nenegativni realni brojevi.");
		}
		return width * height;
	}

	/**
	 * This method will calculate the perimeter of a rectangle with given dimensions using the formula --> perimeter = 2 * width + 2 * height.
	 * The dimensions must be non-negative real numbers, otherwise the method throws an IllegalArgumentException.
	 * If either width or height is equal to zero (or both), the result is width + height (actually the length of a line, since such an object
	 * is either a single point (w = 0, h = 0) or a line ((w = 0 && h != 0) || (w != 0 && h == 0)).
	 * 
	 * @param width rectangle's width
	 * @param height rectangle's height
	 * @return perimeter of the rectangle
	 */
	public static double calculateRectanglePerimeter(double width, double height) {
		if(!checkRectangleValidity(width, height)) {
			throw new IllegalArgumentException("Širina i visina pravokutnika moraju biti nenegativni realni brojevi.");
		}
		return (width == 0 || height == 0) ? (width + height) : (2 * width + 2 * height);
	}
	
	/**
	 * This method checks whether a rectangle with given dimensions can exist (is valid).
	 * A rectangle is valid if it's dimensions are both positive real numbers.
	 * A rectangle whose either dimension or both are equal to zero is technically not a rectangle, but since we can
	 * still calculate its area and perimeter, we will allow that.
	 * 
	 * @param width rectangle's width
	 * @param height rectnagle's height
	 * @return true if rectangle is valid, false otherwise
	 */
	public static boolean checkRectangleValidity(double width, double height) {
		if(!checkDimensionDomain(width) || !checkDimensionDomain(height)) {
			return false;
		}
		return true;
	}
	
	/**
	 * This method checks whether the given rectangle's dimension is inside its domain.
	 * Any of the rectangle's dimensions are inside the acceptable domain if they are non-negative real numbers.
	 * 
	 * @param dimension one of rectangle's dimensions value (presumably width or height)
	 * @return true if the dimension is within its domain, false otherwise
	 */
	public static boolean checkDimensionDomain(double dimension) {
		if(dimension < 0) {
			System.out.println("Unijeli ste negativnu vrijednost.");
			return false;
		}
		return true;
	}
	
	/**
	 * This method tries to parse a double value from a string array at the given index.
	 * If successful, the double value is returned.
	 * If not successful, the method terminates the program since there is nothing we can do with incorrect arguments.
	 * 
	 * @param commandLineArgument a string containing one discrete command line argument
	 * @return rectangle dimension from a given string as a double value
	 */
	public static double getRectangleDimensionFromCommandLine(String commandLineArgument) {
		double dimension = 0;
		
		try {
			dimension = Double.parseDouble(commandLineArgument);
		} catch(NumberFormatException ex) {
			System.out.println("Jedna ili obje dimenzije su u krivom formatu, unesite nenegativan realan broj.");
			throw ex; // after printing an informative message, throw the exception again so it can be caught in the main method for program exit
		}
		
		if(!checkDimensionDomain(dimension)) {
			throw new IllegalArgumentException(); // if the dimension is outside acceptable numerical range, we throw an exception and exit the program in main method
		}
		return dimension;
	}

	/**
	 * This method takes an input from the user (the keyboard) and tries to parse a double value from it.
	 * When successful, the double value is returned and represents one of rectangle's dimensions (width or height).
	 * 
	 * @param scanner a reference to Scanner object that we will use for input
	 * @param dimensionName a string representing the given dimension's name
	 * @return rectangle dimension as a double value
	 */
	public static double getRectangleDimensionFromUser(Scanner scanner, String dimensionName) {
		double dimension = 0;

		while(true) {
			System.out.printf("Unesite %s > ", dimensionName);
			String inputLine = scanner.next(); // read a line from user

			try {
				dimension = Double.parseDouble(inputLine);
			} catch(NumberFormatException ex) {
				System.out.printf("'%s' se ne može protumačiti kao broj.%n", inputLine);
				continue; // if the input line cannot be parsed correctly, repeat the input process
			}
			
			if(!checkDimensionDomain(dimension)) {
				continue; // if the input value is outside acceptable numerical range, repeat the input process
			} else {
				break; // if everything is correctly parsed and checked, break the loop and return the result
			}
		}
		return dimension;
	}
	
}
