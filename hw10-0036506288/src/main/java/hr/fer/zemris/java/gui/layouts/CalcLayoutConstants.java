package hr.fer.zemris.java.gui.layouts;

/**
 * This class contains <code>public static</code> constants used throughout
 * classes that implement the {@link CalcLayout} layout manager.
 * 
 * @author Ivan Skorupan
 */
public class CalcLayoutConstants {
	
	/**
	 * Lower bound index for forbidden columns in the first row
	 * of {@link CalcLayout} grid. This is a specific property of
	 * {@link CalcLayout} layout manager.
	 */
	public static final int FORBIDDEN_COLUMN_LOWER_BOUND = 2;
	
	/**
	 * Higher bound index for forbidden columns in the first row
	 * of {@link CalcLayout} grid. This is a specific property of
	 * {@link CalcLayout} layout manager.
	 */
	public static final int FORBIDDEN_COLUMN_HIGHER_BOUND = 5;

	/**
	 * Number of rows in {@link CalcLayout} layout manager's grid.
	 */
	public static final int ROWS = 5;

	/**
	 * Number of columns in {@link CalcLayout} layout manager's grid.
	 */
	public static final int COLUMNS = 7;
	
	/**
	 * Number of columns taken by the wide component.
	 */
	public static final int WIDE_COMPONENT_COLUMN_WIDTH = 5;
	
	/**
	 * Position of the wide component in {@link CalcLayout} (component in first row and first column).
	 */
	public static final RCPosition WIDE_COMPONENT_POSITION = new RCPosition(1, 1);
	
}
