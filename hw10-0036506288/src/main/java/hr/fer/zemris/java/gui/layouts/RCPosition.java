package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

/**
 * Models a constraint for {@link CalcLayout} layout manager
 * which holds the information about the row and column a
 * component should be added to.
 * 
 * @author Ivan Skorupan
 */
public class RCPosition {
	
	/**
	 * Desired row for the component to be added to.
	 */
	private int row;
	
	/**
	 * Desired column for the component to be added to.
	 */
	private int column;
	
	/**
	 * Constructs a new {@link RCPosition} object and
	 * sets the given <code>row</code> and <code>column</code>
	 * fields. 
	 * 
	 * @param row - row position for the component
	 * @param column - column position for the component
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Getter for {@link #row} field.
	 * 
	 * @return row position
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Getter for {@link #column} field.
	 * 
	 * @return column position
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RCPosition))
			return false;
		RCPosition other = (RCPosition) obj;
		return column == other.column && row == other.row;
	}
	
}
