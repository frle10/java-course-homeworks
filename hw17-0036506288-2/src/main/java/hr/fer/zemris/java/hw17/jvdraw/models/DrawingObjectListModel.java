package hr.fer.zemris.java.hw17.jvdraw.models;

import java.util.Objects;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModelListener;

/**
 * Models a custom list model that has a reference to a drawing model and can therefore manage it through
 * actions happening in the accompanying list.
 * <p>
 * Effectively, this class is an adapter of {@link DrawingModel}.
 * 
 * @author Ivan Skorupan
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Internally stored reference to a drawing model for utilizing
	 * the Adapter Design Pattern.
	 */
	private DrawingModel model;
	
	/**
	 * Constructs a new {@link DrawingObjectListModel} object with
	 * provided drawing model to be an adapter to.
	 * 
	 * @param model - drawing model to be an adapter to
	 * @throws NullPointerException if <code>model</code> is <code>null</code>
	 */
	public DrawingObjectListModel(DrawingModel model) {
		this.model = Objects.requireNonNull(model);
		model.addDrawingModelListener(this);
	}
	
	@Override
	public int getSize() {
		return model.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}
	
	/**
	 * Changes the position of given <code>object</code> by applying
	 * an <code>offset</code> to its position in the drawing model.
	 * 
	 * @param object
	 * @param offset
	 */
	public void changeOrder(GeometricalObject object, int offset) {
		model.changeOrder(object, offset);
	}
	
	/**
	 * Removes the given <code>object</code> from the drawing model.
	 * 
	 * @param object - a geometric object to be removed from the drawing model
	 */
	public void remove(GeometricalObject object) {
		model.remove(object);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		fireIntervalAdded(this, index0, index1);
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		fireIntervalRemoved(this, index0, index1);
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		fireContentsChanged(this, index0, index1);
	}
	
}
