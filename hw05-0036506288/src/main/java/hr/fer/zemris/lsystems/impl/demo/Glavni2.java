package hr.fer.zemris.lsystems.impl.demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Second demo program that showcases the rendering of a Koch curve fractal.
 * 
 * @author Ivan Skorupan
 */
public class Glavni2 {

	/**
	 * Entry point when running the program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		LSystemViewer.showLSystem(createKochCurve2(LSystemBuilderImpl::new));
	}
	
	/**
	 * Generates and returns a Lindermayer system that can render a Koch curve.
	 * <p>
	 * This version of this method uses a special method to construct an {@link LSystem}
	 * that can parse the configuration from text.
	 * 
	 * @param provider - reference to Lindermayer system builder provider
	 * @return a fully configured {@link LSystem}
	 */
	private static LSystem createKochCurve2(LSystemBuilderProvider provider) {
		String[] data = new String[] {
				"origin 0.05 0.4",
				"angle 0",
				"unitLength 0.9",
				"unitLengthDegreeScaler 1.0 / 3.0",
				"",
				"command F draw 1",
				"command + rotate 60",
				"command - rotate -60",
				"",
				"axiom F",
				"",
				"production F F+F--F+F"
		};
		return provider.createLSystemBuilder().configureFromText(data).build();
	}
	
}
