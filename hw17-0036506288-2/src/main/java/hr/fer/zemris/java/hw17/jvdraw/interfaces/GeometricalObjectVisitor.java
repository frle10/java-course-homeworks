package hr.fer.zemris.java.hw17.jvdraw.interfaces;

import hr.fer.zemris.java.hw17.jvdraw.geometry.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.Line;

/**
 * Models objects that can process geometric objects in arbitrary ways
 * (geometric object visitors).
 * 
 * @author Ivan Skorupan
 */
public interface GeometricalObjectVisitor {
	
	/**
	 * Gets called when this visitor stumbles upon a line.
	 * 
	 * @param line - line to be visited
	 */
	public abstract void visit(Line line);
	
	/**
	 * Gets called when this visitor stumbles upon a circle.
	 * 
	 * @param circle - circle to be visited
	 */
	public abstract void visit(Circle circle);
	
	/**
	 * Gets called when this visitor stumbles upon a filled circle.
	 * 
	 * @param filledCircle - filled circle to be visited
	 */
	public abstract void visit(FilledCircle filledCircle);
	
}
