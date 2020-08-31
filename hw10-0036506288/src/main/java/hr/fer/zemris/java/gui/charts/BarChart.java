package hr.fer.zemris.java.gui.charts;

import java.util.List;
import java.util.Objects;

/**
 * This class models a bar chart.
 * <p>
 * A typical bar chart has labels next to its x and y axes,
 * minimum and maximum y axis values, difference between
 * two neighboring y axis values and a list of points (xy-values)
 * that belong to the chart.
 * 
 * @author Ivan Skorupan
 */
public class BarChart {
	
	/**
	 * List of points that belong to this bar chart.
	 */
	private List<XYValue> xyValues;
	
	/**
	 * Label on the x axis.
	 */
	private String xLabel;
	
	/**
	 * Label on the y axis.
	 */
	private String yLabel;
	
	/**
	 * Minimum y axis value.
	 */
	private int minY;
	
	/**
	 * Maximum y axis value.
	 */
	private int maxY;
	
	/**
	 * Difference between two neighboring y axis values.
	 */
	private int diffY;
	
	/**
	 * Constructs a new {@link BarChart} object.
	 * 
	 * @param xyValues - list of points that belong to this bar chart
	 * @param xLabel - label on the x axis
	 * @param yLabel - label on the y axis
	 * @param minY - minimum y axis value
	 * @param maxY - maximum y axis value
	 * @param diffY - difference between two neighboring y axis values
	 * @throws NullPointerException if <code>xyValues</code> or <code>xLabel</code>
	 * or <code>yLabel</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>minY</code> is negative or greater than or equal to
	 * <code>maxY</code> or if any point in <code>xyValues</code> contains a (x, y) pair where y is
	 * less than <code>minY</code>
	 */
	public BarChart(List<XYValue> xyValues, String xLabel, String yLabel, int minY, int maxY, int diffY) {
		if(minY < 0 || maxY <= minY) {
			throw new IllegalArgumentException("Minimum y value must be non-negative and smaller than maximum y value of the chart!");
		}
		
		this.xyValues = Objects.requireNonNull(xyValues);
		
		for(XYValue xyValue : xyValues) {
			if(xyValue.getY() < minY) {
				throw new IllegalArgumentException("All xy-values in the list must have their y value greater than"
						+ " or equal to the minimum y value of the chart!");
			}
		}
		
		if((maxY - minY) % diffY != 0) {
			this.maxY = maxY + (diffY - (maxY - minY) % diffY);
		} else {
			this.maxY = maxY;
		}
		
		this.xLabel = Objects.requireNonNull(xLabel);
		this.yLabel = Objects.requireNonNull(yLabel);
		this.minY = minY;
		this.diffY = diffY;
	}
	
	/**
	 * Getter for this bar chart's points.
	 * 
	 * @return list of this bar chart's points
	 */
	public List<XYValue> getXyValues() {
		return xyValues;
	}
	
	/**
	 * Getter for x axis label.
	 * 
	 * @return x axis label
	 */
	public String getxLabel() {
		return xLabel;
	}
	
	/**
	 * Getter for y axis label.
	 * 
	 * @return y axis label
	 */
	public String getyLabel() {
		return yLabel;
	}
	
	/**
	 * Getter for minimum y axis value.
	 * 
	 * @return minimum y axis value
	 */
	public int getMinY() {
		return minY;
	}
	
	/**
	 * Getter for maximum y axis value.
	 * 
	 * @return maximum y axis value
	 */
	public int getMaxY() {
		return maxY;
	}
	
	/**
	 * Getter for difference between two neighboring
	 * y axis values
	 * 
	 * @return difference between two neighboring y axis values
	 */
	public int getDiffY() {
		return diffY;
	}
	
}
