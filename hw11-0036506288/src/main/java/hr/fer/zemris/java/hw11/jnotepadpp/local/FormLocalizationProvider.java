package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * Models objects that can connect a {@link JFrame} window
 * to the {@link LocalizationProvider} thorough a window
 * listener.
 * <p>
 * The window listener is added in the constructor and it
 * connects the window to {@link LocalizationProvider} when
 * it is opened and disconnects it when it is closed.
 * 
 * @author Ivan Skorupan
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {
	
	/**
	 * Constructs a new {@link FormLocalizationProvider} object.
	 * <p>
	 * The constructor adds a window listener to given <code>window</code>
	 * that connects it to the {@link LocalizationProvider} when it's opened
	 * and disconnects it when it's closed.
	 * 
	 * @param parent - parent localization provider
	 * @param window - reference to a {@link JFrame} that needs to have a
	 * proper {@link WindowListener} added to connect to the {@link LocalizationProvider}
	 */
	public FormLocalizationProvider(ILocalizationProvider parent, JFrame window) {
		super(parent);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
			}
		});
	}
	
}
