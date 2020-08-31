package hr.fer.zemris.java.hw17.jvdraw.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.geometry.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.Point;

/**
 * Models a child window that is used for editing the properties
 * of a selected {@link Circle}.
 * 
 * @author Ivan Skorupan
 */
public class CircleEditor extends GeometricalObjectEditor {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Circle that is being edited by this editor.
	 */
	private Circle circle;
	
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
	private JColorArea colorPreview;
	
	/**
	 * Constructs a new {@link CircleEditor} object.
	 * 
	 * @param circle - circle that is being edited by this editor
	 * @throws NullPointerException if <code>circle</code> is <code>null</code>
	 */
	public CircleEditor(Circle circle) {
		this.circle = Objects.requireNonNull(circle);
		initGUI();
	}
	
	/**
	 * Initializes GUI components on this {@link JPanel}.
	 */
	private void initGUI() {
		setLayout(new GridLayout(3, 3));
		
		JLabel center = new JLabel("Center point coordinates: ");
		JLabel radius = new JLabel("Radius: ");
		JLabel color = new JLabel("Color: ");
		
		centerX = new JTextField(((Integer) circle.getCenter().getX()).toString(), 20);
		centerY = new JTextField(((Integer) circle.getCenter().getY()).toString(), 20);
		radiusValue = new JTextField(((Integer) circle.getRadius()).toString(), 20);
		
		colorPreview = new JColorArea(circle.getFgColor());
		
		JButton chooseColor = new JButton("Choose color");
		chooseColor.addActionListener((e) -> {
			Color chosenColor = JColorChooser.showDialog(this, "Choose Color", circle.getFgColor());
			if(chosenColor != null) {
				colorPreview.setSelectedColor(chosenColor);
			}
		});
		
		add(center);
		add(centerX);
		add(centerY);
		add(radius);
		add(radiusValue);
		add(new JLabel());
		add(color);
		add(chooseColor);
		add(colorPreview);
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
		circle.setCenter(center);
		circle.setRadius(Integer.parseInt(radiusValue.getText()));
		circle.setFgColor(colorPreview.getCurrentColor());
	}
	
}
