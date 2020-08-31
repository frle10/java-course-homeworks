package hr.fer.zemris.math;

import static java.lang.Math.*;

import java.util.Objects;

/**
 * Models a 2D vector whose components are real numbers <code>x</code>
 * and <code>y</code>.
 * 
 * @author Ivan Skorupan
 */
public class Vector2D {
	/**
	 * The x component of this vector.
	 */
	private double x;

	/**
	 * The y component of this vector.
	 */
	private double y;

	/**
	 * Constructs a new {@link Vector2D} object with given
	 * components.
	 * 
	 * @param x - x component
	 * @param y - y component
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the <code>x</code> component of this vector.
	 * 
	 * @return this vector's <code>x</code> component
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the <code>y</code> component of this vector.
	 * 
	 * @return this vector's <code>y</code> component
	 */
	public double getY() {
		return y;
	}

	/**
	 * Does a translation transformation on this vector using another
	 * given vector.
	 * <p>
	 * This method modifies the vector instance upon which it was called
	 * and does not return anything.
	 * 
	 * @param offset - the translation vector
	 */
	public void translate(Vector2D offset) {
		Objects.requireNonNull(offset);
		x = x + offset.x;
		y = y + offset.y;
	}

	/**
	 * Does a translation transformation on this vector using another
	 * given vector.
	 * <p>
	 * This method does not modify the one upon which it was called but
	 * returns a new instance of the modified vector.
	 * 
	 * @param offset - the translation vector
	 * @return a new instance of the modified vector
	 */
	public Vector2D translated(Vector2D offset) {
		Objects.requireNonNull(offset);
		return new Vector2D(x + offset.x, y + offset.y);
	}

	/**
	 * Does a rotation transformation on this vector using the given angle.
	 * <p>
	 * This method modifies the vector instance upon which it was called
	 * and does not return anything.
	 * 
	 * @param angle - the angle by which to rotate the vector
	 */
	public void rotate(double angle) {
		double x1 = x;
		double y1 = y;

		x = cos(angle) * x1 - sin(angle) * y1;
		y = sin(angle) * x1 + cos(angle) * y1;
	}

	/**
	 * Does a rotation transformation on this vector using the given angle.
	 * <p>
	 * This method does not modify the vector instance upon which it
	 * was called, but rather returns a new instance of the modified vector.
	 * 
	 * @param angle - the angle by which to rotate the vector
	 * @return a new instance of the rotated vector
	 */
	public Vector2D rotated(double angle) {
		Vector2D rotatedVector = new Vector2D(x, y);
		rotatedVector.rotate(angle);

		return rotatedVector;
	}

	/**
	 * Does a scaling transformation on this vector using the given
	 * scaling factor.
	 * <p>
	 * This method modifies the vector instance upon which it was called
	 * and does not return anything.
	 * 
	 * @param scaler - the scaling factor
	 */
	public void scale(double scaler) {
		x *= scaler;
		y *= scaler;
	}

	/**
	 * Does a scaling transformation on this vector using the given
	 * scaling factor.
	 * <p>
	 * This method does not modify the vector instance upon which it was called,
	 * but rather returns a new instance of the modified vector.
	 * 
	 * @param scaler - the scaling factor
	 * @return a new instance of the scaled vector
	 */
	public Vector2D scaled(double scaler) {
		return new Vector2D(x * scaler, y * scaler);
	}
	
	/**
	 * Returns a new instance of {@link Vector2D} with components equal
	 * to this vector.
	 * 
	 * @return a new {@link Vector2D} instance with components equal to this vector
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
}
