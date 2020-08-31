package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Models a single document that can be shown in a
 * text component. This model is also a Subject in
 * Observer Design Pattern and therefore needs to
 * contain methods for managing its listeners.
 * 
 * @author Ivan Skorupan
 */
public interface SingleDocumentModel {
	
	/**
	 * Get the text component in which this model
	 * is shown.
	 * 
	 * @return text component that contains this model
	 */
	JTextArea getTextComponent();
	
	/**
	 * Get the path to the underlying file in this model.
	 * 
	 * @return path to the file this document represent
	 */
	Path getFilePath();
	
	/**
	 * Set the path to the file this document will represent
	 * 
	 * @param path - path to underlying file for this model
	 */
	void setFilePath(Path path);
	
	/**
	 * Tests if this document has been modified after last saving operation.
	 * 
	 * @return <code>true</code> if this document was modified, <code>false</code> otherwise
	 */
	boolean isModified();
	
	/**
	 * Set the modified state of this document.
	 * <p>
	 * This method should be called with <code>true</code>
	 * parameter when the document is being modified and
	 * <code>false</code> when the document is saved.
	 * 
	 * @param modified - boolean parameter for modified state
	 */
	void setModified(boolean modified);
	
	/**
	 * Adds a listener to this document model.
	 * 
	 * @param l - listener to add to this document model
	 */
	void addSingleDocumentListener(SingleDocumentListener l);
	
	/**
	 * Removes the given listener from this document model.
	 * 
	 * @param l - listener to remove from this document model
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);

}
