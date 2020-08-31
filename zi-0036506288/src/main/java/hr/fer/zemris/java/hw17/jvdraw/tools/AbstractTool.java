package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.event.MouseEvent;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.gui.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.Tool;

/**
 * This class models an abstract tool for drawing geometric objects.
 * <p>
 * Since all our three supported tools share certain same behavior patterns,
 * we can have this abstract class to minimize code duplication.
 * 
 * @author Ivan Skorupan
 */
public abstract class AbstractTool implements Tool {
	
	/**
	 * Drawing model to operate upon.
	 */
	protected DrawingModel model;
	
	/**
	 * Canvas on which to paint the objects.
	 */
	protected JDrawingCanvas canvas;
	
	/**
	 * Foreground color provider.
	 */
	protected IColorProvider fgColorProvider;
	
	/**
	 * Background color provider.
	 */
	protected IColorProvider bgColorProvider;
	
	/**
	 * Variable that knows how many clicks there were which is necessary
	 * information for object drawing.
	 */
	protected int clickCounter;
	
	/**
	 * Constructs a new {@link AbstractTool} object.
	 * 
	 * @param model - drawing model to add a new object to
	 * @param canvas - canvas on which to paint the objects
	 * @param fgColorProvider - foreground color provider
	 * @param bgColorProvider - background color provider
	 * @throws NullPointerException if any of the arguments is <code>null</code> except <code>bgColorProvider</code>
	 */
	public AbstractTool(DrawingModel model, JDrawingCanvas canvas, IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		this.model = Objects.requireNonNull(model);
		this.canvas = Objects.requireNonNull(canvas);
		this.fgColorProvider = Objects.requireNonNull(fgColorProvider);
		this.bgColorProvider = bgColorProvider;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
}
