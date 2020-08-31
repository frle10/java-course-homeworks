package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Models objects that can provide string translations for
 * a given key.
 * <p>
 * The key is used to fetch the translation value from a map
 * derived from a .properties file.
 * <p>
 * Objects of this kind are also Subjects in Observer Design
 * Pattern and as such need to provide methods for managing
 * their listeners.
 * 
 * @author Ivan Skorupan
 */
public interface ILocalizationProvider {
	
	/**
	 * Adds a localization listener to this localization provider.
	 * 
	 * @param l - localization listener to add to this Subject
	 */
	public void addLocalizationListener(ILocalizationListener l);
	
	/**
	 * Removes the given localization listener from this localization provider.
	 * 
	 * @param l - localization listener to remove from this Subject
	 */
	public void removeLocalizationListener(ILocalizationListener l);
	
	/**
	 * The core method in this interface.
	 * <p>
	 * This method fetches the translation for given <code>key</code>.
	 * 
	 * @param key - key to use for getting the translation from the translation map
	 * @return translation for given <code>key</code>
	 */
	public String getString(String key);
	
	/**
	 * Returns the current application language acronym.
	 * 
	 * @return current application language
	 */
	public String getCurrentLanguage();
	
}
