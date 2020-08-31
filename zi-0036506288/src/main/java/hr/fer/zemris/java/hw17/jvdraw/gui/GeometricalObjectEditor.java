package hr.fer.zemris.java.hw17.jvdraw.gui;

import javax.swing.JPanel;

/**
 * Models an abstract geometric object editor.
 * <p>
 * Each such editor is dervied from {@link JPanel} and
 * provides methods for checking the edited properties and
 * applying the properties that were input by the user.s
 * 
 * @author Ivan Skorupan
 */
public abstract class GeometricalObjectEditor extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Checks if the values in text fields are valid, otherwise throws
	 * an exception.
	 */
	public abstract void checkEditing();
	
	/**
	 * Applies the input values to the actual object that is being edited.
	 */
	public abstract void acceptEditing();
	
}
