package hr.fer.zemris.java.gui.prim;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * This list model incrementally generates prime numbers.
 * <p>
 * On each call to the {@link #next()} method, the new prime
 * number is added to the list and is set as the last generated
 * prime number.
 * 
 * @author Ivan Skorupan
 */
public class PrimListModel implements ListModel<Integer> {
	
	/**
	 * List of all prime numbers generated by this model.
	 */
	private List<Integer> primes = new ArrayList<>();
	
	/**
	 * List of this model's listeners.
	 */
	private List<ListDataListener> listeners = new ArrayList<>();
	
	/**
	 * Last generated prime number by this model.
	 */
	private int currentPrime = 1;
	
	/**
	 * Constructs a new {@link PrimListModel}.
	 */
	public PrimListModel() {
		primes.add(currentPrime);
	}
	
	/**
	 * Generates the next prime number in this model
	 * and notifies all listeners about the model change.
	 */
	public void next() {
		findNextPrime();
		notifyListeners();
	}
	
	/**
	 * Generates the next prime after this model's
	 * last generated prime.
	 */
	private void findNextPrime() {
		int currentNumber = currentPrime + 1;
		while(true) {
			boolean isPrime = true;
			for(int i = 2; i <= sqrt(currentNumber); i++) {
				if(currentNumber % i == 0) {
					isPrime = false;
				}
			}
			
			if(isPrime) {
				currentPrime = currentNumber;
				primes.add(currentPrime);
				break;
			}
			currentNumber++;
		}
	}
	
	/**
	 * Notifies all listeners about the change that ocurred
	 * inside this model.
	 */
	private void notifyListeners() {
		int pos = primes.size() - 1;
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, pos, pos);
		for(ListDataListener listener : listeners) {
			listener.contentsChanged(event);
		}
	}
	
	@Override
	public int getSize() {
		return primes.size();
	}

	@Override
	public Integer getElementAt(int index) {
		if(index < 0 || index > primes.size() - 1) {
			throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for size " + primes.size() + ".");
		}
		
		return primes.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(Objects.requireNonNull(l));
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}
	
}
