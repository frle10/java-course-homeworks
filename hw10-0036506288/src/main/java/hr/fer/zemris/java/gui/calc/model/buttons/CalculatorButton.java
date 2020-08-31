package hr.fer.zemris.java.gui.calc.model.buttons;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import hr.fer.zemris.java.gui.calc.Calculator;

/**
 * Models a button that can be found on {@link Calculator}.
 * <p>
 * Every calculator button has a light gray background color
 * and an action listener which defines what happens when
 * the button is clicked.
 * <p>
 * An action listener does not necessarily need to be defined,
 * but in our calculator every button will have one.
 * 
 * @author Ivan Skorupan
 */
public class CalculatorButton extends JButton {
	
	private static final long serialVersionUID = -6720573604796151238L;

	/**
	 * Constructs a new {@link CalculatorButton} object.
	 * 
	 * @param label - button's label
	 */
	public CalculatorButton(String label) {
		this(label, null);
	}
	
	/**
	 * Constructs a new {@link CalculatorButton} object.
	 * 
	 * @param label - button's label
	 * @param listener - button's action listener
	 */
	public CalculatorButton(String label, ActionListener listener) {
		super(label);
		setOpaque(true);
		setBackground(Color.LIGHT_GRAY);
		if(listener != null) {
			addActionListener(listener);
		}
	}
	
}
