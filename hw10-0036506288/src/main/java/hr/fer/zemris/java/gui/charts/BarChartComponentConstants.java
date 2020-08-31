package hr.fer.zemris.java.gui.charts;

import java.awt.Color;

/**
 * This class defines several <code>public static</code> constants
 * used in {@link BarChartComponent} class.
 * <p>
 * The constants define certain fixed distances in the bar chart component.
 * 
 * @author Ivan Skorupan
 */
public class BarChartComponentConstants {
	
	/**
	 * Default drawing color for the bar chart.
	 */
	public static final Color DEFAULT_COLOR = Color.BLACK;
	
	/**
	 * Distance between the y axis label's baseline and the left-most side of y axis values column.
	 */
	public static final int Y_LABEL_TO_Y_VALUE = 20;
	
	/**
	 * Distance between y axis values baseline and the y axis.
	 */
	public static final int Y_VALUE_TO_Y_AXIS = 10;
	
	/**
	 * Distance between the top of x axis label and the x axis values' baseline.
	 */
	public static final int X_LABEL_TO_X_VALUE = 20;
	
	/**
	 * Distance between the top-line of x axis values and the x axis.
	 */
	public static final int X_VALUE_TO_X_AXIS = 10;
	
	/**
	 * Space in between the top end of the component and top of the graph.
	 */
	public static final int TOP_MARGIN = 20;
	
	/**
	 * Space in between the right end of the component and the right-hand side of the graph.
	 */
	public static final int RIGHT_MARGIN = 20;
	
	/**
	 * Length of the small line that indicates a major value on the axis.
	 */
	public static final int VALUE_INDICATOR_LENGTH = 5;
	
	/**
	 * Length of the side of an axis arrow's triangle.
	 */
	public static final int ARROW_SIDE_LENGTH = 8;
	
}
