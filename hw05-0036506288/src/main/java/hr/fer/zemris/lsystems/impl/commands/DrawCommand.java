package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.math.Vector2D;

/**
 * This command changes the turtle's position vector and draws a line.
 * 
 * @author Ivan Skorupan
 */
public class DrawCommand implements Command {
	
	/**
	 * Percentage of effective displacement length to move the turtle by.
	 */
	private double step;

	/**
	 * Constructs a new {@link DrawCommand} object.
	 * 
	 * @param step - effective displacement length percentage to move by
	 */
	public DrawCommand(double step) {
		this.step = step;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		double displacement = step * ctx.getCurrentState().getEffectiveDisplacementLength();
		Vector2D translationVector = ctx.getCurrentState().getDirection().scaled(displacement);
		
		double x1 = ctx.getCurrentState().getCurrentPosition().getX();
		double y1 = ctx.getCurrentState().getCurrentPosition().getY();
		
		ctx.getCurrentState().getCurrentPosition().translate(translationVector);
		Vector2D currentPos = ctx.getCurrentState().getCurrentPosition();
		Color currentColor = ctx.getCurrentState().getDrawingColor();
		
		painter.drawLine(x1, y1, currentPos.getX(), currentPos.getY(), currentColor, 1f);
	}
	
}
