package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * This command changes the turtle's drawing color.
 * 
 * @author Ivan Skorupan
 */
public class ColorCommand implements Command {

	/**
	 * Color to change to.
	 */
	private Color color;
	
	/**
	 * Constructs a new {@link ColorCommand} object.
	 * 
	 * @param color - color to change to
	 */
	public ColorCommand(Color color) {
		this.color = color;
	}
	
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setDrawingColor(color);
	}

}
