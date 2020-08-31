package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import hr.fer.zemris.java.hw17.jvdraw.geometry.FilledTriangle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.geometry.Point;
import hr.fer.zemris.java.hw17.jvdraw.gui.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.IColorProvider;

public class FilledTriangleTool extends AbstractTool {
	
	private FilledTriangle filledTriangle;
	
	private Point firstPoint;
	
	private Point secondPoint;
	
	private Point thirdPoint;
	
	public FilledTriangleTool(DrawingModel model, JDrawingCanvas canvas, IColorProvider fgColorProvider,
			IColorProvider bgColorProvider) {
		super(model, canvas, fgColorProvider, bgColorProvider);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		++clickCounter;
		if(clickCounter == 1) {
			firstPoint = new Point(e.getX(), e.getY());
			filledTriangle = new FilledTriangle(firstPoint, null, null, fgColorProvider.getCurrentColor(), bgColorProvider.getCurrentColor());
		} else if(clickCounter == 2) {
			secondPoint = new Point(e.getX(), e.getY());
			filledTriangle.setSecondPoint(secondPoint);
			canvas.repaint();
		} else {
			thirdPoint = new Point(e.getX(), e.getY());
			filledTriangle.setThirdPoint(thirdPoint);
			model.add(filledTriangle);
			filledTriangle = null;
			clickCounter = 0;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(clickCounter == 1) {
			secondPoint = new Point(e.getX(), e.getY());
			filledTriangle.setSecondPoint(secondPoint);
			canvas.repaint();
		} else if(clickCounter == 2) {
			thirdPoint = new Point(e.getX(), e.getY());
			filledTriangle.setThirdPoint(thirdPoint);
			canvas.repaint();
		}
	}

	@Override
	public void paint(Graphics2D g2d) {
		g2d.setColor(fgColorProvider.getCurrentColor());
		new GeometricalObjectPainter(g2d).visitGeometricalObject(filledTriangle);
	}
	
}
