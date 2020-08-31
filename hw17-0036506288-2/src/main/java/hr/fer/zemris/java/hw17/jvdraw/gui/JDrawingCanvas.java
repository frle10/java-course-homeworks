package hr.fer.zemris.java.hw17.jvdraw.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.function.Supplier;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.Tool;

/**
 * Models a custom component on which a user can draw with the mouse.
 * <p>
 * The canvas has an attributed {@link DrawingModel} which it uses
 * for geometric object management.
 * 
 * @author Ivan Skorupan
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Drawing model upon which this component operates.
	 */
	private DrawingModel model;
	
	/**
	 * A supplier of currently selected drawing tool.
	 */
	private Supplier<Tool> toolSupplier;
	
	/**
	 * Constructs a new {@link JDrawingCanvas} object with provided
	 * tool supplier and a drawing model on which to operate.
	 * 
	 * @param model - drawing model to work with
	 * @param toolSupplier - supplier of currently selected drawing tool
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public JDrawingCanvas(DrawingModel model, Supplier<Tool> toolSupplier) {
		this.model = Objects.requireNonNull(model);
		this.toolSupplier = Objects.requireNonNull(toolSupplier);
		model.addDrawingModelListener(this);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				toolSupplier.get().mouseClicked(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				toolSupplier.get().mousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				toolSupplier.get().mouseReleased(e);
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				toolSupplier.get().mouseMoved(e);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				toolSupplier.get().mouseDragged(e);
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GeometricalObjectPainter painter = new GeometricalObjectPainter(g2d);
		for(int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(painter);
		}
		toolSupplier.get().paint(g2d);
	}
	
	/**
	 * Getter for reference to the drawing model.
	 * 
	 * @return drawing model this component operates upon
	 */
	public DrawingModel getModel() {
		return model;
	}
	
	/**
	 * Getter for currently selected drawing tool supplier.
	 * 
	 * @return supplier for currently selected drawing tool
	 */
	public Supplier<Tool> getToolSupplier() {
		return toolSupplier;
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		repaint();
	}
	
	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		repaint();
	}
	
	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		repaint();
	}
	
}
