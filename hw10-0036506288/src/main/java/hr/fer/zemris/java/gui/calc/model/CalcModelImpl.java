package hr.fer.zemris.java.gui.calc.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

/**
 * This is an implementation of {@link CalcModel} interface.
 * 
 * @author Ivan Skorupan
 */
public class CalcModelImpl implements CalcModel {
	
	/**
	 * Marks if the model is editable or not.
	 */
	private boolean editable = true;
	
	/**
	 * Marks the sign of current value.
	 */
	private boolean negative;
	
	/**
	 * String that contains textual representation of current value.
	 */
	private String typedDigits = "";
	
	/**
	 * Current value.
	 */
	private double value;
	
	/**
	 * Operand that's waiting for an operation to be done.
	 */
	private Double activeOperand;
	
	/**
	 * Operation that's currently active and is waiting for the second operand.
	 */
	private DoubleBinaryOperator pendingOperation;
	
	/**
	 * List of all listeners that are interested in observing if
	 * the value inside this model has changed.
	 */
	private List<CalcValueListener> listeners = new LinkedList<>();
	
	/**
	 * Notifies all listeners from {@link #listeners} about
	 * the change of current value in this model.
	 */
	private void notifyListeners() {
		for(CalcValueListener listener : listeners) {
			listener.valueChanged(this);
		}
	}
	
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		Objects.requireNonNull(l);
		listeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		Objects.requireNonNull(l);
		listeners.remove(l);
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public void setValue(double value) {
		this.value = value;
		
		if(value < 0) negative = true;
		else negative = false;
		
		if(value == Double.NaN) {
			typedDigits = "NaN";
		} else if(value == Double.NEGATIVE_INFINITY) {
			typedDigits = "-Infinity";
		} else if(value == Double.POSITIVE_INFINITY) {
			typedDigits = "Infinity";
		} else {
			typedDigits = Double.valueOf(value).toString();
		}
		
		editable = false;
		notifyListeners();
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		typedDigits = "";
		value = 0;
		negative = false;
		editable = true;
		notifyListeners();
	}

	@Override
	public void clearAll() {
		activeOperand = null;
		pendingOperation = null;
		clear();
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if(!editable) {
			throw new CalculatorInputException("The calculator is currently not editable!");
		}
		
		negative = !negative;
		value = -value;
		
		if(!typedDigits.isEmpty()) {
			typedDigits = (negative) ? "-" + typedDigits : typedDigits.substring(1, typedDigits.length());
		}
		notifyListeners();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if(!editable) {
			throw new CalculatorInputException("The calculator is currently not editable!");
		}
		
		if(typedDigits.isEmpty()) {
			throw new CalculatorInputException("No digits were typed so a decimal point couldn't be added!");
		}
		
		if(typedDigits.contains(".")) {
			throw new CalculatorInputException("The number already contains a decimal point!");
		}
		
		typedDigits = typedDigits + ".";
		notifyListeners();
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if(digit < 0 || digit > 9) {
			throw new IllegalArgumentException("The digit needs to be in the range 0-9!");
		}
		
		if(!editable) {
			throw new CalculatorInputException("The calculator is currently not editable!");
		}
		
		String newDigits = typedDigits + digit;
		double newValue = 0;
		try {
			newValue = Double.parseDouble(newDigits);
			
			if(newValue == Double.POSITIVE_INFINITY || newValue == Double.NEGATIVE_INFINITY) {
				throw new CalculatorInputException("The number typed is too big to be worked with!");
			}
		} catch(NumberFormatException ex) {
			throw new CalculatorInputException("The digits typed are not parsable into a real number!");
		}
		
		typedDigits = newDigits;
		value = newValue;
		notifyListeners();
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeOperand != null;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if(activeOperand == null) {
			throw new IllegalStateException("Active operand is not set!");
		}
		
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
	}

	@Override
	public void clearActiveOperand() {
		activeOperand = null;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		pendingOperation = op;
	}

	@Override
	public String toString() {
		if(typedDigits.isEmpty()) {
			return (negative) ? "-0" : "0";
		} else {
			return removeLeadingZeros(typedDigits);
		}
	}
	
	/**
	 * Removes leading zeros from given <code>text</code>.
	 * <p>
	 * If the given text is a decimal number, the zero
	 * before the decimal point is not removed.
	 * 
	 * @param text - string to remove leading zeros from
	 * @return <code>text</code> with removed leading zeros
	 */
	private String removeLeadingZeros(String text) {
		StringBuilder sb = new StringBuilder(text);
		for(int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if(c != '0') break;
			if(c == '0' && i < text.length() - 1 && text.charAt(i + 1) != '.') {
				sb.deleteCharAt(0);
			}
		}
		
		return sb.toString();
	}
	
}
