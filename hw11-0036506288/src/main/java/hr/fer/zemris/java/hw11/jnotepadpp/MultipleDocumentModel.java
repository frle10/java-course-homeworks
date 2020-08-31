package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

/**
 * Represents a model capable of holding zero, one or more documents.
 * <p>
 * The model has a concept of current document, which is the one shown
 * to the user and on which the user works.
 * 
 * @author Ivan Skorupan
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	
	/**
	 * Creates a new document.
	 * 
	 * @return the created document model
	 */
	SingleDocumentModel createNewDocument();
	
	/**
	 * Gets the current document in this multiple document
	 * model.
	 * 
	 * @return current document in this model
	 */
	SingleDocumentModel getCurrentDocument();
	
	/**
	 * Loads the specified file from disk and creates a
	 * {@link SingleDocumentModel} for it.
	 * 
	 * @param path - path to the file which should be loaded
	 * @return document model of loaded file
	 */
	SingleDocumentModel loadDocument(Path path);
	
	/**
	 * Saves the given document to disk to the specified
	 * path <code>newPath</code>. If <code>newPath</code>
	 * is <code>null</code>, the file path of <code>model</code>
	 * is taken as the saving destination.
	 * 
	 * @param model - document to save
	 * @param newPath - chosen saving path, can be <code>null</code>
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);
	
	/**
	 * Closes the given document.
	 * 
	 * @param model - document to close
	 */
	void closeDocument(SingleDocumentModel model);
	
	/**
	 * Adds a listener to this multiple document model.
	 * 
	 * @param l - listener to add to this Subject
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Removes the given listener from this multiple document model.
	 * 
	 * @param l - listener to remove from this Subject
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Gets the number of documents found in this multiple document
	 * model.
	 * 
	 * @return number of documents in this model
	 */
	int getNumberOfDocuments();
	
	/**
	 * Gets the document from this model at the specified <code>index</code>.
	 * <p>
	 * The <code>index</code> must not be negative and cannot be greater than
	 * {@link #getNumberOfDocuments()} - 1.
	 * 
	 * @param index - index of document to fetch from this model
	 * @return document at given index
	 */
	SingleDocumentModel getDocument(int index);
	
	/**
	 * Activates the given document <code>model</code>.
	 * 
	 * @param model - {@link SingleDocumentModel} to activate
	 */
	void activateDocument(SingleDocumentModel model);

}
