package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * This command changes the turtle's direction vector (rotates it).
 * 
 * @author Ivan Skorupan
 */
public class RotateCommand implements Command {

	/**
	 * Angle by which to rotate the turtle, in degrees.
	 */
	private double angle;
	
	/**
	 * Constructs a new {@link RotateCommand} object.
	 * 
	 * @param angle - angle by which to rotate
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().getDirection().rotate(angleInRadians());
	}
	
	/**
	 * Returns {@link #angle} in radians.
	 * 
	 * @return {@link #angle} in radians
	 */
	private double angleInRadians() {
		return angle * (Math.PI / 180.);
	}
	
}
