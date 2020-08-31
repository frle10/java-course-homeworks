package hr.fer.zemris.java.hw11.jnotepadpp.utility;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.SingleDocumentModel;

/**
 * This is an adapter class for {@link MultipleDocumentListener} interface.
 * <p>
 * It contains empty implemented methods from the mentioned interface and
 * this is convenient because sometimes we don't need to implement all of
 * these methods.
 * 
 * @author Ivan Skorupan
 */
public class MultipleDocumentListenerAdapter implements MultipleDocumentListener {

	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {}

	@Override
	public void documentAdded(SingleDocumentModel model) {}

	@Override
	public void documentRemoved(SingleDocumentModel model) {}

}
