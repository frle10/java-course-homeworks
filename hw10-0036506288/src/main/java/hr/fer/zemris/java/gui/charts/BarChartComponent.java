package hr.fer.zemris.java.gui.charts;

import static hr.fer.zemris.java.gui.charts.BarChartComponentConstants.*;

import static java.lang.Math.*;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.JComponent;

/**
 * This class models a component that draws (visualizes) the data
 * defined with the passed {@link BarChart} object.
 * <p>
 * The component draws a typical bar chart.
 * 
 * @author Ivan Skorupan
 */
public class BarChartComponent extends JComponent {

	private static final long serialVersionUID = -2290688286165650352L;

	/**
	 * A reference to a {@link BarChart} model this component
	 * is trying to visualize.
	 */
	private BarChart barChart;
	
	/**
	 * List of all x values in this graph (is expected to be sorted in ascending order).
	 */
	private List<Integer> xValues = new LinkedList<>();

	/**
	 * Constructs a new {@link BarChartComponent} object.
	 * 
	 * @param barChart - {@link BarChart} model this component is visualizing
	 * @throws NullPointerException if <code>barChart</code> is <code>null</code>
	 */
	public BarChartComponent(BarChart barChart) {
		this.barChart = Objects.requireNonNull(barChart);
		
		List<XYValue> xyValues = barChart.getXyValues();
		for(XYValue xyValue : xyValues) {
			xValues.add(xyValue.getX());
		}
		xValues.sort(Integer::compare);
	}

	@Override
	public void paint(Graphics g) {
		Objects.requireNonNull(g);
		Graphics2D g2d = (Graphics2D) g;
		FontMetrics fm = g.getFontMetrics();
		
		// compute necessary data
		List<XYValue> xyValues = barChart.getXyValues();
		xyValues.sort((xy1, xy2) -> Integer.compare(xy1.getX(), xy2.getX()));
		
		int yValuesWidth = fm.stringWidth(String.valueOf(barChart.getMaxY()));
		int originX = fm.getHeight() + Y_LABEL_TO_Y_VALUE + yValuesWidth + Y_VALUE_TO_Y_AXIS;
		int originY = getHeight() - fm.getHeight() - X_LABEL_TO_X_VALUE - fm.getHeight() - X_VALUE_TO_X_AXIS;
		
		int xAxisLength = getWidth() - RIGHT_MARGIN - originX;
		int yAxisLength = originY - TOP_MARGIN;
		
		int numberOfValuesOnXAxis = xValues.size();
		int numberOfValuesOnYAxis = (barChart.getMaxY() - barChart.getMinY()) / barChart.getDiffY() + 1;
		
		int xAxisValueBaselineY = originY + X_VALUE_TO_X_AXIS + fm.getAscent();
		
		double xAxisUnitLength = (double) xAxisLength / numberOfValuesOnXAxis;
		double yAxisUnitLength = (double) yAxisLength / (numberOfValuesOnYAxis - 1);
		
		// draw the bar chart
		drawAxes(g2d, originX, originY);
		drawAxisArrows(g2d, originX, originY);
		drawYAxisValues(g2d, fm, originX, originY, numberOfValuesOnYAxis, yAxisUnitLength);
		drawXAxisValues(g2d, fm, originX, originY, numberOfValuesOnXAxis, xAxisUnitLength, xAxisValueBaselineY);
		drawLabels(g2d, fm, originX, originY, xAxisLength, yAxisLength);
		plotData(g2d, xyValues, Color.RED, originX, originY, xAxisUnitLength, yAxisUnitLength, numberOfValuesOnXAxis);
		g2d.dispose();
	}
	
