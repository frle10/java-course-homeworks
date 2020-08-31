package hr.fer.zemris.lsystems.impl.demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Third demo program that showcases the rendering of any kind of fractal as
 * long as we provide a file containing {@link LSystem} configuration.
 * 
 * @author Ivan Skorupan
 */
public class Glavni3 {

	/**
	 * Entry point when running the program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		LSystemViewer.showLSystem(LSystemBuilderImpl::new);
	}

}
