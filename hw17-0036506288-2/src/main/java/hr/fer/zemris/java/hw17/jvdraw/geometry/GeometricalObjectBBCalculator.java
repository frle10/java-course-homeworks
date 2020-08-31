package hr.fer.zemris.java.hw17.jvdraw.geometry;

import static java.lang.Math.*;

import java.awt.Rectangle;

import hr.fer.zemris.java.hw17.jvdraw.interfaces.GeometricalObjectVisitor;

/**
 * Models a geometric object visitor that knows how to calculate the smallest
 * bounding box for each type of geometric object and also the smallest bounding
 * box that encompasses all geometric object in a given drawing model.
 * 
 * @author Ivan Skorupan
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {
	
	/**
	 * This visitor's bounding box that encompasses all geometric objects
	 * in a drawing model.
	 */
	private Rectangle boundingBox;
	
	@Override
	public void visit(Line line) {
		int minX = min(line.getStartPoint().getX(), line.getEndPoint().getX());
		int minY = min(line.getStartPoint().getY(), line.getEndPoint().getY());
		int width = abs(line.getStartPoint().getX() - line.getEndPoint().getX());
		int height = abs(line.getStartPoint().getY() - line.getEndPoint().getY());
		
		Rectangle lineBounds = new Rectangle(minX, minY, width, height);
		updateBoundingBox(lineBounds);
	}
	
	@Override
	public void visit(Circle circle) {
		int minX = circle.getCenter().getX() - circle.getRadius();
		int minY = circle.getCenter().getY() - circle.getRadius();
		int width = 2 * circle.getRadius();
		int height = 2 * circle.getRadius();
		
		Rectangle circleBounds = new Rectangle(minX, minY, width, height);
		updateBoundingBox(circleBounds);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		int minX = filledCircle.getCenter().getX() - filledCircle.getRadius();
		int minY = filledCircle.getCenter().getY() - filledCircle.getRadius();
		int width = 2 * filledCircle.getRadius();
		int height = 2 * filledCircle.getRadius();
		
		Rectangle filledCircleBounds = new Rectangle(minX, minY, width, height);
		updateBoundingBox(filledCircleBounds);
	}
	
	/**
	 * Getter for bounding box.
	 * 
	 * @return bounding box
	 */
	public Rectangle getBoundingBox() {
		return boundingBox;
	}
	
	/**
	 * Updates this visitor's bounding box given a new object's calculated
	 * bounds.
	 * <p>
	 * If the object's bounds are inside current bounding box, this method does nothing,
	 * otherwise it expands the bounding box to encompass the new object's bounds too.
	 * 
	 * @param objectBounds - new object bounds
	 */
	private void updateBoundingBox(Rectangle objectBounds) {
		if(boundingBox == null) {
			boundingBox = objectBounds;
		} else {
			mergeBounds(objectBounds);
		}
	}
	
	/**
	 * In case the bounding box needs to be expanded with another object's bounds,
	 * this method does that.
	 * 
	 * @param objectBounds - another object's bounds that the bounding box should be expanded with
	 */
	private void mergeBounds(Rectangle objectBounds) {
		int xMin = min(boundingBox.x, objectBounds.x);
		int yMin = min(boundingBox.y, objectBounds.y);
		
		int xMax = max(boundingBox.x + boundingBox.width, objectBounds.x + objectBounds.width);
		int yMax = max(boundingBox.y + boundingBox.height, objectBounds.y + objectBounds.height);
		
		boundingBox = new Rectangle(xMin, yMin, xMax - xMin, yMax - yMin);
	}
	
}
