package hr.fer.zemris.java.hw17.jvdraw.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JOptionPane;

import hr.fer.zemris.java.hw17.jvdraw.geometry.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.models.DrawingObjectListModel;

/**
 * Models a custom list that holds the geometric objects of a given
 * drawing model.
 * <p>
 * The list has added mouse listeners that respond to a double click and certain keyboard
 * clicks in order to implements desired functionality (rearranging the objects order
 * and opening object editor's).
 * 
 * @author Ivan Skorupan
 */
public class CustomObjectList extends JList<GeometricalObject> {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * This list's list model.
	 */
	DrawingObjectListModel listModel;
	
	/**
	 * Constructs a new {@link CustomObjectList} object.
	 * 
	 * @param listModel - this list's list model
	 */
	public CustomObjectList(DrawingObjectListModel listModel) {
		super(listModel);
		this.listModel = listModel;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					GeometricalObject clicked = ((CustomObjectList) e.getSource()).getSelectedValue();
					GeometricalObjectEditor editor = clicked.createGeometricalObjectEditor();
					if(JOptionPane.showConfirmDialog(getParent(), editor,
							"Edit Object", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
						try {
							editor.checkEditing();
							editor.acceptEditing();
						} catch(Exception ex) {
							JOptionPane.showMessageDialog(getParent(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							mouseClicked(e);
						}
					}
				}
			}
		});
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				GeometricalObject selected = CustomObjectList.this.getSelectedValue();
				
				if(e.getKeyCode() == KeyEvent.VK_DELETE) {
					if(selected != null) {
						listModel.remove(selected);
					}
				} else if(e.getKeyCode() == KeyEvent.VK_PLUS) {
					if(selected != null) {
						listModel.changeOrder(selected, 1);
					}
				} else if(e.getKeyCode() == KeyEvent.VK_MINUS) {
					if(selected != null) {
						listModel.changeOrder(selected, -1);
					}
				}
			}
		});
	}
}
