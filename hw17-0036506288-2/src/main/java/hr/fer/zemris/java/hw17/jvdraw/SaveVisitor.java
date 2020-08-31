package hr.fer.zemris.java.hw17.jvdraw;

import static hr.fer.zemris.java.hw17.jvdraw.Util.*;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.geometry.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.Line;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.GeometricalObjectVisitor;

/**
 * This class models a geometric object visitor that knows how to
 * generate a line in a save file properly formatted for given
 * object type.
 * 
 * @author Ivan Skorupan
 */
public class SaveVisitor implements GeometricalObjectVisitor {
	
	/**
	 * Last generated save line.
	 */
	private StringBuilder saveLine;
	
	@Override
	public void visit(Line line) {
		Color fgColor = line.getFgColor();
		saveLine = new StringBuilder();
		saveLine.append(LINE_IDENTIFIER + " ");
		saveLine.append(line.getStartPoint().getX() + " " + line.getStartPoint().getY() + " ");
		saveLine.append(line.getEndPoint().getX() + " " + line.getEndPoint().getY() + " ");
		saveLine.append(fgColor.getRed() + " " + fgColor.getGreen() + " " + fgColor.getBlue());
		saveLine.append("\n");
	}

	@Override
	public void visit(Circle circle) {
		Color fgColor = circle.getFgColor();
		saveLine = new StringBuilder();
		saveLine.append(CIRCLE_IDENTIFIER + " ");
		saveLine.append(circle.getCenter().getX() + " " + circle.getCenter().getY() + " ");
		saveLine.append(circle.getRadius() + " ");
		saveLine.append(fgColor.getRed() + " " + fgColor.getGreen() + " " + fgColor.getBlue());
		saveLine.append("\n");
	}

	@Override
	public void visit(FilledCircle fCircle) {
		Color fgColor = fCircle.getFgColor();
		Color fillColor = fCircle.getFillColor();
		saveLine = new StringBuilder();
		saveLine.append(FILLED_CIRCLE_IDENTIFIER + " ");
		saveLine.append(fCircle.getCenter().getX() + " " + fCircle.getCenter().getY() + " ");
		saveLine.append(fCircle.getRadius() + " ");
		saveLine.append(fgColor.getRed() + " " + fgColor.getGreen() + " " + fgColor.getBlue() + " ");
		saveLine.append(fillColor.getRed() + " " + fillColor.getGreen() + " " + fillColor.getBlue());
		saveLine.append("\n");
	}

	/**
	 * Getter for last generated save line as a string.
	 * 
	 * @return last generated save line
	 */
	public String getSaveLine() {
		return saveLine.toString();
	}
	
}
