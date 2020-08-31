package hr.fer.zemris.java.hw17.jvdraw.interfaces;

import java.awt.Color;

/**
 * Models objects that can provide color information.
 * <p>
 * These objects must provide listener managing methods because they
 * are interested in color changes in this color provider.
 * 
 * @author Ivan Skorupan
 */
public interface IColorProvider {

	/**
	 * Returns the current selected color in this color provider.
	 * 
	 * @return this provider's current selected color
	 */
	public Color getCurrentColor();
	
	/**
	 * Adds a listener to this color provider.
	 * 
	 * @param l - listener to be added
	 */
	public void addColorChangeListener(ColorChangeListener l);
	
	/**
	 * Removes a listener from this color provider.
	 * 
	 * @param l - listener to be removed
	 */
	public void removeColorChangeListener(ColorChangeListener l);

}
