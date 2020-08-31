package hr.fer.zemris.java.hw17.jvdraw.interfaces;

import java.awt.Color;

/**
 * Models objects that listen for color changes on color providers.
 * 
 * @author Ivan Skorupan
 */
public interface ColorChangeListener {
	
	/**
	 * Performs an action when a new color gets selected on <code>source</code>.
	 * 
	 * @param source - a color provider whose color has changed
	 * @param oldColor - old color of the source
	 * @param newColor - new color of the source
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
	
}
