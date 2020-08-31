package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This class is an implementation of {@link SingleDocumentModel}.
 * <p>
 * This model initializes a {@link JTextArea} in its constructor and adds
 * a listener to its document model that updates the modified state of this
 * object.
 * 
 * @author Ivan Skorupan
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	/**
	 * Path to file that this model represents.
	 */
	private Path file;
	
	/**
	 * Text component in which this document is shown.
	 */
	private JTextArea textArea;
	
	/**
	 * Modified state of this document.
	 */
	private boolean modified;
	
	/**
	 * List of listeners for this Subject.
	 */
	private List<SingleDocumentListener> listeners = new ArrayList<>();
	
	/**
	 * Constructs a new {@link DefaultSingleDocumentModel} object.
	 * 
	 * @param file - path to file this document corresponds to
	 * @param text - this document model's text
	 */
	public DefaultSingleDocumentModel(Path file, String text) {
		this.file = file;
		this.textArea = new JTextArea(text);
		this.textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(true);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(true);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				setModified(true);
			}
		});;
	}
	
	/**
	 * Helper method that notifies all listeners about the changed modify
	 * status flag.
	 */
	private void fireModified() {
		for(SingleDocumentListener listener : listeners) {
			listener.documentModifyStatusUpdated(this);
		}
	}
	
	/**
	 * Helper method that notifies all listeners about the changed file
	 * path of this model.
	 */
	private void firePath() {
		for(SingleDocumentListener listener : listeners) {
			listener.documentFilePathUpdated(this);
		}
	}
	
	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	@Override
	public Path getFilePath() {
		return file;
	}

	@Override
	public void setFilePath(Path path) {
		file = Objects.requireNonNull(path);
		firePath();
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		fireModified();
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		Objects.requireNonNull(l);
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}
	
}
