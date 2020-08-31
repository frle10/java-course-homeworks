package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.geometry.Line;
import hr.fer.zemris.java.hw17.jvdraw.geometry.Point;
import hr.fer.zemris.java.hw17.jvdraw.gui.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.IColorProvider;

/**
 * 
 * 
 * @author Ivan Skorupan
 */
public class LineTool extends AbstractTool {

	/**
	 * Line that is currently being drawn.
	 */
	private Line line;

	/**
	 * Constructs a new {@link LineTool} object.
	 * 
	 * @param model - drawing model to add a new line to
	 * @param canvas - canvas on which to paint the line
	 * @param fgColorProvider - foreground color provider
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public LineTool(DrawingModel model, JDrawingCanvas canvas, IColorProvider fgColorProvider) {
		super(model, canvas, fgColorProvider, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		++clickCounter;
		if(clickCounter == 1) {
			Point startPoint = new Point(e.getX(), e.getY());
			line = new Line(startPoint, startPoint, fgColorProvider.getCurrentColor());
			canvas.repaint();
		} else {
			Point endPoint = new Point(e.getX(), e.getY());
			line.setEndPoint(endPoint);
			model.add(line);
			line = null;
			clickCounter = 0;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(clickCounter == 1) {
			Point endPoint = new Point(e.getX(), e.getY());
			line.setEndPoint(endPoint);
			canvas.repaint();
		}
	}
	
	@Override
	public void paint(Graphics2D g2d) {
		g2d.setColor(fgColorProvider.getCurrentColor());
		new GeometricalObjectPainter(g2d).visitGeometricalObject(line);
	}

}
