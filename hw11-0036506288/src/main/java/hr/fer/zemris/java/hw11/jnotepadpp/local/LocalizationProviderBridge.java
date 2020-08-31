package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * This class is a decorator for some other {@link ILocalizationProvider}.
 * <p>
 * It provides two additional methods: {@link #connect()} and {@link #disconnect()}.
 * <p>
 * When this object is asked to resolve a key, it delegates this request to the
 * wrapped (decorated) {@link ILocalizationProvider} object.
 * <p>
 * When user calls {@link #connect()} on it, the method will register an
 * instance of anonymous {@link ILocalizationListener} on the decorated object.
 * When user calls {@link #disconnect()}, this object will be unregistered from decorated object.
 * 
 * @author Ivan Skorupan
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	
	/**
	 * Connected state of this bridge.
	 */
	private boolean connected;
	
	/**
	 * Localization listener that is added to the parent localization
	 * provider when this bridge connects to it.
	 */
	private ILocalizationListener listener;
	
	/**
	 * Parent localization provider, for us that's {@link LocalizationProvider}.
	 */
	private ILocalizationProvider parent;
	
	/**
	 * Last application language read from {@link #parent}.
	 */
	private String language;
	
	/**
	 * Constructs a new {@link LocalizationProviderBridge} object.
	 * 
	 * @param parent - parent localization provider this bridge connects to on demand
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
		this.language = parent.getCurrentLanguage();
		this.listener = new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				fire();
			}
		};
	}
	
	/**
	 * Disconnects this bridge from {@link #parent} and updates the
	 * connected state of this object.
	 */
	public void disconnect() {
		language = parent.getCurrentLanguage();
		parent.removeLocalizationListener(listener);
		connected = false;
	}
	
	/**
	 * Connects this bridge to {@link #parent}, notifies all listeners
	 * about the language change in case it happened and updates
	 * the current language and connected state.
	 */
	public void connect() {
		if(!connected) {
			if(!language.equals(parent.getCurrentLanguage())) {
				fire();
				language = parent.getCurrentLanguage();
			}
			
			parent.addLocalizationListener(listener);
			connected = true;
		}
	}
	
	@Override
	public String getCurrentLanguage() {
		return language;
	}

	@Override
	public String getString(String key) {
		return parent.getString(key);
	}
	
}
