package searching.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Contains <code>public static</code> methods that implement
 * algorithms for exploring the state sub-space of relevant system.
 * 
 * @author Ivan Skorupan
 */
public class SearchUtil {

	/**
	 * Implements a breadth first search algorithm for walking through state sub-space.
	 * <p>
	 * This algorithm's property is that the state to be examined is taken from the beginning of the list,
	 * while the new states are added to the end of the list.
	 * 
	 * @param s0 - a supplier of the starting state
	 * @param succ - a function that returns all possible state transitions from the current state
	 * @param goal - a predicate that tests if the current state is acceptable or not
	 * @return a node that contains the acceptable state or <code>null</code> if somehow the process was terminated
	 */
	public static <S> Node<S> bfs(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal){
		List<Node<S>> toVisit = new LinkedList<>();
		toVisit.add(new Node<S>(null, s0.get(), 0));
		
		while(!toVisit.isEmpty()) {
			Node<S> currentNode = toVisit.remove(0);
			if(goal.test(currentNode.getState())) return currentNode;
			List<Transition<S>> transitions = succ.apply(currentNode.getState());
			for(Transition<S> transition : transitions) {
				toVisit.add(new Node<S>(currentNode, transition.getState(), currentNode.getCost() + 1));
			}
		}
		
		return null;
	}
	
	/**
	 * Implements an improved breadth first search algorithm for walking through state sub-space.
	 * <p>
	 * This algorithm is practically identical to {@link #bfs(Supplier, Function, Predicate) bfs()}
	 * algorithm, but it is more efficient because only the states that haven't already been visited are
	 * added to the list of states that still need to be visited.
	 * 
	 * @param s0 - a supplier of the starting state
	 * @param succ - a function that returns all possible state transitions from the current state
	 * @param goal - a predicate that tests if the current state is acceptable or not
	 * @return a node that contains the acceptable state or <code>null</code> if somehow the process was terminated
	 */
	public static <S> Node<S> bfsv(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal){
		List<Node<S>> toVisit = new LinkedList<>();
		Set<S> visited = new HashSet<>();
		toVisit.add(new Node<S>(null, s0.get(), 0));
		visited.add(s0.get());
		
		while(!toVisit.isEmpty()) {
			Node<S> currentNode = toVisit.remove(0);
			if(goal.test(currentNode.getState())) return currentNode;
			List<Transition<S>> transitions = succ.apply(currentNode.getState());
			for(Transition<S> transition : transitions) {
				if(!visited.contains(transition.getState())) {
					toVisit.add(new Node<S>(currentNode, transition.getState(), currentNode.getCost() + 1));
					visited.add(transition.getState());
				}
			}
		}
		
		return null;
	}
	
}
