package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Models a command that the turtle must be able to execute.
 * 
 * @author Ivan Skorupan
 */
public interface Command {
	
	/**
	 * Does an arbitrary action on a turtle or its properties.
	 * 
	 * @param ctx - context reference
	 * @param painter - a reference to object that can draw lines on screen
	 */
	void execute(Context ctx, Painter painter);
	
}
