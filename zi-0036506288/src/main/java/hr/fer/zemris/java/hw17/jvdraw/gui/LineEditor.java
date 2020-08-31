package hr.fer.zemris.java.hw17.jvdraw.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.geometry.Line;
import hr.fer.zemris.java.hw17.jvdraw.geometry.Point;

/**
 * Models a child window that is used for editing the properties
 * of a selected {@link Line}.
 * 
 * @author Ivan Skorupan
 */
public class LineEditor extends GeometricalObjectEditor {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Line that is being edited by this editor.
	 */
	private Line line;
	
	/**
	 * Text field for this line's start point x coordinate.
	 */
	private JTextField startX;
	
	/**
	 * Text field for this line's start point y coordinate.
	 */
	private JTextField startY;
	
	/**
	 * Text field for this line's end point x coordinate.
	 */
	private JTextField endX;
	
	/**
	 * Text field for this line's end point y coordinate.
	 */
	private JTextField endY;
	
	/**
	 * A color area previewing the object's foreground color.
	 */
	private JColorArea colorPreview;
	
	/**
	 * Constructs a new {@link LineEditor} object.
	 * 
	 * @param line - line that is being edited by this editor
	 * @throws NullPointerException if <code>line</code> is <code>null</code>
	 */
	public LineEditor(Line line) {
		this.line = Objects.requireNonNull(line);
		initGUI();
	}
	
	/**
	 * Initializes GUI components on this {@link JPanel}.
	 */
	private void initGUI() {
		setLayout(new GridLayout(3, 0));
		
		JLabel start = new JLabel("Start point coordinates:");
		JLabel end = new JLabel("End point coordinates:");
		JLabel color = new JLabel("Color:");
		
		startX = new JTextField(((Integer) line.getStartPoint().getX()).toString(), 20);
		startY = new JTextField(((Integer) line.getStartPoint().getY()).toString(), 20);
		endX = new JTextField(((Integer) line.getEndPoint().getX()).toString(), 20);
		endY = new JTextField(((Integer) line.getEndPoint().getY()).toString(), 20);
		
		colorPreview = new JColorArea(line.getFgColor());
		
		JButton chooseColor = new JButton("Choose color");
		chooseColor.addActionListener((e) -> {
			Color chosenColor = JColorChooser.showDialog(this, "Choose Color", line.getFgColor());
			if(chosenColor != null) {
				colorPreview.setSelectedColor(chosenColor);
			}
		});
		
		add(start);
		add(startX);
		add(startY);
		add(end);
		add(endX);
		add(endY);
		add(color);
		add(chooseColor);
		add(colorPreview);
	}

	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(startX.getText());
			Integer.parseInt(startY.getText());
			Integer.parseInt(endX.getText());
			Integer.parseInt(endY.getText());
			
			acceptEditing();
		} catch(NumberFormatException ex) {
			throw ex;
		}
	}
	
	@Override
	public void acceptEditing() {
		Point startPoint = new Point(Integer.parseInt(startX.getText()), Integer.parseInt(startY.getText()));
		Point endPoint = new Point(Integer.parseInt(endX.getText()), Integer.parseInt(endY.getText()));
		Color selectedColor = colorPreview.getCurrentColor();
		
		line.setStartPoint(startPoint);
		line.setEndPoint(endPoint);
		line.setFgColor(selectedColor);
	}
	
}
