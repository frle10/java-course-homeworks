package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import static java.lang.Math.*;

import hr.fer.zemris.math.Vector2D;

/**
 * Models one turtle state.
 * <p>
 * Remembers current turtle position, direction,
 * drawing color and effective displacement length.
 * 
 * @author Ivan Skorupan
 */
public class TurtleState {
	
	/**
	 * Current turtle position vector.
	 */
	private Vector2D currentPosition;
	
	/**
	 * Current turtle direction vector.
	 */
	private Vector2D direction;
	
	/**
	 * Current turtle's drawing color.
	 */
	private Color drawingColor;
	
	/**
	 * Current effective displacement length.
	 */
	private double effectiveDisplacementLength;
	
	/**
	 * Constructs a new {@link TurtleState} object.
	 * 
	 * @param currentPosition - turtle's position vector
	 * @param direction - turtle's direction vector
	 * @param drawingColor - drawing color
	 * @param effectiveDisplacementLength - effective displacement length
	 */
	public TurtleState(Vector2D currentPosition, Vector2D direction, Color drawingColor, double effectiveDisplacementLength) {
		this.currentPosition = currentPosition;
		this.direction = direction;
		makeDirectionVectorUnitLength();
		this.drawingColor = drawingColor;
		this.effectiveDisplacementLength = effectiveDisplacementLength;
	}
	
	/**
	 * In case user gives a direction vector that isn't of unit length, this method
	 * will fix that.
	 */
	private void makeDirectionVectorUnitLength() {
		this.direction.scale(sqrt(direction.getX() * direction.getX() + direction.getY() * direction.getY()));
	}
	
	/**
	 * Returns a new instance of turtle state with fields equal to this turtle state,
	 * effectively a copy of this state.
	 * 
	 * @return copy (new instance) of this state
	 */
	public TurtleState copy() {
		return new TurtleState(currentPosition.copy(), direction.copy(), drawingColor, effectiveDisplacementLength);
	}

	public Vector2D getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Vector2D currentPosition) {
		this.currentPosition = currentPosition;
	}

	public Vector2D getDirection() {
		return direction;
	}

	public void setDirection(Vector2D direction) {
		this.direction = direction;
	}

	public Color getDrawingColor() {
		return drawingColor;
	}

	public void setDrawingColor(Color drawingColor) {
		this.drawingColor = drawingColor;
	}

	public double getEffectiveDisplacementLength() {
		return effectiveDisplacementLength;
	}

	public void setEffectiveDisplacementLength(double effectiveDisplacementLength) {
		this.effectiveDisplacementLength = effectiveDisplacementLength;
	}
	
}
