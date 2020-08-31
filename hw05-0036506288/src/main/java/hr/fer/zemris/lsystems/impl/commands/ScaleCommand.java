package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * This command changes the turtle's effective displacement length.
 * 
 * @author Ivan Skorupan
 */
public class ScaleCommand implements Command {
	
	/**
	 * Factor by which to multiply current effective displacement length.
	 */
	private double factor;

	/**
	 * Constructs a new {@link ScaleCommand} object.
	 * 
	 * @param factor - factor by which to multiply current effective displacement length
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		double currentEffectiveDisplacementLength = ctx.getCurrentState().getEffectiveDisplacementLength();
		ctx.getCurrentState().setEffectiveDisplacementLength(currentEffectiveDisplacementLength * factor);
	}
	
}
