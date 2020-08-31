package hr.fer.zemris.java.hw17.jvdraw.geometry;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.gui.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.gui.LineEditor;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.GeometricalObjectVisitor;

/**
 * This class models a line in 2D space defined by its
 * starting and ending {@link Point}.
 * 
 * @author Ivan Skorupan
 */
public class Line extends GeometricalObject {
	
	/**
	 * This line's start point.
	 */
	private Point startPoint;
	
	/**
	 * This line's end point.
	 */
	private Point endPoint;
	
	/**
	 * Constructs a new {@link Line} object with initialized
	 * starting and ending point.
	 * 
	 * @param startPoint - start point of this line
	 * @param endPoint - end point of this line
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public Line(Point startPoint, Point endPoint, Color fgColor) {
		super(fgColor);
		this.startPoint = Objects.requireNonNull(startPoint);
		this.endPoint = Objects.requireNonNull(endPoint);
	}
	
	/**
	 * Getter for this line's start point.
	 * 
	 * @return start point
	 */
	public Point getStartPoint() {
		return startPoint;
	}
	
	/**
	 * Setter for this line's start point.
	 * 
	 * @param startPoint - start point
	 */
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
		notifyListeners();
	}
	
	/**
	 * Getter for this line's end point.
	 * 
	 * @return end point
	 */
	public Point getEndPoint() {
		return endPoint;
	}
	
	/**
	 * Setter for this line's end point.
	 * 
	 * @param endPoint - new end point
	 */
	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
		notifyListeners();
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}
	
	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}
	
	@Override
	public String toString() {
		return "Line " + startPoint + "-" + endPoint;
	}
	
}
