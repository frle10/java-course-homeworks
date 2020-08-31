package hr.fer.zemris.java.hw17.jvdraw.gui;

import java.awt.Color;
import java.util.Objects;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw17.jvdraw.interfaces.ColorChangeListener;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.IColorProvider;

/**
 * Models a simple status bar for our application which holds the information
 * about currently selected foreground and fill color.
 * 
 * @author Ivan Skorupan
 */
public class StatusBar extends JLabel implements ColorChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Foreground color information provider object.
	 */
	private IColorProvider fgColorProvider;
	
	/**
	 * Background color information provider object.
	 */
	private IColorProvider bgColorProvider;
	
	/**
	 * Constructs a new {@link StatusBar} object with provided
	 * {@link IColorProvider} objects for foreground and background
	 * color information fetching.
	 * 
	 * @param fgColorProvider - foreground color information provider
	 * @param bgColorProvider - background color information provider
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public StatusBar(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		this.fgColorProvider = Objects.requireNonNull(fgColorProvider);
		this.bgColorProvider = Objects.requireNonNull(bgColorProvider);

		this.fgColorProvider.addColorChangeListener(this);
		this.bgColorProvider.addColorChangeListener(this);
		newColorSelected(null, null, null);
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		Color fgColor = fgColorProvider.getCurrentColor();
		Color bgColor = bgColorProvider.getCurrentColor();
		
		String foregroundRGB = "(" + fgColor.getRed() + ", " + fgColor.getGreen() + ", " + fgColor.getBlue() + ")";
		String backgroundRGB = "(" + bgColor.getRed() + ", " + bgColor.getGreen() + ", " + bgColor.getBlue() + ")";
		
		setText("Foreground color: " + foregroundRGB + ", background color: " + backgroundRGB + ".");
	}
	
}
