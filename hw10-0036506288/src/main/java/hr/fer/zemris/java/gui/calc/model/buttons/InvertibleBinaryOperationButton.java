package hr.fer.zemris.java.gui.calc.model.buttons;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

import javax.swing.JCheckBox;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Models an invertible button that performs a
 * binary mathematical operation, such as:
 * <p>
 * <b>+, -, *, /, x^n, ...</b>
 * 
 * @author Ivan Skorupan
 */
public class InvertibleBinaryOperationButton extends InvertibleButton {
	
	private static final long serialVersionUID = -7569848495763623308L;

	/**
	 * Constructs a new {@link InvertibleBinaryOperationButton} object.
	 * 
	 * @param label - button's label when not inverted
	 * @param invertedLabel - button's label when inverted
	 * @param calcModel - calculator model this button acts on
	 * @param invertingCheckBox - check-box that can invert this button when selected
	 * @param op1 - binary operation to perform when not inverted
	 * @param op2 - binary operation to perform when inverted
	 * @throws NullPointerException if <code>op1</code> or </code>op2</code> is <code>null</code>
	 */
	public InvertibleBinaryOperationButton(String label, String invertedLabel, CalcModel calcModel, JCheckBox invertingCheckBox,
			DoubleBinaryOperator op1, DoubleBinaryOperator op2) {
		super(label, invertedLabel, calcModel, invertingCheckBox);
		Objects.requireNonNull(op1);
		Objects.requireNonNull(op2);
		
		addActionListener(e -> {
			if(calcModel.getPendingBinaryOperation() != null) {
				calcModel.setActiveOperand((calcModel.getPendingBinaryOperation().applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue())));
			} else {
				calcModel.setActiveOperand(calcModel.getValue());
			}
			
			if(!invertingCheckBox.isSelected()) {
				calcModel.setPendingBinaryOperation(op1);
			} else {
				calcModel.setPendingBinaryOperation(op2);
			}
			
			calcModel.clear();
		});
	}
	
}
