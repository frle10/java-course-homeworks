package hr.fer.zemris.java.hw17.jvdraw.interfaces;

/**
 * Models objects that listen to changes in the drawing model.
 * 
 * @author Ivan Skorupan
 */
public interface DrawingModelListener {
	
	/**
	 * Gets called when a new object was added to the drawing model
	 * <code>source</code>.
	 * 
	 * @param source - drawing model that had an object added
	 * @param index0 - index of added object
	 * @param index1 - index of added object
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);
	
	/**
	 * Gets called when an object gets removed from the drawing model
	 * <code>source</code>.
	 * 
	 * @param source - drawing model that had an object removed
	 * @param index0 - index of removed object
	 * @param index1 - index of removed object
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);
	
	/**
	 * Gets called when an object in drawing model <code>source</code>
	 * gets changed.
	 * 
	 * @param source - drawing model that had an object changed
	 * @param index0 - index of changed object
	 * @param index1 - index of changed object
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
	
}
