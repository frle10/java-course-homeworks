package searching.algorithms;

/**
 * Models a node that contains information about the current state of the system,
 * the parent node (which would also be the parent state from which we transitioned
 * into this state) and the cost that we paid by now to come into this state.
 * 
 * @author Ivan Skorupan
 *
 * @param <S> - an object that models one state or configuration of relevant system
 */
public class Node<S> {
	
	/**
	 * Parent node.
	 */
	private Node<S> parent;
	
	/**
	 * Current system state.
	 */
	private S state;
	
	/**
	 * Price paid by now for getting to this state.
	 */
	private double cost;

	/**
	 * Constructs a new {@link Node} object.
	 * 
	 * @param parent - parent node
	 * @param state - current state
	 * @param cost - price paid by now for getting to this state
	 */
	public Node(Node<S> parent, S state, double cost) {
		this.parent = parent;
		this.state = state;
		this.cost = cost;
	}
	
	/**
	 * Getter for current state.
	 * 
	 * @return current system state
	 */
	public S getState() {
		return state;
	}
	
	/**
	 * Getter for cost.
	 * 
	 * @return cost of getting to this state
	 */
	public double getCost() {
		return cost;
	}
	
	/**
	 * Getter for parent node.
	 * 
	 * @return parent node
	 */
	public Node<S> getParent() {
		return parent;
	}
	
}
