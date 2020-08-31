package hr.fer.zemris.java.gui.layouts;

import static hr.fer.zemris.java.gui.layouts.CalcLayoutConstants.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * This is a custom {@link LayoutManager2} implementation which we
 * will be using to implement the GUI for our calculator described
 * in problem 2 of the 10th homework.
 * 
 * @author Ivan Skorupan
 */
public class CalcLayout implements LayoutManager2 {

	/**
	 * Distance in pixels between two rows or two columns.
	 */
	private int borderSpacing;
	
	/**
	 * Internal map that maps each position in this layout's grid with its component
	 * if there is one.
	 */
	private Map<RCPosition, Component> layoutMap = new HashMap<>();

	/**
	 * Constructs a new {@link CalcLayout} object and
	 * sets {@link #borderSpacing} to 0;
	 */
	public CalcLayout() {
		this(0);
	}

	/**
	 * Constructs a new {@link CalcLayout} object and
	 * sets the given <code>borderSpacing</code>.
	 * 
	 * @param borderSpacing - border spacing (distance between two rows or columns)
	 */
	public CalcLayout(int borderSpacing) {
		if(borderSpacing < 0) {
			throw new IllegalArgumentException("Border spacing cannot be negative!");
		}
		this.borderSpacing = borderSpacing;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException("This operation is not supported!");
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		Objects.requireNonNull(comp);
		layoutMap.keySet().removeIf((rc) -> comp.equals(layoutMap.get(rc)));
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Objects.requireNonNull(parent);
		return calcLayoutSize(parent, c -> c.getPreferredSize(), (i, j) -> i > j);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Objects.requireNonNull(parent);
		return calcLayoutSize(parent, c -> c.getMinimumSize(), (i, j) -> i > j);
	}
	
	@Override
	public Dimension maximumLayoutSize(Container target) {
		Objects.requireNonNull(target);
		return calcLayoutSize(target, c -> c.getMaximumSize(), (i, j) -> i < j);
	}
	
	/**
	 * Helper method called by {@link #preferredLayoutSize(Container)}, {@link #minimumLayoutSize(Container)}
	 * and {@link #maximumLayoutSize(Container)} methods.
	 * <p>
	 * It uses the Strategy Design Pattern in order to deal with code duplication removal, since the three
	 * above mentioned methods do very similar work.
	 * <p>
	 * The method calculates the layout size by running through an array of components that are contained
	 * by <code>parent</code> and using the given {@link Function} <code>f</code> and {@link BiPredicate}
	 * <code>p</code> in order to determine the best dimensions.
	 * 
	 * @param parent - parent container that uses this layout manager
	 * @param f - {@link Function} that returns the desired component's dimension type (minimum, maximum, preferred)
	 * @param p - {@link BiPredicate} that tests two component's dimensions using a user given implementation
	 * @return the best {@link Dimension} for this layout given described parameters and strategies
	 */
	private Dimension calcLayoutSize(Container parent, Function<Component, Dimension> f, BiPredicate<Integer, Integer> p) {
		Component[] components = parent.getComponents();
		Dimension compDim = f.apply(components[0]);
		int width = isWideComponent(components[0]) ? getColumnWidthFromWideComponent(compDim.width) : compDim.width;
		int height = compDim.height;
		
		for(int i = 1; i < components.length; i++) {
			compDim = f.apply(components[i]);
			if(compDim == null) continue;
			int compWidth = compDim.width;
			int compHeight = compDim.height;
			
			compWidth = isWideComponent(components[i]) ? getColumnWidthFromWideComponent(compDim.width) : compWidth;
			
			if(p.test(compWidth, width)) {
				width = compWidth;
			}
			
			if(p.test(compHeight, height)) {
				height = compHeight;
			}
		}
		
		width = width * COLUMNS + borderSpacing * (COLUMNS - 1);
		height = height * ROWS + borderSpacing * (ROWS - 1);
		
		return new Dimension(width, height);
	}
	
	/**
	 * Extracts the width of a single column given the width of the wide component.
	 * <p>
	 * The wide component is the one on position <b>(1, 1)</b>.
	 * 
	 * @param wideCompWidth - width of the wide component
	 * @return single column width
	 */
	private int getColumnWidthFromWideComponent(int wideCompWidth) {
		return (wideCompWidth - (WIDE_COMPONENT_COLUMN_WIDTH - 1) * borderSpacing) / WIDE_COMPONENT_COLUMN_WIDTH;
	}
	
