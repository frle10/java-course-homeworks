package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Models an abstract localization provider.
 * <p>
 * This class implements only the listener managing methods
 * from {@link ILocalizationProvider} interface.
 * <p>
 * The methods for getting the translation string and current
 * language still need to be implemented by an actual
 * non-abstract localization provider.
 * 
 * @author Ivan Skorupan
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	
	/**
	 * List of localization listeners that track this localization
	 * provider's current language.
	 */
	private List<ILocalizationListener> listeners = new ArrayList<>();
	
	/**
	 * Empty constructor that just constructs a new {@link AbstractLocalizationProvider}
	 * object.
	 */
	public AbstractLocalizationProvider() {}

	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		listeners.add(Objects.requireNonNull(l));
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies all listeners about the change in application
	 * localization.
	 */
	public void fire() {
		for(ILocalizationListener listener : listeners) {
			listener.localizationChanged();
		}
	}
	
}
