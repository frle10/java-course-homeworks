package searching.slagalica;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import searching.algorithms.Transition;

/**
 * An implementation of all three interfaces needed for execution
 * of the mathematical model of the solution to the puzzle problem.
 * <p>
 * This class also contains an extra field which represents the initial
 * puzzle configuration.
 * 
 * @author Ivan Skorupan
 */
public class Slagalica implements Supplier<KonfiguracijaSlagalice>,
								Function<KonfiguracijaSlagalice, List<Transition<KonfiguracijaSlagalice>>>,
								Predicate<KonfiguracijaSlagalice> {
	
	/**
	 * Initial configuration of the puzzle.
	 */
	private KonfiguracijaSlagalice initialConfiguration;
	
	/**
	 * Cost of one swap of empty slot and neighboring slot.
	 */
	private static final int SWAP_COST = 1;
	
	/**
	 * Constructs a new {@link Slagalica} object.
	 * 
	 * @param initialConfiguration - initial puzzle configuration
	 * @throws NullPointerException if <code>initialConfiguration</code> is <code>null</code>
	 */
	public Slagalica(KonfiguracijaSlagalice initialConfiguration) {
		this.initialConfiguration = Objects.requireNonNull(initialConfiguration);
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Tests whether the provided puzzle state is acceptable or not.
	 * 
	 * @param t - a puzzle state
	 * @return <code>true</code> if <code>t</code> is acceptable, <code>false</code> otherwise
	 * @throws NullPointerException if <code>t</code> is <code>null</code>
	 */
	@Override
	public boolean test(KonfiguracijaSlagalice t) {
		Objects.requireNonNull(t);
		int[] goal = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 0};
		return Arrays.equals(goal, t.getPolje());
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Finds all the neighboring slots of the space slot and returns all possible
	 * transitions in a list.
	 * 
	 * @param t - a puzzle state
	 * @return list of possible transitions from given configuration
	 * @throws NullPointerException if <code>t</code> is <code>null</code>
	 */
	@Override
	public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice t) {
		Objects.requireNonNull(t);
		List<Transition<KonfiguracijaSlagalice>> transitions = new LinkedList<>();
		
		Swapper swapper = new Swapper(t);
		List<KonfiguracijaSlagalice> swaps = swapper.getSwaps();
		for(KonfiguracijaSlagalice swap : swaps) {
			transitions.add(new Transition<KonfiguracijaSlagalice>(swap, SWAP_COST));
		}
		
		return transitions;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Returns the initial puzzle configuration state.
	 */
	@Override
	public KonfiguracijaSlagalice get() {
		return initialConfiguration;
	}
	
}
