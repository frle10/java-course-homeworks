package hr.fer.zemris.java.hw17.jvdraw.geometry;

import java.awt.Graphics2D;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.interfaces.GeometricalObjectVisitor;

/**
 * Models a geometric object visitor that knows how to paint all different types
 * of geometric objects on a canvas.
 * 
 * @author Ivan Skorupan
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {
	
	/**
	 * Graphics object used to paint geometric shapes.
	 */
	private Graphics2D graphics;
	
	/**
	 * Constructs a new {@link GeometricalObjectPainter} object with
	 * provided {@link Graphics2D} object which can be used to draw
	 * 2D shapes.
	 * 
	 * @param graphics - {@link Graphics2D} object to use for drawing geometry
	 * @throws NullPointerException if <code>graphics</code> is <code>null</code>
	 */
	public GeometricalObjectPainter(Graphics2D graphics) {
		this.graphics = Objects.requireNonNull(graphics);
	}
	
	/**
	 * Helper method that can distinguish the actual type of a
	 * {@link GeometricalObject} reference and delegates further
	 * work to appropriate methods.
	 * 
	 * @param object - a geometric object to determine the type of
	 */
	public void visitGeometricalObject(GeometricalObject object) {
		if(object == null) {
			return;
		} else if(object instanceof Line) {
			visit((Line) object);
		} else if(object instanceof Circle) {
			visit((Circle) object);
		} else if(object instanceof FilledCircle) {
			visit((FilledCircle) object);
		} else if(object instanceof FilledTriangle) {
			visit((FilledTriangle) object);
		}
	}
	
	@Override
	public void visit(Line line) {
		Point startPoint = line.getStartPoint();
		Point endPoint = line.getEndPoint();
		graphics.setColor(line.getFgColor());
		graphics.drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
	}

	@Override
	public void visit(Circle circle) {
		Point center = circle.getCenter();
		int radius = circle.getRadius();
		graphics.setColor(circle.getFgColor());
		graphics.drawOval(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		Point center = filledCircle.getCenter();
		int radius = filledCircle.getRadius();
		
		graphics.setColor(filledCircle.getFillColor());
		graphics.fillOval(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius);
		
		graphics.setColor(filledCircle.getFgColor());
		graphics.drawOval(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius);
	}
	
	public void visit(FilledTriangle filledTriangle) {
		Point firstPoint = filledTriangle.getFirstPoint();
		Point secondPoint = filledTriangle.getSecondPoint();
		Point thirdPoint = filledTriangle.getThirdPoint();
		
		if(thirdPoint == null) {
			graphics.drawLine(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY());
		} else {
			int x[] = new int[] {firstPoint.getX(), secondPoint.getX(), thirdPoint.getX()};
			int y[] = new int[] {firstPoint.getY(), secondPoint.getY(), thirdPoint.getY()};
			int n = 3;
			
			graphics.setColor(filledTriangle.getFillColor());
			graphics.fillPolygon(x, y, n);
			
			graphics.setColor(filledTriangle.getFgColor());
			graphics.drawPolygon(x, y, n);
		}
	}
	
}
