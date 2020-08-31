package hr.fer.zemris.java.gui.calc;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.UnaryOperator;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * This class provides <code>public static</code> methods for
 * producing {@link ActionListener} objects using different
 * strategies.
 * <p>
 * Many buttons in {@link Calculator} GUI have very similar
 * functions so the Strategy Design Pattern of programming
 * is a good choice here and this class is its heart.
 * 
 * @author Ivan Skorupan
 */
public class ButtonListenerFactory {
	
	/**
	 * Builds a listener for pop button.
	 * 
	 * @param parent - parent component of the error window that could possibly show up
	 * @param model - active calculator model through which the button should act
	 * @param stack - reference to a stack from which a value should be popped
	 * @return an {@link ActionListener} object that pops a number from the given <code>stack</code>
	 * @throws NullPointerException if any of the arguments are <code>null</code>
	 */
	public static ActionListener popListener(Component parent, CalcModel model, Stack<Double> stack) {
		checkNull(parent, model, stack);
		return e -> {
			if(stack.isEmpty()) {
				showError(parent, "The stack is empty so nothing can be popped!");
			} else {
				model.setValue(stack.pop());
			}
		};
	}
	
	/**
	 * Builds a listener for insert decimal point button.
	 * 
	 * @param parent - parent component of the error window that could possibly show up
	 * @param model - active calculator model through which the button should act
	 * @return an {@link ActionListener} object that insert a decimal point into the current displayed number
	 * @throws NullPointerException if any of the arguments are <code>null</code>
	 */
	public static ActionListener decimalPointListener(Component parent, CalcModel model) {
		checkNull(parent, model);
		return e -> {
			try {
				model.insertDecimalPoint();
			} catch(CalculatorInputException ex) {
				showError(parent, ex.getMessage());
			}
		};
	}
	
	public static ActionListener swapSignListener(Component parent, CalcModel model) {
		checkNull(parent, model);
		return e -> {
			try {
				model.swapSign();
			} catch(CalculatorInputException ex) {
				showError(parent, ex.getMessage());
			}
		};
	}
	
	/**
	 * Build a listener for digit buttons. Every digit button
	 * does the same thing, the only difference is their label.
	 * <p>
	 * Therefore, we can build a generic {@link ActionListener}
	 * that we can use for all such buttons.
	 * 
	 * @param parent - parent component of the error window that could possibly show up
	 * @param model - active calculator model through which the button should act
	 * @return an {@link ActionListener} object that inserts a new digit (button label digit)
	 * into current value of <code>model</code>
	 * @throws NullPointerException if any of the arguments are <code>null</code>
	 */
	public static ActionListener digitInputListener(Component parent, CalcModel model) {
		checkNull(parent, model);
		return e -> {
			JButton source = (JButton) e.getSource();
			try {
				model.insertDigit(Integer.parseInt(source.getText()));
			} catch(CalculatorInputException | IllegalArgumentException ex) {
				showError(parent, ex.getMessage());
			}
		};
	}
	
	/**
	 * Build a listener for unary operation buttons. Every such button
	 * does a very similar thing, it performs a unary operation on
	 * current calculator model value. The only difference is the
	 * actual operation.
	 * <p>
	 * Therefore, we can build a generic {@link ActionListener}
	 * that we can use for all such buttons.
	 * 
	 * @param model - active calculator model through which the button should act
	 * @param operator - unary operation that should be performed
	 * @return an {@link ActionListener} object that performs the unary operation
	 * (one that corresponds with the button's label) on current value of <code>model</code>
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public static ActionListener unaryOperationListener(CalcModel model, UnaryOperator<Double> operator) {
		checkNull(model, operator);
		return e -> model.setValue(operator.apply(model.getValue()));
	}
	
	/**
	 * Build a listener for binary operation buttons. Every such button
	 * does a very similar thing, it performs a binary operation on
	 * the pending operand and current calculator model value. The only
	 * difference is the actual operation.
	 * <p>
	 * Therefore, we can build a generic {@link ActionListener}
	 * that we can use for such buttons.
	 * 
	 * @param model - active calculator model through which the button should act
	 * @param operator - binary operation that should be performed
	 * @return an {@link ActionListener} object that performs the binary operation
	 * (one that corresponds with the button's label) on the pending operand and current
	 * value of <code>model</code>
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public static ActionListener binaryOperationListener(CalcModel model, DoubleBinaryOperator operator) {
		checkNull(model, operator);
		return e -> {
			if(model.getPendingBinaryOperation() != null) {
				model.setActiveOperand((model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue())));
			} else {
				model.setActiveOperand(model.getValue());
			}
			
			model.setPendingBinaryOperation(operator);
			model.clear();
		};
	}
	
	/**
	 * Build a listener for the equals button.
	 * <p>
	 * The equals button performs the pending binary operation on active
	 * operand and current value and sets the result as the new value of
	 * <code>model</code>. Afterwards, the active operand is cleared as
	 * well as the pending operation.
	 * 
	 * @param model - active calculator model through which the button should act
	 * @return an {@link ActionListener} object that performs the equals operation
	 * on <code>model</code>
	 * @throws NullPointerException if any of the arguments are <code>null</code>
	 */
	public static ActionListener equalsOperationListener(Component component, CalcModel model) {
		checkNull(component, model);
		return e -> {
			if(model.getPendingBinaryOperation() == null) {
				showError(component, "There is no pending operation to be performed!");
			} else {
				model.setValue((model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue())));
				model.clearActiveOperand();
				model.setPendingBinaryOperation(null);
			}
		};
	}
	
	/**
	 * Creates a {@link JOptionPane} error message dialog with
	 * given <code>message</code>.
	 * 
	 * @param parent - parent component of the error window
	 * @param message - error message dialog's text
	 */
	private static void showError(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Checks if any of the given objects is <code>null</code> and
	 * throws an exception if so.
	 * <p>
	 * If <code>objects</code> is <code>null</code>, the method
	 * does nothing.
	 * 
	 * @param objects - variable number of objects
	 * @throws NullPointerException if any of the given <code>objects</code> is <code>null</code>
	 */
	private static void checkNull(Object ... objects) {
		if(objects != null) {
			for(Object object : objects) {
				Objects.requireNonNull(object);
			}
		}
	}
	
}
