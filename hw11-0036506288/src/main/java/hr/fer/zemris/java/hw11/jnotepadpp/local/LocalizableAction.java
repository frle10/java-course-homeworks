package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Models actions whose properties can be localized based on
 * current application language as tracked by {@link LocalizationProvider}.
 * <p>
 * Currently, properties that are localized are the action's
 * {@link Action#NAME NAME} and {@link Action#SHORT_DESCRIPTION}.
 * 
 * @author Ivan Skorupan
 */
public abstract class LocalizableAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Key used to fetch this action's name translation.
	 */
	private String key;
	
	/**
	 * Key used to fetch this action's short description translation.
	 */
	private String descKey;
	
	/**
	 * A listener that will be added to the given localization
	 * provider.
	 * <p>
	 * This listener will update the action's name and short description
	 * whenever the current language in the application changes.
	 */
	private ILocalizationListener listener;
	
	/**
	 * The provided localization provider.
	 */
	private ILocalizationProvider lp;
	
	/**
	 * Constructs a new {@link LocalizableAction} object.
	 * 
	 * @param key - key to use for getting the localized action name from <code>lp</code>
	 * @param descKey - key to use for getting the localized action short description from <code>lp</code>
	 * @param lp - localization provider
	 */
	public LocalizableAction(String key, String descKey, ILocalizationProvider lp) {
		this.key = key;
		this.descKey = descKey;
		this.lp = lp;
		this.listener = new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				updateAction();
			}
		};
		
		lp.addLocalizationListener(listener);
		updateAction();
	}
	
	/**
	 * Update's the action's name and short description by
	 * fetching the localized string from {@link #lp}.
	 */
	private void updateAction() {
		putValue(NAME, lp.getString(key));
		if(descKey != null) {
			putValue(SHORT_DESCRIPTION, lp.getString(descKey));
		}
	}
	
}
