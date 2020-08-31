package hr.fer.zemris.java.hw17.jvdraw.geometry;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.gui.CircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.gui.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.GeometricalObjectVisitor;

/**
 * This class models a non-colored circle in 2D space
 * defined by its center {@link Point} and radius.
 * 
 * @author Ivan Skorupan
 */
public class Circle extends GeometricalObject {
	
	/**
	 * This circle's center point.
	 */
	private Point center;
	
	/**
	 * This circle's radius.
	 */
	private int radius;
	
	/**
	 * Constructs a new {@link Circle} object with initialized
	 * center point and radius.
	 * 
	 * @param center - center point of this circle
	 * @param radius - radius of this circle
	 * @throws NullPointerException if <code>center</code> is <code>null</code>
	 */
	public Circle(Point center, int radius, Color fgColor) {
		super(fgColor);
		this.center = Objects.requireNonNull(center);
		this.radius = radius;
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
	 * Setter for this circle's radius.
	 * 
	 * @param radius - circle radius
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		notifyListeners();
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}
	
	@Override
	public String toString() {
		return "Circle " + center + ", " + radius;
	}
	
}
