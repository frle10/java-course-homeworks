package hr.fer.zemris.math;

import static java.lang.Math.*;

/**
 * Models an immutable three component vector. All operations
 * on an instance of this class return a new instance that
 * represents the result of the applied operation.
 * <p>
 * The three components of this vector are its coordinates in
 * 3D space.
 * <p>
 * Textual representation of this vector is: (x, y, z).
 * 
 * @author Ivan Skorupan
 */
public class Vector3 {
	
	/**
	 * Coordinate x of this vector in 3D space;
	 */
	private double x;
	
	/**
	 * Coordinate y of this vector in 3D space;
	 */
	private double y;
	
	/**
	 * Coordinate z of this vector in 3D space;
	 */
	private double z;
	
	/**
	 * Constructs a new {@link Vector3} object initialized
	 * with its coordinates in 3D space.
	 * 
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Calculates and returns the norm of this vector (its length).
	 * 
	 * @return length of this vector
	 */
	public double norm() {
		return sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Returns a normalized vector (a vector whose length is 1) of
	 * this vector.
	 * 
	 * @return normalized vector of this vector
	 */
	public Vector3 normalized() {
		return this.scale(1 / this.norm());
	}
	
	/**
	 * Adds this vector with given vector <code>other</code>
	 * and returns the result as a new vector.
	 * 
	 * @param other - vector to add this vector with
	 * @return result of addition operation
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}
	
	/**
	 * Subtracts given vector <code>other</code> from this vector
	 * and returns the result as a new vector.
	 * 
	 * @param other - vector to subtract from this vector
	 * @return result of subtraction operation
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}
	
	/**
	 * Calculates and returns the dot product of this vector and
	 * given vector <code>other</code>.
	 * 
	 * @param other - vector to dot multiply this vector by
	 * @return dot product of this vector and vector <code>other</code>
	 */
	public double dot(Vector3 other) {
		return x * other.x + y * other.y + z * other.z;
	}
	
	/**
	 * Returns a vector representing a cross product of this vector
	 * and given vector <code>other</code>.
	 * 
	 * @param other - vector to cross multiply this vector with
	 * @return new vector as cross product of this vector and vector <code>other</code>
	 */
	public Vector3 cross(Vector3 other) {
		double crossX = y * other.z - z * other.y;
		double crossY = z * other.x - x * other.z;
		double crossZ = x * other.y - y * other.x;
		
		return new Vector3(crossX, crossY, crossZ);
	}
	
	/**
	 * Returns a new vector that represents this vector
	 * scaled by <code>s</code>.
	 * <p>
	 * The vector is scaled by multiplying each of its components
	 * with given <code>double</code> value <code>s</code>.
	 * 
	 * @param s - number to scale this vector by
	 * @return scaled vector
	 */
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}
	
	/**
	 * Returns the cosine of angle between this vector and
	 * given vector <code>other</code>.
	 * 
	 * @param other - vector relative to which to calculate the angle cosine
	 * @return cosine of angle between this vector and vector <code>other</code>
	 */
	public double cosAngle(Vector3 other) {
		return this.dot(other) / (this.norm() * other.norm());
	}

	/**
	 * Getter for x-coordinate of this vector.
	 * 
	 * @return this vector's x-coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Getter for y-coordinate of this vector.
	 * 
	 * @return this vector's y-coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Getter for z-coordinate of this vector.
	 * 
	 * @return this vector's z-coordinate
	 */
	public double getZ() {
		return z;
	}
	
	/**
	 * Converts this vector into an array with three <code>double</code>
	 * elements and returns it. The elements are this vector's coordinates
	 * x, y and z.
	 * 
	 * @return <code>double</code> array containing this vector's coordinates
	 */
	public double[] toArray() {
		double[] coordinates = new double[3];
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
		
		return coordinates;
	}

	@Override
	public String toString() {
		return "(" + String.format("%.6f", x) + ", " + String.format("%.6f", y) + ", "
				+ String.format("%.6f", z) + ")";
	}
	
}
