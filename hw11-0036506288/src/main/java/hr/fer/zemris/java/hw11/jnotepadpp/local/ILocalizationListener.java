package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Models objects that listen for localization changes in the
 * application.
 * <p>
 * The provided method {@link ILocalizationListener#localizationChanged() localizationChanged()}
 * is called every time the current language in the application changes.
 * 
 * @author Ivan Skorupan
 */
public interface ILocalizationListener {
	
	/**
	 * Performs an arbitrary action on objects that are
	 * interested in localization changes.
	 */
	public void localizationChanged();
	
}
