package hr.fer.zemris.java.gui.calc.model.buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JCheckBox;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Models a calculator button whose label and mathematical operation
 * can be inverted.
 * <p>
 * An example is a button with label "sin" that, when clicked, performs
 * a sine operation on current calculator display value. If such a button
 * is inverted, the label would say "arcsin" and the button would perform
 * the arc sine operation.
 * 
 * @author Ivan Skorupan
 */
public class InvertibleButton extends CalculatorButton implements ActionListener {
	
	private static final long serialVersionUID = 3577784072870652267L;

	/**
	 * Button's standard (non-inverted) label.
	 */
	private String label;
	
	/**
	 * Button's label when inverted.
	 */
	private String invertedLabel;
	
	/**
	 * Calculator model this button acts on.
	 */
	private CalcModel calcModel;
	
	/**
	 * Reference to the check-box that can invert this button when selected. 
	 */
	private JCheckBox invertingCheckBox;
	
	/**
	 * Constructs a new {@link InvertibleButton} object.
	 * 
	 * @param label - button's label when not inverted
	 * @param invertedLabel - button's label when inverted
	 * @param calcModel - calculator model this button acts on
	 * @param invertingCheckBox - check-box that can invert this button when selected
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public InvertibleButton(String label,  String invertedLabel, CalcModel calcModel, JCheckBox invertingCheckBox) {
		super(label);
		this.label = Objects.requireNonNull(label);
		this.invertedLabel = Objects.requireNonNull(invertedLabel);
		this.calcModel = Objects.requireNonNull(calcModel);
		this.invertingCheckBox = Objects.requireNonNull(invertingCheckBox);
		invertingCheckBox.addActionListener(this);
	}

	/**
	 * Getter for this button's non-inverted label.
	 * 
	 * @return this button's label when not inverted
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Getter for this button's inverted label.
	 * 
	 * @return this button's label when inverted
	 */
	public String getInvertedLabel() {
		return invertedLabel;
	}
	
	/**
	 * Getter for reference to calculator model this
	 * button acts on
	 * 
	 * @return this button's calculator model reference
	 */
	public CalcModel getCalcModel() {
		return calcModel;
	}

	/**
	 * Getter for check-box that can invert this button
	 * 
	 * @return reference to check-box that can invert this button
	 */
	public JCheckBox getInvertingCheckBox() {
		return invertingCheckBox;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(invertingCheckBox.isSelected()) {
			setText(invertedLabel);
		} else {
			setText(label);
		}
	}
	
}
