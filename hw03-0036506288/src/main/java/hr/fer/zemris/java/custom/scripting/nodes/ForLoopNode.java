package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * A node representing a single for-loop construct.
 * 
 * @author Ivan Skorupan
 */
public class ForLoopNode extends Node {
	
	/**
	 * A for loop variable to iterate through.
	 */
	private ElementVariable variable;
	
	/**
	 * The starting value from which to iterate.
	 */
	private Element startExpression;
	
	/**
	 * The ending value to which to iterate.
	 */
	private Element endExpression;
	
	/**
	 * The increment of {@link #variable} after each iteration of for loop.
	 * <p>
	 * Can be <code>null</code>.
	 */
	private Element stepExpression;
	
	/**
	 * Constructs a new {@link ForLoopNode} object.
	 * <p>
	 * Only the {@link #stepExpression} field can be <code>null</code>.
	 * 
	 * @param variable - variable through which to iterate
	 * @param startExpression - starting iteration value
	 * @param endExpression - ending iteration value
	 * @param stepExpression - increment value after each iteration
	 * @throws NullPointerException if any parameter is <code>null</code> except {@link #stepExpression}
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
		this.variable = Objects.requireNonNull(variable);
		this.startExpression = Objects.requireNonNull(startExpression);
		this.endExpression = Objects.requireNonNull(endExpression);
		this.stepExpression = stepExpression;
	}
	
	/**
	 * Getter for {@link #variable} field.
	 * 
	 * @return iteration variable of this for-loop
	 */
	public ElementVariable getVariable() {
		return variable;
	}
	
	/**
	 * Getter for {@link #startExpression} field.
	 * 
	 * @return starting value of this for-loop
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Getter for {@link #endExpression} field.
	 * 
	 * @return ending value of this for-loop
	 */
	public Element getEndExpression() {
		return endExpression;
	}
	
	/**
	 * Getter for {@link #stepExpression} field.
	 * 
	 * @return increment value after each iteration of this for-loop
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	@Override
	public String toString() {
		StringBuilder forLoop = new StringBuilder();
		
		forLoop.append("{$FOR ");
		forLoop.append(variable.asText() + " " + startExpression.asText() + " " + endExpression.asText());
		
		if(stepExpression != null) {
			forLoop.append(" " + stepExpression.asText());
		}
		
		forLoop.append(" $}");
		forLoop.append(super.toString());
		forLoop.append("{$END$}");
		
		return forLoop.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(endExpression, startExpression, stepExpression, variable);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ForLoopNode))
			return false;
		ForLoopNode other = (ForLoopNode) obj;
		return  super.equals(other)
				&& Objects.equals(endExpression, other.endExpression)
				&& Objects.equals(startExpression, other.startExpression)
				&& Objects.equals(stepExpression, other.stepExpression) && Objects.equals(variable, other.variable);
	}
	
}
