package hr.fer.zemris.java.gui.calc.model.buttons;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JCheckBox;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Models an invertible button that performs a
 * unary mathematical operation, such as:
 * <p>
 * <b>sin, cos, tan, 1/x, ...</b>
 * 
 * @author Ivan Skorupan
 */
public class InvertibleUnaryOperationButton extends InvertibleButton {
	
	private static final long serialVersionUID = 4320422530754394620L;

	/**
	 * Constructs a new {@link InvertibleUnaryOperationButton} object.
	 * 
	 * @param label - button's label when not inverted
	 * @param invertedLabel - button's label when inverted
	 * @param calcModel - calculator model this button acts on
	 * @param invertingCheckBox - check-box that can invert this button when selected
	 * @param op1 - unary operation to perform when not inverted
	 * @param op2 - unary operation to perform when inverted
	 * @throws NullPointerException if <code>op1</code> or </code>op2</code> is <code>null</code>
	 */
	public InvertibleUnaryOperationButton(String label, String invertedLabel, CalcModel calcModel, JCheckBox invertingCheckBox,
			DoubleUnaryOperator op1, DoubleUnaryOperator op2) {
		super(label, invertedLabel, calcModel, invertingCheckBox);
		Objects.requireNonNull(op1);
		Objects.requireNonNull(op2);
		
		addActionListener(e -> {
			if(!invertingCheckBox.isSelected()) {
				calcModel.setValue(op1.applyAsDouble(calcModel.getValue()));
			} else {
				calcModel.setValue(op2.applyAsDouble(calcModel.getValue()));
			}
		});
	}
	
}
