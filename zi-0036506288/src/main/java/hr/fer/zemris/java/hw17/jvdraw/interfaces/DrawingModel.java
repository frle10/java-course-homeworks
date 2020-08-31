package hr.fer.zemris.java.hw17.jvdraw.interfaces;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;

/**
 * This interface defines methods that a drawing model should have.
 * <p>
 * A drawing model must provide methods for adding and removing listeners
 * interested in changes in the model.
 * <p>
 * Also, the model must have a way of managing the objects contained inside it.
 * 
 * @author Ivan Skorupan
 */
public interface DrawingModel {
	
	/**
	 * Returns the number of objects in this model.
	 * 
	 * @return number of objects in this model
	 */
	public int getSize();
	
	/**
	 * Returns the object at index <code>index</code>.
	 * 
	 * @param index - index at which the object to be returned is
	 * @return object at <code>index</code>
	 */
	public GeometricalObject getObject(int index);
	
	/**
	 * Adds a geometric object to the model.
	 * 
	 * @param object - object to be added
	 */
	public void add(GeometricalObject object);
	
	/**
	 * Removes a geometric object from the model.
	 * 
	 * @param object - object to be removed
	 */
	public void remove(GeometricalObject object);
	
	/**
	 * Changes the index of given <code>object</code> by applying
	 * an <code>offset</code> to it.
	 * 
	 * @param object - object whose order should be changed
	 * @param offset - offset to be applied to object's index
	 */
	public void changeOrder(GeometricalObject object, int offset);
	
	/**
	 * Returns the index of given <code>object</code> in this model.
	 * 
	 * @param object - object whose index should be returned
	 * @return index of <code>object</code>
	 */
	public int indexOf(GeometricalObject object);
	
	/**
	 * Clears the model of all objects.
	 */
	public void clear();
	
	/**
	 * Sets the modified flag to <code>false</code>.
	 */
	public void clearModifiedFlag();
	
	/**
	 * Returns the modified flag.
	 * 
	 * @return <code>true</code> if the model was modified since last save, <code>false</code> otherwise
	 */
	public boolean isModified();
	
	/**
	 * Adds a listener to this model.
	 * 
	 * @param l - listener to be added
	 */
	public void addDrawingModelListener(DrawingModelListener l);
	
	/**
	 * Removes a listener from this model.
	 * 
	 * @param l - listener to be removed
	 */
	public void removeDrawingModelListener(DrawingModelListener l);
	
}
