package hr.fer.zemris.java.hw11.jnotepadpp.utility;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;

/**
 * This class provides <code>public static</code> methods
 * for working with image icons that are used in
 * {@link JNotepadPP} as well as string constants
 * representing paths to all icons used in the
 * mentioned program.
 * 
 * @author Ivan Skorupan
 */
public class IconsUtility {
	
	/**
	 * Relative path to modified file icon.
	 */
	public static final String MODIFIED_FILE_ICON_PATH = "../icons/red_diskette_icon.png";
	
	/**
	 * Relative path to unmodified file icon.
	 */
	public static final String UNMODIFIED_FILE_ICON_PATH = "../icons/blue_diskette_icon.png";
	
	/**
	 * Gets the icon at specified <code>path</code>.
	 * 
	 * @param path - relative path to the icon
	 * @return an {@link ImageIcon} object representing the icon
	 */
	public static Icon getIconFrom(String path, Component parent) {
		InputStream is = IconsUtility.class.getResourceAsStream(path);
		if(is == null) {
			
		}
		
		BufferedImage buffIcon = null;
		try {
			buffIcon = ImageIO.read(is);
			is.close();
		} catch (IOException e) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(parent,
						"There was a problem while reading the icon!",
						"Error",
						JOptionPane.ERROR_MESSAGE
				);
			});
			return null;
		}
		
		Image icon = buffIcon.getScaledInstance(16, 16, Image.SCALE_DEFAULT);
		return new ImageIcon(icon);
	}
	
}
