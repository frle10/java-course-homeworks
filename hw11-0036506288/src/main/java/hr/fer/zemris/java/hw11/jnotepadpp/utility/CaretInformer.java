package hr.fer.zemris.java.hw11.jnotepadpp.utility;

/**
 * This is a helper class that models and object that
 * holds current information about the caret.
 * <p>
 * The actual information is calculated outside of this class,
 * objects of this type just store the information so it can
 * be returned as one reference from some method.
 * 
 * @author Ivan Skorupan
 */
public class CaretInformer {
	
	/**
	 * Line in which the caret is right now.
	 */
	private Integer line;
	
	/**
	 * Column in which the caret is right now.
	 */
	private Integer column;
	
	/**
	 * Current selection length.
	 */
	private Integer selectionLength;

	/**
	 * Constructs a new {@link CaretInformer} object.
	 * 
	 * @param line - current caret line
	 * @param column - current caret column
	 * @param selectionLength - current selection length
	 */
	public CaretInformer(Integer line, Integer column, Integer selectionLength) {
		this.line = line;
		this.column = column;
		this.selectionLength = selectionLength;
	}

	/**
	 * Getter for current caret line.
	 * 
	 * @return caret line
	 */
	public Integer getLine() {
		return line;
	}

	/**
	 * Getter for current caret column.
	 * 
	 * @return caret column
	 */
	public Integer getColumn() {
		return column;
	}

	/**
	 * Getter for current selection length.
	 * 
	 * @return selection length
	 */
	public Integer getSelectionLength() {
		return selectionLength;
	}
	
}
