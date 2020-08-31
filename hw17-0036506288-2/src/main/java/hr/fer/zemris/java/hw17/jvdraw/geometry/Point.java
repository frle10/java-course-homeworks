package hr.fer.zemris.java.hw17.jvdraw.geometry;

import static java.lang.Math.*;

/**
 * This class models a point in 2D space with integer
 * coordinates.
 * 
 * @author Ivan Skorupan
 */
public class Point {
	
	/**
	 * Coordinate x of this point.
	 */
	private int x;
	
	/**
	 * Coordinate y of this point.
	 */
	private int y;

	/**
	 * Constructs a new {@link Point} object with initialized
	 * coordinates.
	 * 
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static int distance(Point start, Point end) {
		return (int) round(sqrt(pow(start.x - end.x, 2.) + pow(start.y - end.y, 2)));
	}
	
	/**
	 * Getter for this point's x coordinate.
	 * 
	 * @return x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter for this point's y coordinate.
	 * 
	 * @return y coordinate
	 */
	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
}