	/**
	 * Plots the data from <code>xyValues</code> list on the graph
	 * by drawing colored bars whose color is given by <code>color</code>.
	 * <p>
	 * At the end of plotting, the color of the graphics object <code>g2d</code>
	 * is set back to {@link BarChartComponentConstants#DEFAULT_COLOR DEFAULT_COLOR}.
	 * 
	 * @param g2d - graphics object to use for drawing
	 * @param xyValues - list of data points on the graph
	 * @param color - color of the bars
	 * @param originX - x coordinate of chart origin point
	 * @param originY - y coordinate of chart origin point
	 * @param xAxisUnitLength - unit length on x axis
	 * @param yAxisUnitLength - unit length on y axis
	 * @param numberOfValuesOnXAxis - number of values under the x axis
	 */
	private void plotData(Graphics2D g2d, List<XYValue> xyValues, Color color, int originX, int originY, double xAxisUnitLength, double yAxisUnitLength, int numberOfValuesOnXAxis) {
		g2d.setColor(color);
		int x = originX + 1;
		for(int i = 0; i < numberOfValuesOnXAxis; i++) {
			double height = (xyValues.get(i).getY() - barChart.getMinY()) / (double) barChart.getDiffY() * yAxisUnitLength;
			int nextX = 1 + originX + (int) ((i + 1) * xAxisUnitLength);
			g2d.fillRect(x, originY - (int) height, nextX - x - 1, (int) height);
			x += nextX - x;
		}
		g2d.setColor(DEFAULT_COLOR);
	}
	
	/**
	 * Draws the values under the x axis on the graph.
	 * 
	 * @param g2d - graphics object to use for drawing
	 * @param fm - font metrics object to use for obtaining font information
	 * @param originX - x coordinate of chart origin point
	 * @param originY - y coordinate of chart origin point
	 * @param numberOfValuesOnXAxis - number of values under the x axis
	 * @param xAxisUnitLength - unit length on x axis
	 * @param xAxisValueBaselineY - y coordinate of x axis values baseline
	 */
	private void drawXAxisValues(Graphics2D g2d, FontMetrics fm, int originX, int originY, int numberOfValuesOnXAxis, double xAxisUnitLength, int xAxisValueBaselineY) {
		for(int i = 0; i < numberOfValuesOnXAxis; i++) {
			int valueX = originX + (int) (i * xAxisUnitLength);
			int x = valueX + (int) (xAxisUnitLength / 2) - fm.stringWidth(xValues.get(i).toString()) / 2;
			
			g2d.drawLine(valueX, originY, valueX, originY + VALUE_INDICATOR_LENGTH);
			g2d.drawString(xValues.get(i).toString(), x, xAxisValueBaselineY);
		}
		g2d.drawLine(getWidth() - RIGHT_MARGIN, originY, getWidth() - RIGHT_MARGIN, originY + VALUE_INDICATOR_LENGTH);
	}
	
	/**
	 * Draws the values on the left of the y axis on the graph.
	 * 
	 * @param g2d - graphics object to use for drawing
	 * @param fm - font metrics object to use for obtaining font information
	 * @param originX - x coordinate of chart origin point
	 * @param originY - y coordinate of chart origin point
	 * @param numberOfValuesOnYAxis - number of values under the y axis
	 * @param yAxisUnitLength - unit length on y axis
	 */
	private void drawYAxisValues(Graphics2D g2d, FontMetrics fm, int originX, int originY, int numberOfValuesOnYAxis, double yAxisUnitLength) {
		for(int i = 0, num = barChart.getMinY(); i < numberOfValuesOnYAxis; i++) {
			int valueBaselineY = originY - (int) (i * yAxisUnitLength);
			int x = originX - Y_VALUE_TO_Y_AXIS - fm.stringWidth(String.valueOf(num));
			int y = valueBaselineY + fm.getAscent() / 3;
			
			g2d.drawLine(originX, valueBaselineY, originX - VALUE_INDICATOR_LENGTH, valueBaselineY);
			g2d.drawString(String.valueOf(num), x, y);
			
			num += barChart.getDiffY();
		}
	}
	
	/**
	 * Draws the axes on the graph.
	 * 
	 * @param g2d - graphics object to use for drawing
	 * @param originX - x coordinate of chart origin point
	 * @param originY - y coordinate of chart origin point
	 */
	private void drawAxes(Graphics2D g2d, int originX, int originY) {
		g2d.drawLine(originX, originY, originX, TOP_MARGIN);
		g2d.drawLine(originX, originY, getWidth() - RIGHT_MARGIN, originY);
	}
	
