package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Instances of this class enable the execution of fractal rendering and
 * therefore they offer a stack on which we can push and pop turtle states.
 * 
 * @author Ivan Skorupan
 */
public class Context {
	
	/**
	 * Internal stack used for storing turtle states.
	 */
	private ObjectStack<TurtleState> turtleStateStack = new ObjectStack<>();
	
	/**
	 * Returns the turtle state currently on top of internal stack.
	 * 
	 * @return current turtle state
	 */
	public TurtleState getCurrentState() {
		return turtleStateStack.peek();
	}
	
	/**
	 * Pushes a turtle state on internal stack.
	 * 
	 * @param state - turtle state to push on stack
	 */
	public void pushState(TurtleState state) {
		turtleStateStack.push(state);
	}
	
	/**
	 * Removes the top turtle state from internal stack.
	 */
	public void popState() {
		turtleStateStack.pop();
	}
	
}
