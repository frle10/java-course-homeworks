package hr.fer.zemris.java.hw17.jvdraw.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.GeometricalObjectListener;

/**
 * This class is an implementation of {@link DrawingModel}.
 * <p>
 * This model has a list of geometric objects that are contained in it and
 * through interface method implementations can manipulate that list in
 * meaningful ways.
 * 
 * @author Ivan Skorupan
 */
public class JVDrawingModel implements DrawingModel, GeometricalObjectListener {
	
	/**
	 * Internal list of all geometrical objects contained in this model.
	 */
	private List<GeometricalObject> objects = new ArrayList<>();
	
	/**
	 * List of all listeners that are interested in object changes in this drawing model.
	 */
	private List<DrawingModelListener> listeners = new ArrayList<>();
	
	/**
	 * The internal modification flag that indicates if any object
	 * in this model was added, removed or changed since last update.
	 */
	private boolean modified;
	
	/**
	 * Helper method that notifies all drawing model
	 * listeners about an added geometrical object.
	 * 
	 * @param index - index at which a new object was added
	 */
	private void notifyAdded(int index) {
		for(DrawingModelListener l : listeners) {
			l.objectsAdded(this, index, index);
		}
	}
	
	/**
	 * Helper method that notifies all drawing model
	 * listeners about a removed geometrical object.
	 * 
	 * @param index - index at which the old object used to be
	 */
	private void notifyRemoved(int index) {
		for(DrawingModelListener l : listeners) {
			l.objectsRemoved(this, index, index);
		}
	}
	
	/**
	 * Helper method that notifies all drawing model
	 * listeners about a changed  order of
	 * geometrical objects.
	 * 
	 * @param index1 - index of first swapped object
	 * @param index2 - index of second swapped object
	 */
	private void notifyChangedOrder(int index1, int index2) {
		for(DrawingModelListener l : listeners) {
			l.objectsChanged(this, index1, index2);
		}
	}
	
	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);
		object.addGeometricalObjectListener(this);
		notifyAdded(objects.indexOf(object));
		modified = true;
	}

	@Override
	public void remove(GeometricalObject object) {
		int index = objects.indexOf(object);
		if(index != -1) {
			objects.remove(object);
			notifyRemoved(index);
			modified = true;
		}
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		int currentIndex = objects.indexOf(object);
		int newIndex = currentIndex + offset;
		
		if(currentIndex != newIndex && newIndex >= 0 && newIndex < objects.size()) {
			GeometricalObject other = objects.get(newIndex);
			objects.set(currentIndex, other);
			objects.set(newIndex, object);
			notifyChangedOrder(currentIndex, newIndex);
			modified = true;
		}
	}

	@Override
	public int indexOf(GeometricalObject object) {
		return objects.indexOf(object);
	}

	@Override
	public void clear() {
		objects.clear();
		notifyRemoved(0);
		modified = true;
	}

	@Override
	public void clearModifiedFlag() {
		modified = false;
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(Objects.requireNonNull(l));
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void geometricalObjectChanged(GeometricalObject o) {
		int index = objects.indexOf(o);
		for(DrawingModelListener l : listeners) {
			l.objectsChanged(this, index, index);
		}
	}
	
}
