package hr.fer.zemris.java.hw17.jvdraw.tools;

import static hr.fer.zemris.java.hw17.jvdraw.geometry.Point.*;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.geometry.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.geometry.Point;
import hr.fer.zemris.java.hw17.jvdraw.gui.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.IColorProvider;

/**
 * 
 * 
 * @author Ivan Skorupan
 */
public class CircleTool extends AbstractTool {
	
	/**
	 * Circle that is currently being drawn.
	 */
	private Circle circle;
	
	/**
	 * Constructs a new {@link CircleTool} object.
	 * 
	 * @param model - drawing model to add a new object to
	 * @param canvas - canvas on which to paint the objects
	 * @param fgColorProvider - foreground color provider
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public CircleTool(DrawingModel model, JDrawingCanvas canvas, IColorProvider fgColorProvider) {
		super(model, canvas, fgColorProvider, null);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		++clickCounter;
		if(clickCounter == 1) {
			Point center = new Point(e.getX(), e.getY());
			circle = new Circle(center, 0, fgColorProvider.getCurrentColor());
			canvas.repaint();
		} else {
			Point borderPoint = new Point(e.getX(), e.getY());
			circle.setRadius(distance(circle.getCenter(), borderPoint));
			model.add(circle);
			circle = null;
			clickCounter = 0;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(clickCounter == 1) {
			Point borderPoint = new Point(e.getX(), e.getY());
			circle.setRadius(distance(circle.getCenter(), borderPoint));
			canvas.repaint();
		}
	}
	
	@Override
	public void paint(Graphics2D g2d) {
		g2d.setColor(fgColorProvider.getCurrentColor());
		new GeometricalObjectPainter(g2d).visitGeometricalObject(circle);
	}
	
}
