package coloring.algorithms;

import java.util.Objects;

/**
 * Models a pixel on a digital screen.
 * 
 * @author Ivan Skorupan
 */
public class Pixel {
	
	/**
	 * Coordinate x of this pixel.
	 */
	public int x;
	
	/**
	 * Coordinate y of this pixel.
	 */
	public int y;

	/**
	 * COnstructs a new {@link Pixel} object.
	 * 
	 * @param x - c coordinate
	 * @param y - y coordinate
	 */
	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Pixel))
			return false;
		Pixel other = (Pixel) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
}
