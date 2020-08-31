package hr.fer.zemris.java.hw11.jnotepadpp;

import static hr.fer.zemris.java.hw11.jnotepadpp.utility.IconsUtility.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * This class is an implementation of {@link MultipleDocumentModel} interface.
 * <p>
 * Except being a multiple document model, this class is also a {@link JTabbedPane},
 * which means it has the ability to show documents it contains in tabs.
 * <p>
 * Therefore, this class contains some logic for managing tabs and making sure
 * that the current document is correctly updated.
 * 
 * @author Ivan Skorupan
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Cached icon that represents an unmodified document.
	 */
	private final Icon UNMODIFIED_DOCUMENT_ICON;
	
	/**
	 * Cached icon that represents a modified document.
	 */
	private final Icon MODIFIED_DOCUMENT_ICON;
	
	/**
	 * This model's current document.
	 */
	private SingleDocumentModel currentDocument;

	/**
	 * List of all documents in this model.
	 */
	private List<SingleDocumentModel> documents = new ArrayList<>();

	/**
	 * List of listeners in Observer Design Pattern that listen to this
	 * Subject.
	 */
	private List<MultipleDocumentListener> listeners = new ArrayList<>();
	
	/**
	 * Localization provider object.
	 */
	private ILocalizationProvider flp;
	
	/**
	 * Constructs a new {@link DefaultMultipleDocumentModel} object.
	 * 
	 * @param flp - localization provider object
	 */
	public DefaultMultipleDocumentModel(ILocalizationProvider flp) {
		this.flp = flp;
		UNMODIFIED_DOCUMENT_ICON = getIconFrom(UNMODIFIED_FILE_ICON_PATH, getParent());
		MODIFIED_DOCUMENT_ICON = getIconFrom(MODIFIED_FILE_ICON_PATH, getParent());
		
		addChangeListener((e) -> {
			SingleDocumentModel old = currentDocument;
			currentDocument = getSelectedIndex() == -1 ? null : documents.get(getSelectedIndex());
			notifyListeners(old, currentDocument);
		});
		
		flp.addLocalizationListener(this::updateUnnamedTabs);
	}
	
	/**
	 * Updates the names of unnamed documents to be localized.
	 */
	private void updateUnnamedTabs() {
		for(int i = 0; i < getNumberOfDocuments(); i++) {
			if(documents.get(i).getFilePath() == null) {
				setTitleAt(i, flp.getString("unnamed"));
				setToolTipTextAt(i, flp.getString("not_saved"));
			}
		}
	}
	
	/**
	 * Notifies all listener's of this Subject about the change of current document.
	 * 
	 * @param previous - previous version of current document
	 * @param current - new version of current document
	 */
	private void notifyListeners(SingleDocumentModel previous, SingleDocumentModel current) {
		for(MultipleDocumentListener listener : listeners) {
			listener.currentDocumentChanged(previous, current);
		}
	}
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel newDocument = new DefaultSingleDocumentModel(null, "");
		SingleDocumentModel old = currentDocument;
		addDocumentAndShowNewTab(newDocument);
		notifyListeners(old, currentDocument);
		return newDocument;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		Objects.requireNonNull(path);

		for(SingleDocumentModel document : documents) {
			if(path.equals(document.getFilePath())) {
				setSelectedIndex(documents.indexOf(document));
				return document;
			}
		}
		
		if(!Files.isReadable(path)) {
			JOptionPane.showMessageDialog(getParent(),
					flp.getString("no_reading_permission") + "!",
					flp.getString("error"),
					JOptionPane.ERROR_MESSAGE
					);
			return null;
		}
		
		String text = null;
		try {
			text = Files.readString(path);
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(getParent(),
					flp.getString("reading_error") + "!",
					flp.getString("error"),
					JOptionPane.ERROR_MESSAGE
					);
		}

		SingleDocumentModel loadedDocument = new DefaultSingleDocumentModel(path, text);
		SingleDocumentModel old = currentDocument;
		addDocumentAndShowNewTab(loadedDocument);
		notifyListeners(old, currentDocument);
		return loadedDocument;
	}
	
	/**
	 * Adds the given document <code>model</code> to this multiple
	 * document model, updates the current document and creates a
	 * tab for the given document after which it switches to it.
	 * 
	 * @param model - document to be added to this model
	 */
	private void addDocumentAndShowNewTab(SingleDocumentModel model) {
		documents.add(model);
		currentDocument = model;
		String tooltip = model.getFilePath() == null ? flp.getString("not_saved") : model.getFilePath().toString();
		String name = model.getFilePath() == null ? flp.getString("unnamed") : model.getFilePath().getFileName().toString();
		addTab(name, UNMODIFIED_DOCUMENT_ICON, new JScrollPane(model.getTextComponent()), tooltip);
		model.addSingleDocumentListener(new SingleDocumentListener() {
			int index = documents.indexOf(model);
			
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				setIconAt(index, updateTabIcon());
			}
			
			private Icon updateTabIcon() {
				if(currentDocument.isModified()) {
					return MODIFIED_DOCUMENT_ICON;
				} else {
					return UNMODIFIED_DOCUMENT_ICON;
				}
			}

			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				setToolTipTextAt(index, model.getFilePath().toString());
				setTitleAt(index, model.getFilePath().getFileName().toString());
			}
		});
		setSelectedIndex(documents.size() - 1);
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		Objects.requireNonNull(model);
		
		if(newPath != null) {
			for(SingleDocumentModel document : documents) {
				if(document == currentDocument) continue;
				
				if(newPath.equals(document.getFilePath())) {
					JOptionPane.showMessageDialog(getParent(),
							flp.getString("file") + " " + document.getFilePath() + " " + flp.getString("is_opened") + "!",
							flp.getString("error"),
							JOptionPane.ERROR_MESSAGE
							);
					return;
				}
			}
		}
		
		newPath = (newPath != null) ? newPath : model.getFilePath();
		
		String text = model.getTextComponent().getText();
		try {
			Files.writeString(newPath, text);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(getParent(),
					flp.getString("saving_error") + "!",
					flp.getString("error"),
					JOptionPane.ERROR_MESSAGE
					);
		}
		
		model.setFilePath(newPath);
		model.setModified(false);
		notifyListeners(null, currentDocument);
		
		JOptionPane.showMessageDialog(getParent(),
				flp.getString("save_success") + "!",
				flp.getString("info"),
				JOptionPane.INFORMATION_MESSAGE
				);
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		Objects.requireNonNull(model);
		remove(documents.indexOf(model));
		documents.remove(model);
		currentDocument = (getSelectedIndex() == -1) ? null : documents.get(getSelectedIndex());
		notifyListeners(model, currentDocument);
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		Objects.requireNonNull(l);
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		if(index < 0 || index > documents.size() - 1) {
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + documents.size() + ".");
		}
		return documents.get(index);
	}

	@Override
	public void activateDocument(SingleDocumentModel model) {
		if(documents.contains(model)) {
			SingleDocumentModel old = currentDocument;
			currentDocument = model;
			setSelectedIndex(documents.indexOf(model));
			notifyListeners(old, model);
		}
	}

}
