package hr.fer.zemris.java.hw17.jvdraw.geometry;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.gui.FilledTriangleEditor;
import hr.fer.zemris.java.hw17.jvdraw.gui.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.GeometricalObjectVisitor;

public class FilledTriangle extends GeometricalObject {
	
	private Point firstPoint;
	
	private Point secondPoint;
	
	private Point thirdPoint;
	
	private Color fillColor;
	
	public FilledTriangle(Point firstPoint, Point secondPoint, Point thirdPoint, Color fgColor, Color fillColor) {
		super(fgColor);
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
		this.thirdPoint = thirdPoint;
		this.fillColor = fillColor;
	}
	
	public Point getFirstPoint() {
		return firstPoint;
	}



	public void setFirstPoint(Point firstPoint) {
		this.firstPoint = firstPoint;
	}



	public Point getSecondPoint() {
		return secondPoint;
	}



	public void setSecondPoint(Point secondPoint) {
		this.secondPoint = secondPoint;
	}



	public Point getThirdPoint() {
		return thirdPoint;
	}



	public void setThirdPoint(Point thirdPoint) {
		this.thirdPoint = thirdPoint;
	}



	public Color getFillColor() {
		return fillColor;
	}



	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}



	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledTriangleEditor(this);
	}

	@Override
	public String toString() {
		String hexFillColor = String.format("#%02x%02x%02x", fillColor.getRed(),
				fillColor.getGreen(), fillColor.getBlue()).toUpperCase();
		return "Filled triangle " + firstPoint + ", " + secondPoint + ", " + thirdPoint + ", " + hexFillColor;
	}
	
}
