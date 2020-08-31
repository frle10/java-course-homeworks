package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.hw17.jvdraw.geometry.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.geometry.Line;
import hr.fer.zemris.java.hw17.jvdraw.geometry.Point;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModel;

/**
 * This class provides <code>public static</code> methods used
 * in {@link JVDraw} class to implement actions such as saving
 * a file or opening an existing file.
 * 
 * @author Ivan Skorupan
 */
public class Util {
	
	/**
	 * File extension that this application can save and open.
	 */
	public static final String JVDRAW_FILE_EXTENSION = "jvd";
	
	/**
	 * Identifier of a line in a .jvd file.
	 */
	public static final String LINE_IDENTIFIER = "LINE";
	
	/**
	 * Identifier of a circle in a .jvd file.
	 */
	public static final String CIRCLE_IDENTIFIER = "CIRCLE";
	
	/**
	 * Identifier of a filled circle in a .jvd file.
	 */
	public static final String FILLED_CIRCLE_IDENTIFIER = "FCIRCLE";
	
	/**
	 * This method performs the export operation on a given <code>model</code> and
	 * makes a file on <code>exportPath</code>.
	 * <p>
	 * The file exported is an image in JPG, PNG or GIF format whose dimensions are
	 * determined by the smallest bounding box encompassing all elements of the
	 * given drawing model.
	 * 
	 * @param exportPath - path to which to export the image
	 * @param model - drawing model to make an image from
	 * @throws Exception if there is any kind of error while generating the export image
	 */
	public static void export(Path exportPath, DrawingModel model) throws Exception {
		GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
		for(int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(bbcalc);
		}
		
		Rectangle boundingBox = bbcalc.getBoundingBox();
		BufferedImage image = new BufferedImage(boundingBox.width, boundingBox.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		g.translate(-boundingBox.x, -boundingBox.y);
		
		g.setColor(Color.WHITE);
		g.fillRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
		
		GeometricalObjectPainter painter = new GeometricalObjectPainter(g);
		for(int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(painter);
		}
		
		g.dispose();
		File file = exportPath.toFile();
		String fileName = file.getName();
		try {
			ImageIO.write(image, fileName.substring(fileName.lastIndexOf(".") + 1), file);
		} catch (IOException e) {
			throw new Exception("The image was not exported because of an IO error!");
		}
	}
	
	/**
	 * This method performs a saving operation on a given <code>savePath</code> and
	 * <code>model</code>.
	 * 
	 * @param savePath - path to which to save the <code>model</code>
	 * @param model - drawing model to save
	 * @throws Exception if any kind of error occurs while saving
	 */
	public static void saveFile(Path savePath, DrawingModel model) throws Exception {
		String fileName = savePath.getFileName().toString();
		String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
		if(!fileExtension.equals(JVDRAW_FILE_EXTENSION)) {
			throw new Exception("The file extension must be .jvd! The file was not saved.");
		}
		
		try(OutputStream os = new BufferedOutputStream(Files.newOutputStream(savePath))) {
			SaveVisitor sv = new SaveVisitor();
			for(int i = 0; i < model.getSize(); i++) {
				GeometricalObject object = model.getObject(i);
				object.accept(sv);
				os.write(sv.getSaveLine().getBytes());
			}
		} catch(IOException e) {
			throw new Exception("The file was not saved because of an IO error!");
		}
	}
	
	/**
	 * This method opens a .jvd file from <code>openPath</code> and loads it into
	 * the <code>model</code>.
	 * 
	 * @param openPath - path from which to load a .jvd file
	 * @param model - drawing model to load the opened file into
	 * @throws Exception if any kind of error occurs while opening the file
	 */
	public static void openFile(Path openPath, DrawingModel model) throws Exception {
		String fileName = openPath.getFileName().toString();
		String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
		if(!fileExtension.equals(JVDRAW_FILE_EXTENSION)) {
			throw new Exception("This application supports only .jvd files! The file was not opened.");
		}
		
		List<GeometricalObject> objects = new ArrayList<>();
		List<String> lines = Files.readAllLines(openPath);
		for(String fileLine : lines) {
			String[] tokens = fileLine.split(" ");
			String objectType = tokens[0];
			GeometricalObject object = null;
			if(objectType.equals(LINE_IDENTIFIER)) {
				object = parseLine(tokens, lines.indexOf(fileLine));
			} else if(objectType.equals(CIRCLE_IDENTIFIER)) {
				object = parseCircle(tokens, lines.indexOf(fileLine));
			} else if(objectType.equals(FILLED_CIRCLE_IDENTIFIER)) {
				object = parseFilledCircle(tokens, lines.indexOf(fileLine));
			}
			
			if(object != null) {
				objects.add(object);
			}
		}
		
		model.clear();
		for(GeometricalObject object : objects) {
			model.add(object);
		}
	}
	
	/**
	 * Parses a line from the save file that represents a {@link Line}.
	 * 
	 * @param lineTokens - distinct line information
	 * @param lineIndex - index of line in document file
	 * @return an instance of {@link Line} built from given <code>lineTokens</code>
	 * @throws Exception if any kind of error occurs while parsing the line
	 */
	private static Line parseLine(String[] lineTokens, int lineIndex) throws Exception {
		if(lineTokens.length != 8) {
			throw new Exception("There is an invalid definition of a line in row number " + (lineIndex + 1) + "!");
		}
		
		int x1, y1, x2, y2;
		int r, g, b;
		
		try {
			x1 = Integer.parseInt(lineTokens[1]);
			y1 = Integer.parseInt(lineTokens[2]);
			x2 = Integer.parseInt(lineTokens[3]);
			y2 = Integer.parseInt(lineTokens[4]);
			
			r = Integer.parseInt(lineTokens[5]);
			g = Integer.parseInt(lineTokens[6]);
			b = Integer.parseInt(lineTokens[7]);
		} catch(NumberFormatException ex) {
			throw new Exception("There is an invalid definition of a line in row number " + (lineIndex + 1) + "!");
		}
		
		Point startPoint = new Point(x1, y1);
		Point endPoint = new Point(x2, y2);
		Line line = new Line(startPoint, endPoint, new Color(r, g, b));
		return line;
	}
	
	/**
	 * Parses a line from the save file that represents a {@link Circle}.
	 * 
	 * @param circleTokens - distinct circle information
	 * @param lineIndex - index of line in document file
	 * @return an instance of {@link Circle} built from given <code>circleTokens</code>
	 * @throws Exception if any kind of error occurs while parsing the circle
	 */
	private static Circle parseCircle(String[] circleTokens, int lineIndex) throws Exception {
		if(circleTokens.length != 7) {
			throw new Exception("There is an invalid definition of a circle in row number " + (lineIndex + 1) + "!");
		}
		
		int centerX, centerY, radius;
		int r, g, b;
		
		try {
			centerX = Integer.parseInt(circleTokens[1]);
			centerY = Integer.parseInt(circleTokens[2]);
			radius = Integer.parseInt(circleTokens[3]);
			
			r = Integer.parseInt(circleTokens[4]);
			g = Integer.parseInt(circleTokens[5]);
			b = Integer.parseInt(circleTokens[6]);
		} catch(NumberFormatException ex) {
			throw new Exception("There is an invalid definition of a circle in row number " + (lineIndex + 1) + "!");
		}
		
		Point center = new Point(centerX, centerY);
		Circle circle = new Circle(center, radius, new Color(r, g, b));
		return circle;
	}
	
	/**
	 * Parses a line from the save file that represents a {@link FilledCircle}.
	 * 
	 * @param filledCircleTokens - distinct filled circle information
	 * @param lineIndex - index of line in document file
	 * @return an instance of {@link FilledCircle} built from given <code>filledCircleTokens</code>
	 * @throws Exception if any kind of error occurs while parsing the filled circle
	 */
	private static FilledCircle parseFilledCircle(String[] filledCircleTokens, int lineIndex) throws Exception {
		if(filledCircleTokens.length != 10) {
			throw new Exception("There is an invalid definition of a filled circle in row number " + (lineIndex + 1) + "!");
		}
		
		int centerX, centerY, radius;
		int fgR, fgG, fgB;
		int fillR, fillG, fillB;
		
		try {
			centerX = Integer.parseInt(filledCircleTokens[1]);
			centerY = Integer.parseInt(filledCircleTokens[2]);
			radius = Integer.parseInt(filledCircleTokens[3]);
			
			fgR = Integer.parseInt(filledCircleTokens[4]);
			fgG = Integer.parseInt(filledCircleTokens[5]);
			fgB = Integer.parseInt(filledCircleTokens[6]);
			
			fillR = Integer.parseInt(filledCircleTokens[7]);
			fillG = Integer.parseInt(filledCircleTokens[8]);
			fillB = Integer.parseInt(filledCircleTokens[9]);
		} catch(NumberFormatException ex) {
			throw new Exception("There is an invalid definition of a filled circle in row number " + (lineIndex + 1) + "!");
		}
		
		Point center = new Point(centerX, centerY);
		FilledCircle fCircle = new FilledCircle(center, radius, new Color(fgR, fgG, fgB), new Color(fillR, fillG, fillB));
		fCircle.setFgColor(new Color(fgR, fgG, fgB));
		return fCircle;
	}
	
}