	/**
	 * Draws the axis arrows on both the x and y axis.
	 * 
	 * @param g2d - graphics object to use for drawing
	 * @param originX - x coordinate of chart origin point
	 * @param originY - y coordinate of chart origin point
	 */
	private void drawAxisArrows(Graphics2D g2d, int originX, int originY) {
		drawXAxisArrow(g2d, getWidth() - RIGHT_MARGIN, originY, VALUE_INDICATOR_LENGTH, ARROW_SIDE_LENGTH);
		drawYAxisArrow(g2d, originX, TOP_MARGIN, VALUE_INDICATOR_LENGTH, ARROW_SIDE_LENGTH);
	}
	
	/**
	 * Draws an arrow on the far right of the x axis.
	 * <p>
	 * The arrow itself consists of two parts: the tail and the triangle.
	 * <p>
	 * The tail is drawn as a simple line, while the triangle is drawn as
	 * a filled polygon.
	 * 
	 * @param g2d - graphics object to use for drawing
	 * @param x - x coordinate of beginning of arrow tail
	 * @param y - y coordinate of the beginning of arrow tail
	 * @param tailLength - length of arrow tail
	 * @param side - length of side of the arrow triangle
	 */
	private void drawXAxisArrow(Graphics2D g2d, int x, int y, int tailLength, int side) {
		g2d.drawLine(x, y, x + tailLength, y);
		int[] xCoords = new int[] {x + tailLength, x + tailLength, x + tailLength + (int) (side * sqrt(3) / 2)};
		int[] yCoords = new int[] {y + side / 2, y - side / 2, y};
		
		Polygon p = new Polygon(xCoords, yCoords, xCoords.length);
		g2d.fillPolygon(p);
	}
	
	/**
	 * Draws an arrow on the very top of the y axis.
	 * <p>
	 * The arrow itself consists of two parts: the tail and the triangle.
	 * <p>
	 * The tail is drawn as a simple line, while the triangle is drawn as
	 * a filled polygon.
	 * 
	 * @param g2d - graphics object to use for drawing
	 * @param x - x coordinate of beginning of arrow tail
	 * @param y - y coordinate of the beginning of arrow tail
	 * @param tailLength - length of arrow tail
	 * @param side - length of side of the arrow triangle
	 */
	private void drawYAxisArrow(Graphics2D g2d, int x, int y, int tailLength, int side) {
		g2d.drawLine(x, y, x, y - tailLength);
		int[] xCoords = new int[] {x - side / 2, x + side / 2, x};
		int[] yCoords = new int[] {y - tailLength, y - tailLength, y - tailLength - (int) (side * sqrt(3) / 2)};
		
		Polygon p = new Polygon(xCoords, yCoords, xCoords.length);
		g2d.fillPolygon(p);
	}

	/**
	 * Draws x and y axes labels on the graph.
	 * 
	 * @param g2d - graphics object to use for drawing
	 * @param fm - font metrics object to use for obtaining font information
	 * @param originX - x coordinate of chart origin point
	 * @param originY - y coordinate of chart origin point
	 * @param xAxisLength - length of the x axis
	 * @param yAxisLength - length of the y axis
	 */
	private void drawLabels(Graphics2D g2d, FontMetrics fm, int originX, int originY, int xAxisLength, int yAxisLength) {
		String xLabel = barChart.getxLabel();
		g2d.drawString(xLabel, originX + xAxisLength / 2 - fm.stringWidth(xLabel) / 2, getHeight() - fm.getHeight());

		AffineTransform defaultAt = g2d.getTransform();
		g2d.rotate(-Math.PI / 2);

		String yLabel = barChart.getyLabel();
		g2d.drawString(yLabel, -originY + yAxisLength / 2 - fm.stringWidth(yLabel) / 2, fm.getHeight());
		g2d.setTransform(defaultAt);
	}

	/**
	 * Getter for this component's {@link BarChart} reference.
	 * 
	 * @return {@link BarChart} model this component is visualizing
	 */
	public BarChart getBarChart() {
		return barChart;
	}

}
