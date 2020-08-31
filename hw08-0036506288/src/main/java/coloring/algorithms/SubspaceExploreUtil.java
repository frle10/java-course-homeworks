package coloring.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Contains <code>public static</code> methods that implement
 * three different algorithms for walking through a sub-space of states.
 * <p>
 * The algorithms implemented are:
 * <ul>
 * 	<li>bfs</li>
 * 	<li>dfs</li>
 * 	<li>bfsv</li>
 * </ul>
 * 
 * @author Ivan Skorupan
 */
public class SubspaceExploreUtil {

	/**
	 * Implements a breadth first search algorithm for walking through state sub-space.
	 * <p>
	 * This algorithm's property is that the state to be examined is taken from the beginning of the list,
	 * while the new states are added to the end of the list.
	 * 
	 * @param s0 - a supplier of the starting state
	 * @param process - a consumer that processes the state
	 * @param succ - a function that returns all adjacent states of the current state
	 * @param acceptable - a predicate that tests if the current state is acceptable or not
	 */
	public static <S> void bfs(Supplier<S> s0, Consumer<S> process, Function<S,List<S>> succ, Predicate<S> acceptable) {
		List<S> toVisit = new LinkedList<>();
		toVisit.add(s0.get());

		while(!toVisit.isEmpty()) {
			S current = toVisit.remove(0);
			if(!acceptable.test(current)) continue;
			process.accept(current);
			toVisit.addAll(succ.apply(current));
		}
	}

	/**
	 * Implements a depth first search algorithm for walking through state sub-space.
	 * <p>
	 * This algorithm's property is that the state to be examined is taken from the beginning of the list,
	 * while the new states are added also to the beginning of the list.
	 * 
	 * @param s0 - a supplier of the starting state
	 * @param process - a consumer that processes the state
	 * @param succ - a function that returns all adjacent states of the current state
	 * @param acceptable - a predicate that tests if the current state is acceptable or not
	 */
	public static <S> void dfs(Supplier<S> s0, Consumer<S> process, Function<S,List<S>> succ, Predicate<S> acceptable) {
		List<S> toVisit = new LinkedList<>();
		toVisit.add(s0.get());

		while(!toVisit.isEmpty()) {
			S current = toVisit.remove(0);
			if(!acceptable.test(current)) continue;
			process.accept(current);
			toVisit.addAll(0, succ.apply(current));
		}
	}
	
	/**
	 * Implements an improved breadth first search algorithm for walking through state sub-space.
	 * <p>
	 * This algorithm is practically identical to {@link #bfs(Supplier, Consumer, Function, Predicate) bfs()}
	 * algorithm, but it is more efficient because only the states that haven't already been visited are
	 * added to the list of states that still need to be visited.
	 * 
	 * @param s0 - a supplier of the starting state
	 * @param process - a consumer that processes the state
	 * @param succ - a function that returns all adjacent states of the current state
	 * @param acceptable - a predicate that tests if the current state is acceptable or not
	 */
	public static <S> void bfsv(Supplier<S> s0, Consumer<S> process, Function<S,List<S>> succ, Predicate<S> acceptable) {
		List<S> toVisit = new LinkedList<>();
		Set<S> visited = new HashSet<>();
		toVisit.add(s0.get());
		visited.add(s0.get());

		while(!toVisit.isEmpty()) {
			S current = toVisit.remove(0);
			if(!acceptable.test(current)) continue;
			process.accept(current);
			List<S> children = succ.apply(current);
			children.removeAll(visited);
			toVisit.addAll(children);
			visited.addAll(children);
		}
	}
	
}
