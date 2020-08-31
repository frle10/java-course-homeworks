package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.math.Vector2D;

/**
 * This command changes the turtle's position vector but does not draw a line.
 * 
 * @author Ivan Skorupan
 */
public class SkipCommand implements Command {

	/**
	 * Percentage of effective displacement length to move the turtle by.
	 */
	private double step;

	/**
	 * Constructs a new {@link SkipCommand} object.
	 * 
	 * @param step - effective displacement length percentage to move by
	 */
	public SkipCommand(double step) {
		this.step = step;
	}
	
	@Override
	public void execute(Context ctx, Painter painter) {
		double displacement = step * ctx.getCurrentState().getEffectiveDisplacementLength();
		Vector2D translationVector = ctx.getCurrentState().getDirection().scaled(displacement);
		
		ctx.getCurrentState().getCurrentPosition().translate(translationVector);
	}
	
}
