package hr.fer.zemris.java.hw17.jvdraw.geometry;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.gui.FilledCircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.gui.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.GeometricalObjectVisitor;

/**
 * This class models a color filled circle in 2D space
 * defined by its center {@link Point}, radius and
 * fill color.
 * 
 * @author Ivan Skorupan
 */
public class FilledCircle extends GeometricalObject {
	
	/**
	 * This circle's center point.
	 */
	private Point center;
	
	/**
	 * This circle's radius.
	 */
	private int radius;
	
	/**
	 * This circle's fill color.
	 */
	private Color fillColor;
	
	/**
	 * Constructs a new {@link FilledCircle} object with initialized
	 * center point, radius and fill color.
	 * 
	 * @param center - center point of this filled circle
	 * @param radius - radius of this filled circle
	 * @param fillColor - fill color of this filled circle
	 * @throws NullPointerException if <code>center</code> or <code>fillColor</code> is <code>null</code>
	 */
	public FilledCircle(Point center, int radius, Color fgColor, Color fillColor) {
		super(fgColor);
		this.center = Objects.requireNonNull(center);
		this.radius = radius;
		this.fillColor = Objects.requireNonNull(fillColor);
	}
	
	/**
	 * Getter for this circle's center point.
	 * 
	 * @return center point
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Setter for this circle's center point.
	 * 
	 * @param center - center point
	 */
	public void setCenter(Point center) {
		this.center = center;
		notifyListeners();
	}
	
	/**
	 * Getter for this circle's radius.
	 * 
	 * @return radius
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * Setter for this filled circle's radius.
	 * 
	 * @param radius - circle radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		notifyListeners();
	}

	/**
	 * Getter for this filled circle's fill color.
	 * 
	 * @return fill color
	 */
	public Color getFillColor() {
		return fillColor;
	}
	
	/**
	 * Setter for this filled circle's fill color.
	 * 
	 * @param fillColor - fill color
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		notifyListeners();
	}
	
	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}
	
	@Override
	public String toString() {
		String hexFillColor = String.format("#%02x%02x%02x", fillColor.getRed(),
				fillColor.getGreen(), fillColor.getBlue()).toUpperCase();
		return "Filled circle " + center + ", " + radius + ", " + hexFillColor;
	}
	
}
