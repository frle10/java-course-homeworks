package hr.fer.zemris.java.hw17.jvdraw.interfaces;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;

/**
 * Models objects that listen to changes on a geometric object.
 * 
 * @author Ivan Skorupan
 */
public interface GeometricalObjectListener {
	
	/**
	 * Gets called when a property change occurs on a geometric object
	 * <code>o</code>.
	 * 
	 * @param o - geometric object whose properties changeds
	 */
	public void geometricalObjectChanged(GeometricalObject o);
	
}
