package hr.fer.zemris.java.hw11.jnotepadpp.local;

import static hr.fer.zemris.java.hw11.jnotepadpp.utility.Utility.*;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.utility.CaretInformer;

public class LocalizedRowColSelLabel extends JLabel {
	
	private static final long serialVersionUID = 1L;
	
	private String lineKey;

	private String columnKey;

	private String selectionKey;
	
	/**
	 * A listener that will be added to the given localization
	 * provider.
	 * <p>
	 * This listener will update the label's text whenever the
	 * current language in the application changes.
	 */
	private ILocalizationListener listener;
	
	/**
	 * The provided localization provider.
	 */
	private ILocalizationProvider lp;
	
	/**
	 * Multiple document model this label is working with.
	 */
	private MultipleDocumentModel mdm;
	
	/**
	 * Constructs a new {@link LocalizedLengthLabel} object. 
	 * 
	 * @param key - key to use for getting the localized label text from <code>lp</code>
	 * @param lp - localization provider
	 * @param mdm - multiple document model this label is working with
	 */
	public LocalizedRowColSelLabel(String lineKey, String columnKey, String selectionKey,
			ILocalizationProvider lp, MultipleDocumentModel mdm) {
		this.lineKey = lineKey;
		this.columnKey = columnKey;
		this.selectionKey = selectionKey;
		this.lp = lp;
		this.mdm = mdm;
		this.listener = new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				updateLabel();
			}
		};
		
		lp.addLocalizationListener(listener);
		updateLabel();
	}
	
	/**
	 * Update's the label's text by fetching the localized string
	 * from {@link #lp}.
	 */
	public void updateLabel() {
		CaretInformer ci = getCaretInfo(mdm.getCurrentDocument());
		String line = lp.getString(lineKey) + ": " + ((ci.getLine() == null) ? "" : ci.getLine());
		String column = lp.getString(columnKey) + ": " + ((ci.getColumn() == null) ? "" : ci.getColumn());
		String selection = lp.getString(selectionKey) + ": " + ((ci.getSelectionLength() == null) ? "" : ci.getSelectionLength());
		setText(line + "  " + column + "  " + selection);
	}
	
}
