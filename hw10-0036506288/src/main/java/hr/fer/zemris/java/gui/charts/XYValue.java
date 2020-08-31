package hr.fer.zemris.java.gui.charts;

/**
 * Models a point on the chart defined by its (x, y) pair.
 * 
 * @author Ivan Skorupan
 */
public class XYValue {
	
	/**
	 * Point's value on the x axis.
	 */
	private int x;
	
	/**
	 * Points value on the y axis.
	 */
	private int y;

	/**
	 * Constructs a new {@link XYValue} object.
	 * 
	 * @param x - x axis value
	 * @param y - y axis value
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Getter for x axis value.
	 * 
	 * @return x axis value
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Getter for y axis value.
	 * 
	 * @return y axis value
	 */
	public int getY() {
		return y;
	}
	
}
