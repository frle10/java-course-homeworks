package hr.fer.zemris.java.hw17.jvdraw.interfaces;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Models objects that act as drawing tools for geometric objects.
 * <p>
 * The tools perform actions on mouse events.
 * 
 * @author Ivan Skorupan
 */
public interface Tool {
	
	/**
	 * Gets called on a mouse press.
	 * 
	 * @param e - mouse event object reference
	 */
	public void mousePressed(MouseEvent e);
	
	/**
	 * Gets called on a mouse release.
	 * 
	 * @param e - mouse event object reference
	 */
	public void mouseReleased(MouseEvent e);
	
	/**
	 * Gets called on a mouse click.
	 * 
	 * @param e - mouse event object reference
	 */
	public void mouseClicked(MouseEvent e);
	
	/**
	 * Gets called on a mouse move.
	 * 
	 * @param e - mouse event object reference
	 */
	public void mouseMoved(MouseEvent e);
	
	/**
	 * Gets called on a mouse drag.
	 * 
	 * @param e - mouse event object reference
	 */
	public void mouseDragged(MouseEvent e);
	
	/**
	 * Gets called when this tool has an object ready to be painted.
	 * 
	 * @param g2d - graphics object to use for drawing the ready object
	 */
	public void paint(Graphics2D g2d);
	
}
