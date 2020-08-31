package hr.fer.zemris.java.custom.collections.demo;

import java.util.Objects;
import java.util.StringTokenizer;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * A class that demos the use of {@link ObjectStack} class implemented
 * in {@link hr.fer.zemris.java.custom.collections} package.
 * <p>
 * The program expects exactly one argument from the command line: a valid
 * postfix mathematical expression.
 * <p>
 * It supports the following mathematical operations between postfix expression operands:
 * <ul>
 * 	<li>addition - "+"</li>
 * 	<li>subtraction - "-"</li>
 *  <li>multiplication - "*"</li>
 *  <li>integer division - "/"</li>
 *  <li>remainder of integer division (modulo) - "%"</li>
 * </ul>
 * 
 * @author Ivan Skorupan
 * @see #ObjectStack
 * @see #SUPPORTED_OPERATIONS
 */
public class StackDemo {

	/**
	 * Number of arguments expected from the command line.
	 */
	private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
	
	/**
	 * An array of strings representing supported mathematical operations.
	 */
	private static final String[] SUPPORTED_OPERATIONS = {"+", "-", "*", "/", "%"};
	
	/**
	 * Implements a demo to showcase the functionality of {@link ObjectStack} class. 
	 * 
	 * @param args - command line arguments
	 * @see <code>ObjectStack</code>
	 */
	public static void main(String[] args) {
		if(args.length == 1) {
			// tokenize the postfix expression into operators and operands and create a new empty stack
			StringTokenizer tokenizer = new StringTokenizer(args[0]);
			ObjectStack stack = new ObjectStack();
			
			while(tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken(); // read the next operator or operand
				
				/*
				 * If the token read is a supported operator, pop 2 values from the stack and
				 * apply the operator to them. Afterwards, push the result on stack.
				 * 
				 * If the token read is a number, push it on the stack.
				 * 
				 * Otherwise, the program will throw an exception because the expression is not valid.
				 */
				if(isSupportedOperation(token)) {
					Integer secondOperand = (Integer)stack.pop();
					Integer firstOperand = (Integer)stack.pop();
					
					try {
						stack.push(performOperation(firstOperand, secondOperand, token));
					} catch(ArithmeticException ex) {
						System.out.println("Division by zero was detected!");
						System.exit(1);
					}
				} else {
					int number = 0;
					
					try {
						number = Integer.parseInt(token);
					} catch(NumberFormatException ex) {
						System.out.println("The expression is not valid, the numbers should all be integers!");
						System.out.println("Also, the operators must be supported (+, -, *, /, %).");
						System.exit(1);
					}
					
					stack.push(Integer.valueOf(number));
				}
			}
			
			if(stack.size() != 1) {
				System.out.println("There was an error calculating the expression, the stack size should be equal to 1.");
				System.out.println("Instead, the size is: " + stack.size());
			} else {
				System.out.println("Expression evaluates to " + stack.pop() + ".");
			}
		} else {
			System.out.println("Wrong number of arguments!");
			System.out.println("Got: " + args.length + ", expected: " + EXPECTED_NUMBER_OF_ARGUMENTS);
		}
	}

	/**
	 * Performs a supported mathematical operation on two operands.
	 * <p>
	 * If the operation is integer division or the remainder of integer division,
	 * then the secondOperand cannot be 0.
	 * <p>
	 * Also, none of the arguments can be <code>null</code>.
	 * 
	 * @param firstOperand - the first operand 
	 * @param secondOperand - the second operand
	 * @param operation - the operator
	 * @return the result of operation --> (<code>firstOperand</code>) operator (<code>secondOperand</code>)
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 * @throws ArithmeticException if division or remainder of division by 0 is detected 
	 */
	private static Integer performOperation(Integer firstOperand, Integer secondOperand, String operation) {
		Objects.requireNonNull(firstOperand);
		Objects.requireNonNull(secondOperand);
		Objects.requireNonNull(operation);
		
		if(operation.equals("+")) {
			return firstOperand + secondOperand;
		} else if(operation.equals("-")) {
			return firstOperand - secondOperand;
		} else if(operation.equals("*")) {
			return firstOperand * secondOperand;
		} else {
			if(secondOperand.equals(0)) {
				throw new ArithmeticException();
			}
			
			if(operation.equals("/")) {
				return firstOperand / secondOperand;
			} else {
				return firstOperand % secondOperand;
			}
		}
	}

	/**
	 * Tests if the mathematical operation represented by <code>token</code> is supported.
	 * <p>
	 * For a list of supported operations, see {@link #SUPPORTED_OPERATIONS}.
	 * 
	 * @param token - a string representing a desired mathematical operation
	 * @return <code>true</code> if the operation is supported, <code>false</code> otherwise
	 */
	private static boolean isSupportedOperation(String token) {
		for(String operation : SUPPORTED_OPERATIONS) {
			if(operation.equals(token)) {
				return true;
			}
		}
		
		return false;
	}

}
