package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;

/**
 * Models a label that acts as {@link Calculator} display.
 * <p>
 * The display is distinct from normal {@link JLabel} in
 * the fact that it has a black line border, yellow
 * background color and larger font.
 * 
 * @author Ivan Skorupan
 */
public class Display extends JLabel implements CalcValueListener {
	
	private static final long serialVersionUID = -6816144765306365422L;
	
	/**
	 * Constructs a new {@link Display} object.
	 * 
	 * @param text - display's initial text
	 */
	public Display(String text) {
		super(text);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setHorizontalAlignment(SwingConstants.RIGHT);
		this.setOpaque(true);
		this.setBackground(Color.YELLOW);
		this.setFont(this.getFont().deriveFont(30f));
	}

	@Override
	public void valueChanged(CalcModel model) {
		Objects.requireNonNull(model);
		setText(model.toString());
	}
	
}
