package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;

/**
 * This class is an extension of {@link JMenu} which
 * enables the menu to be localized dynamically in
 * the program.
 * <p>
 * In order to be able to do that, the menu needs to be
 * a listener of a certain localization provider in
 * Observer Design Pattern.
 * 
 * @author Ivan Skorupan
 */
public class LJMenu extends JMenu {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Key that unlocks the localized string that will be
	 * set as this menu's title.
	 */
	private String key;
	
	/**
	 * A listener that will be added to the given localization
	 * provider.
	 * <p>
	 * This listener will update the menu's title whenever the
	 * current language in the application changes.
	 */
	private ILocalizationListener listener;
	
	/**
	 * The provided localization provider.
	 */
	private ILocalizationProvider lp;
	
	/**
	 * Constructs a new {@link LJMenu} object. 
	 * 
	 * @param key - key to use for getting the localized menu text from <code>lp</code>
	 * @param lp - localization provider
	 */
	public LJMenu(String key, ILocalizationProvider lp) {
		this.key = key;
		this.lp = lp;
		this.listener = new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				updateMenu();
			}
		};
		
		lp.addLocalizationListener(listener);
		updateMenu();
	}
	
	/**
	 * Update's the menu's text by fetching the localized string
	 * from {@link #lp}.
	 */
	private void updateMenu() {
		setText(lp.getString(key));
	}
	
}
