package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

/**
 * This class models an object that can provide localized strings
 * to all program components that contain some kind of text or title.
 * <p>
 * We would like to have only one instance of this class because it is
 * unnecessary to have more than one {@link LocalizationProvider}.
 * <p>
 * Therefore, this class utilizes the Singleton Design Pattern.
 * 
 * @author Ivan Skorupan
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
	
	/**
	 * Fully qualified class name syntax for getting the localization .properties files.
	 */
	private static final String TRANSLATIONS = "hr.fer.zemris.java.hw11.jnotepadpp.local.translations";
	
	/**
	 * Current application language.
	 */
	private String language;
	
	/**
	 * A resource bundle object used for loading the translations.
	 */
	private ResourceBundle bundle;
	
	/**
	 * A single and only instance of this class.
	 */
	private static final LocalizationProvider instance = new LocalizationProvider();
	
	/**
	 * Constructs a new {@link LocalizationProvider} object.
	 * <p>
	 * This constructor is private because this class utilizes the Singleton
	 * design pattern.
	 */
	private LocalizationProvider() {
		this.language = "en";
		this.bundle = ResourceBundle.getBundle(TRANSLATIONS, Locale.forLanguageTag("en"));
	}
	
	/**
	 * Gets this class's only instance. The same instance is always returned.
	 * 
	 * @return an instance of this class
	 */
	public static LocalizationProvider getInstance() {
		return instance;
	}

	/**
	 * Sets the current application language. If the given <code>language</code>
	 * is not different from the current one, this method does nothing.
	 * <p>
	 * If the given <code>language</code> is different from current language,
	 * then the {@link #language} field is updates, the new resource bundle is
	 * acquired and all listeners that are interested in current application
	 * language are notified about the change.
	 * 
	 * @param language
	 */
	public void setLanguage(String language) {
		if(!this.language.equals(language)) {
			this.language = language;
			bundle = ResourceBundle.getBundle(TRANSLATIONS, Locale.forLanguageTag(language));
			JOptionPane.setDefaultLocale(Locale.forLanguageTag(language));
			fire();
		}
	}
	
	@Override
	public String getCurrentLanguage() {
		return language;
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}
	
}