	/**
	 * Tests if the given component is on position <b>(1, 1)</b>
	 * which would make it the wide component.
	 * 
	 * @param component - component to be tested
	 * @return <code>true</code> if <code>component</code> is wide, <code>false</code> otherwise
	 */
	private boolean isWideComponent(Component component) {
		if(layoutMap.get(WIDE_COMPONENT_POSITION) == component) {
			return true;
		}
		
		return false;
	}

	@Override
	public void layoutContainer(Container parent) {
		Objects.requireNonNull(parent);
		UniformSizeDistributor distributor = new UniformSizeDistributor(parent, borderSpacing);
		Component[] components = parent.getComponents();
		
		for(Component component : components) {
			RCPosition position = findPosition(component);
			distributor.setBoundsForComponent(component, position);
		}
	}
	
	/**
	 * Finds the position of given component, but only if
	 * the component is present in this layout manager,
	 * otherwise <code>null</code> is returned.
	 * 
	 * @param component - component whose position should be found
	 * @return position of given <code>component</code> or <code>null</code> if the component isn't
	 * in this layout manager's internal map
	 */
	private RCPosition findPosition(Component component) {
		for(Entry<RCPosition, Component> entry : layoutMap.entrySet()) {
			if(component == entry.getValue()) {
				return entry.getKey();
			}
		}
		
		return null;
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		Objects.requireNonNull(comp);
		Objects.requireNonNull(constraints);

		RCPosition position = null;
		if(constraints instanceof RCPosition) {
			position = (RCPosition) constraints;
		} else if(constraints instanceof String) {
			String posText = (String) constraints;
			position = parsePosition(posText);
		} else {
			throw new UnsupportedOperationException("The constraints should be of type RCPosition or String!");
		}
		
		if(isValidPosition(position)) {
			if(layoutMap.get(position) == null) {
				layoutMap.put(position, comp);
			} else {
				throw new CalcLayoutException("The given position is already taken by another component!");
			}
		} else {
			throw new CalcLayoutException("The given position for the component is invalid!");
		}
	}
	
	/**
	 * Parses a {@link RCPosition} object from the given string <code>posText</code>.
	 * <p>
	 * An exception is thrown in case the string is invalid (contains wrong number of
	 * tokens or contains tokens that aren't integers).
	 * 
	 * @param posText - string to be parsed for a valid {@link RCPosition} object
	 * @return parsed {@link RCPosition} object
	 * @throws CalcLayoutException if <code>posTest<code> cannot be parsed into a vali
	 * {@link RCPosition} object
	 */
	private RCPosition parsePosition(String posText) {
		String[] tokens = posText.trim().split(",");
		if(tokens.length != 2) {
			throw new CalcLayoutException("The position string is invalid!");
		}
		
		int row = 0, column = 0;
		try {
			row = Integer.parseInt(tokens[0]);
			column = Integer.parseInt(tokens[1]);
		} catch(NumberFormatException ex) {
			throw new CalcLayoutException("The position string is invalid!");
		}
		
		return new RCPosition(row, column);
	}
	
	/**
	 * Tests if the given position is valid.
	 * <p>
	 * A position is valid if it is inside the grid dimensions,
	 * so the row and column fields cannot be lower than 1 or
	 * higher than the total number of rows or columns.
	 * <p>
	 * Also, the position is invalid if the row field is equal to 1
	 * and column is anywhere between 2 and 5 (this is specific to
	 * this layout manager's design).
	 * 
	 * @param position - position to test
	 * @return <code>true</code> if <code>position</code> is valid, <code>false</code> otherwise
	 */
	private boolean isValidPosition(RCPosition position) {
		int row = position.getRow();
		int column = position.getColumn();
		
		if(row == 1 && column >= FORBIDDEN_COLUMN_LOWER_BOUND && column <= FORBIDDEN_COLUMN_HIGHER_BOUND) {
			return false;
		} else if(row < 1 || column < 1 || row > ROWS || column > COLUMNS) {
			return false;
		}
		
		return true;
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {

	}

}
