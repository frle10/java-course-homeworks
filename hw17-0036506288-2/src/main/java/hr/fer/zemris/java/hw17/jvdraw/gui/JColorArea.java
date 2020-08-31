package hr.fer.zemris.java.hw17.jvdraw.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.jvdraw.interfaces.ColorChangeListener;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.IColorProvider;

/**
 * Models a custom component which can be clicked to show
 * a {@link JColorChooser} and is also used to preview
 * the currently selected color.
 * 
 * @author Ivan Skorupan
 */
public class JColorArea extends JComponent implements IColorProvider {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Width of this component.
	 */
	private static final int WIDTH = 15;
	
	/**
	 * Height of this component.
	 */
	private static final int HEIGHT = 15;
	
	/**
	 * Max width of this component.
	 */
	private static final int MAX_WIDTH = 35;
	
	/**
	 * Max height of this component.
	 */
	private static final int MAX_HEIGHT = 35;
	
	/**
	 * List of listeners that are interested in color change on this color area component.
	 */
	private List<ColorChangeListener> listeners = new ArrayList<>();
	
	/**
	 * Currently selected color for this color area component.
	 */
	private Color selectedColor;
	
	/**
	 * Constructs a new {@link JColorArea} object with provided
	 * initial selected color <code>selectedColor</code>.
	 * <p>
	 * This constructor also registers a mouse listener on this component
	 * which, when triggered with a mouse click, shows a {@link JColorChooser}
	 * dialog to allow the user to choose a new color for this component.
	 * 
	 * @param selectedColor - initial selected color for this color area component
	 * @throws NullPointerException if <code>selectedColor</code> is <code>null</code>
	 */
	public JColorArea(Color selectedColor) {
		this.selectedColor = Objects.requireNonNull(selectedColor);
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color chosenColor = JColorChooser.showDialog(JColorArea.this, "Choose a color", selectedColor);
				
				if(chosenColor != null) {
					setSelectedColor(chosenColor);
				}
			}
		});
	}
	
	/**
	 * Sets the currently selected color for this color area component.
	 * 
	 * @param selectedColor - color to be set as selected
	 * @throws NullPointerException if <code>selectedColor</code> is <code>null</code>
	 */
	public void setSelectedColor(Color selectedColor) {
		Color oldColor = this.selectedColor;
		this.selectedColor = Objects.requireNonNull(selectedColor);
		repaint();
		notifyListeners(oldColor, selectedColor);
	}

	/**
	 * Helper method that notifies all {@link ColorChangeListener} about the
	 * color change event of this color area component.
	 */
	private void notifyListeners(Color oldColor, Color newColor) {
		for(ColorChangeListener l : listeners) {
			l.newColorSelected(this, oldColor, newColor);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(selectedColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(MAX_WIDTH, MAX_HEIGHT);
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		listeners.add(Objects.requireNonNull(l));
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}
	
}
