package searching.algorithms;

/**
 * Models pairs of format <b>(s, c)</b> where s is a neighboring system's state and
 * c is the cost of transition to that state from the current state.
 * 
 * @author Ivan Skorupan
 * 
 * @param <S> - an object that models one state or configuration of relevant system
 */
public class Transition<S> {
	
	/**
	 * Neighboring state that we can transition to.
	 */
	private S state;
	
	/**
	 * Cost of transition to {@link #state}.
	 */
	private double cost;

	/**
	 * Constructs a new {@link Transition} object.
	 * 
	 * @param state - neighboring system state
	 * @param cost - price to pay for transition into <code>state</code>
	 */
	public Transition(S state, double cost) {
		this.state = state;
		this.cost = cost;
	}

	/**
	 * Getter for s of the pair (s, c).
	 * 
	 * @return state in this pair
	 */
	public S getState() {
		return state;
	}

	/**
	 * Getter for c of the pair (s, c).
	 * 
	 * @return cost in this pair
	 */
	public double getCost() {
		return cost;
	}
	
}
