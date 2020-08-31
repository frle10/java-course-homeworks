package hr.fer.zemris.java.hw17.jvdraw.geometry;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.gui.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.GeometricalObjectListener;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.GeometricalObjectVisitor;

/**
 * Models an abstract geometric object.
 * <p>
 * Each such object has a foreground color and a list of listeners that listen
 * to the object's property changes.
 * 
 * @author Ivan Skorupan
 */
public abstract class GeometricalObject {
	
	/**
	 * Foreground color of this object after the final click
	 * (its registration to the drawing model).
	 */
	protected Color fgColor;
	
	/**
	 * List of listeners interested in this geometric object changes.
	 */
	List<GeometricalObjectListener> listeners = new ArrayList<>();
	
	public GeometricalObject(Color fgColor) {
		this.fgColor = Objects.requireNonNull(fgColor);
	}
	
	/**
	 * Helper method that notifies all {@link GeometricalObjectListener}
	 * objects about this object's change.
	 */
	protected void notifyListeners() {
		for(GeometricalObjectListener l : listeners) {
			l.geometricalObjectChanged(this);
		}
	}
	
	/**
	 * Getter for this geometric object's foreground color.
	 * 
	 * @return this shape's foreground color
	 */
	public Color getFgColor() {
		return fgColor;
	}
	
	/**
	 * Setter for this geometric object's foreground color.
	 * 
	 * @param fgColor - foreground color
	 */
	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
		notifyListeners();
	}
	
	/**
	 * Adds a geometric object listener to this object.
	 * 
	 * @param l - listener to be added
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.add(Objects.requireNonNull(l));
	}
	
	/**
	 * Removes a geometric object listener from this object.
	 * 
	 * @param l - listener to be removed
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Accepts a visitor to this object by calling the appropriate method
	 * of given visitor.
	 * 
	 * @param v - visitor to be accepted
	 */
	public abstract void accept(GeometricalObjectVisitor v);
	
	/**
	 * Creates an appropriate geometric object editor instance for
	 * this object.
	 * 
	 * @return an instance of {@link GeometricalObjectEditor}, properly sub-typed
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();
	
}
