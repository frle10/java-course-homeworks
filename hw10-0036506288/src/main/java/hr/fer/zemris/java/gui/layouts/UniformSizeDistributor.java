package hr.fer.zemris.java.gui.layouts;

import static hr.fer.zemris.java.gui.layouts.CalcLayoutConstants.*;

import static java.lang.Math.*;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class models objects that know how to uniformly distribute
 * row and column sizes throughout the {@link CalcLayout} so that
 * all rows are of equal size and all columns are of equal size
 * with a maximum difference of 1 pixel if necessary (in case
 * width % numberOfRowsOrColumns != 0).
 * 
 * @author Ivan Skorupan
 */
public class UniformSizeDistributor {
	
	/**
	 * Parent container of {@link CalcLayout} that created this object.
	 */
	private Container parent;
	
	/**
	 * Border spacing in {@link CalcLayout} that created this object.
	 */
	private int borderSpacing;
	
	/**
	 * Holds sizes of each row, from the first up to the last.
	 */
	private List<Integer> rowSizes = new ArrayList<>();
	
	/**
	 * Holds sizes for each column, from the first up to the last.
	 */
	private List<Integer> columnSizes = new ArrayList<>();
	
	/**
	 * Constructs a new {@link UniformSizeDistributor} object.
	 * 
	 * @param parent - parent container of {@link CalcLayout}
	 * that called this constructor
	 * @param borderSpacing - border spacing in {@link CalcLayout} that
	 * called this constructor
	 * @throws NullPointerException if <code>parent</code> is <code>null</code>
	 */
	public UniformSizeDistributor(Container parent, int borderSpacing) {
		this.parent = Objects.requireNonNull(parent);
		this.borderSpacing = borderSpacing;
		distributeRowSizes();
	}
	
	/**
	 * Calculates what height the rows should be and what widths the columns should be
	 * based on the fact that all rows are the same size and all columns are the same size
	 * in {@link CalcLayout}.
	 * <p>
	 * Since the calculated size could be a double value and not necessary an integer, some
	 * rows will be one pixel taller than others and some columns one pixel wider than others.
	 * <p>
	 * We need to uniformly distribute those differences and that's what this method does.
	 */
	private void distributeRowSizes() {
		double rowHeight = (parent.getHeight() - (ROWS - 1) * borderSpacing) / (double) ROWS;
		double columnWidth = (parent.getWidth() - (COLUMNS - 1) * borderSpacing) / (double) COLUMNS;
		
		double mistake = 0;
		for(int i = 0; i < ROWS; i++) {
			int size = (int) round(rowHeight + mistake);
			mistake = rowHeight - size;
			
			rowSizes.add(size);
		}
		
		mistake = 0;
		for(int i = 0; i < COLUMNS; i++) {
			int size = (int) round(columnWidth + mistake);
			mistake = columnWidth - size;
			
			columnSizes.add(size);
		}
	}
	
	/**
	 * Calls the {@link Component#setBounds(int, int, int, int) setBounds()} method
	 * on the given <code>component</code> and takes its <code>position</code>
	 * in the {@link CalcLayout} layout manager.
	 * <p>
	 * The method calculates the correct coordinates, width and height of the given
	 * component based on earlier calculated width and column sizes for the parent
	 * container and then calls the mentioned method on the <code>component</code>.
	 * 
	 * @param component - component whose bounds should be set
	 * @param position - position of the <code>component</code> inside {@link CalcLayout}
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public void setBoundsForComponent(Component component, RCPosition position) {
		Objects.requireNonNull(component);
		Objects.requireNonNull(position);
		
		int x = 0;
		for(int i = 1; i < position.getColumn(); i++) {
			x += columnSizes.get(i - 1) + borderSpacing;
		}
		
		int y = 0;
		for(int i = 1; i < position.getRow(); i++) {
			y += rowSizes.get(i - 1) + borderSpacing;
		}
		
		int width = 0;
		if(position.equals(WIDE_COMPONENT_POSITION)) {
			for(int i = 0; i < WIDE_COMPONENT_COLUMN_WIDTH; i++) {
				width += columnSizes.get(i) + ((i == WIDE_COMPONENT_COLUMN_WIDTH - 1) ? 0 : borderSpacing);
			}
		} else {
			width = columnSizes.get(position.getColumn() - 1);
		}
		
		int height = rowSizes.get(position.getRow() - 1);
		component.setBounds(x, y, width, height);
	}
	
}
