package hr.fer.zemris.java.hw11.jnotepadpp.local;

import static hr.fer.zemris.java.hw11.jnotepadpp.utility.Utility.*;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw11.jnotepadpp.MultipleDocumentModel;

/**
 * This class is an extension of {@link JLabel} which
 * enables the label to be localized dynamically in
 * the program.
 * <p>
 * In order to be able to do that, the label needs to be
 * a listener of a certain localization provider in
 * Observer Design Pattern.
 * 
 * @author Ivan Skorupan
 */
public class LocalizedLengthLabel extends JLabel {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Key that unlocks the localized string that will be
	 * set as this label's text.
	 */
	private String key;
	
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
	public LocalizedLengthLabel(String key, ILocalizationProvider lp, MultipleDocumentModel mdm) {
		this.key = key;
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
		Integer length = documentLength(mdm.getCurrentDocument());
		setText(lp.getString(key) + ": " + ((length == null) ? "" : length));
	}
	
}
