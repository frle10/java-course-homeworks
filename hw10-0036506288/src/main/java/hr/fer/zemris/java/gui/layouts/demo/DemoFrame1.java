package hr.fer.zemris.java.gui.layouts.demo;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Simple demo program that tests the implemented {@link CalcLayout}.
 * 
 * @author Ivan Skorupan
 */
public class DemoFrame1 extends JFrame {
	
	private static final long serialVersionUID = -5296650607303186995L;
	
	/**
	 * Constructs a new {@link DemoFrame1} object.
	 */
	public DemoFrame1() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initGUI();
		pack();
	}
	
	/*
	public DemoFrame1() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		initGUI();
	}
	*/
	
	/**
	 * Initializes and places GUI components on the window.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(3));
		cp.add(l("tekst 1"), new RCPosition(1,1));
		cp.add(l("tekst 2"), new RCPosition(2,3));
		cp.add(l("tekst stvarno najdulji"), new RCPosition(2,7));
		cp.add(l("tekst kraći"), new RCPosition(4,2));
		cp.add(l("tekst srednji"), new RCPosition(4,5));
		cp.add(l("tekst"), new RCPosition(4,7));
	}
	
	/**
	 * Creates and returns a {@link JLabel} instance
	 * that has yellow background color, is opaque
	 * and has text set to <code>text</code>
	 * 
	 * @param text - label's text
	 * @return new {@link JLabel} with given <code>text</code>, yellow
	 * background color and opaque property set to <code>true</code>
	 */
	private JLabel l(String text) {
		JLabel l = new JLabel(text);
		l.setBackground(Color.YELLOW);
		l.setOpaque(true);
		return l;
	}
	
	/**
	 * Entry point of this demo program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new DemoFrame1().setVisible(true);
		});
	}
}
