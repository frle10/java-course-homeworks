package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Models objects that are listener's of {@link SingleDocumentModel}
 * in Observer Design Pattern.
 * <p>
 * Those objects track if the document's modified status has been changed
 * or if the model's underlying file path has been updated and than a
 * certain desired action is done.
 * 
 * @author Ivan Skorupan
 */
public interface SingleDocumentListener {

	/**
	 * Called whenever the modified field of some {@link SingleDocumentModel}
	 * is changed.
	 * 
	 * @param model - document whose modified status has changed
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	
	/**
	 * Called whenever the path of the underlying file in <code>model</code>
	 * is updated.
	 * 
	 * @param model - document whose underlying file path has changed
	 */
	void documentFilePathUpdated(SingleDocumentModel model);

}
