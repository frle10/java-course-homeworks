package hr.fer.zemris.java.gui.calc.model.buttons;

import java.awt.event.ActionListener;

/**
 * Models a calculator button that has a digit label
 * and is used for doing digit input to the calculator's
 * display.
 * 
 * @author Ivan Skorupan
 */
public class DigitButton extends CalculatorButton {
	
	private static final long serialVersionUID = 10283297308141556L;
	
	/**
	 * Constructs a new {@link DigitButton} object. 
	 * 
	 * @param label - button's label
	 * @param listener - button's action listener
	 */
	public DigitButton(String label, ActionListener listener) {
		super(label, listener);
		setFont(getFont().deriveFont(30f));
	}
	
}
