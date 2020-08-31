package hr.fer.zemris.java.hw17.jvdraw.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.geometry.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.Point;

/**
 * Models a child window that is used for editing the properties
 * of a selected {@link FilledCircle}.
 * 
 * @author Ivan Skorupan
 */
public class FilledCircleEditor extends GeometricalObjectEditor {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Filled circle that is being edited by this editor.
	 */
	private FilledCircle filledCircle;
	
	/**
	 * Text field for x coordinate of circle center.
	 */
	private JTextField centerX;
	
	/**
	 * Text field of y coordinate of circle center.
	 */
	private JTextField centerY;
	
	/**
	 * Text field for circle radius value.
	 */
	private JTextField radiusValue;
	
	/**
	 * A color area previewing the object's foreground color.
	 */
	private JColorArea outlineColorPreview;
	
	/**
	 * A color area previewing the object's fill color.
	 */
	private JColorArea fillColorPreview;
	
	/**
	 * Constructs a new {@link CircleEditor} object.
	 * 
	 * @param circle - circle that is being edited by this editor
	 * @throws NullPointerException if <code>circle</code> is <code>null</code>
	 */
	public FilledCircleEditor(FilledCircle filledCircle) {
		this.filledCircle = Objects.requireNonNull(filledCircle);
		initGUI();
	}
	
	/**
	 * Initializes GUI components on this {@link JPanel}.
	 */
	private void initGUI() {
		setLayout(new GridLayout(4, 0));
		
		JLabel center = new JLabel("Center point coordinates: ");
		JLabel radius = new JLabel("Radius: ");
		JLabel outlineColor = new JLabel("Outline color: ");
		JLabel fillColor = new JLabel("Fill color: ");
		
		centerX = new JTextField(((Integer) filledCircle.getCenter().getX()).toString(), 20);
		centerY = new JTextField(((Integer) filledCircle.getCenter().getY()).toString(), 20);
		radiusValue = new JTextField(((Integer) filledCircle.getRadius()).toString(), 20);
		
		outlineColorPreview = new JColorArea(filledCircle.getFgColor());
		fillColorPreview = new JColorArea(filledCircle.getFillColor());
		
		JButton chooseOutlineColor = new JButton("Choose outline color");
		chooseOutlineColor.addActionListener((e) -> {
			Color chosenColor = JColorChooser.showDialog(this, "Choose Outline Color", filledCircle.getFgColor());
			if(chosenColor != null) {
				outlineColorPreview.setSelectedColor(chosenColor);
			}
		});
		
		JButton chooseFillColor = new JButton("Choose fill color");
		chooseFillColor.addActionListener((e) -> {
			Color chosenColor = JColorChooser.showDialog(this, "Choose Fill Color", filledCircle.getFillColor());
			if(chosenColor != null) {
				fillColorPreview.setSelectedColor(chosenColor);
			}
		});
		
		add(center);
		add(centerX);
		add(centerY);
		add(radius);
		add(radiusValue);
		add(new JLabel());
		add(outlineColor);
		add(chooseOutlineColor);
		add(outlineColorPreview);
		add(fillColor);
		add(chooseFillColor);
		add(fillColorPreview);
	}
	
	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(centerX.getText());
			Integer.parseInt(centerY.getText());
			Integer.parseInt(radiusValue.getText());
			
			acceptEditing();
		} catch(NumberFormatException ex) {
			throw ex;
		}
	}
	
	@Override
	public void acceptEditing() {
		Point center = new Point(Integer.parseInt(centerX.getText()), Integer.parseInt(centerY.getText()));
		filledCircle.setCenter(center);
		filledCircle.setRadius(Integer.parseInt(radiusValue.getText()));
		filledCircle.setFgColor(outlineColorPreview.getCurrentColor());
		filledCircle.setFillColor(fillColorPreview.getCurrentColor());
	}
	
}
