package hr.fer.zemris.java.hw07.demo2;

import static java.lang.Math.*;

import java.util.Iterator;

/**
 * A collection of consecutive prime numbers which are only generated
 * when there is a need to get the next prime number in this collection.
 * 
 * @author Ivan Skorupan
 */
public class PrimesCollection implements Iterable<Integer> {
	
	/**
	 * Number of consecutive primes that must be in this collection. 
	 */
	private int numberOfPrimes;
	
	/**
	 * Constructs a new {@link PrimesCollection} object.
	 * <p>
	 * The constructor takes one argument which tells how many consecutive
	 * prime numbers this collection should have (and must have).
	 * 
	 * @param numberOfPrimes - number of consecutive primes that must be in this collection
	 */
	public PrimesCollection(int numberOfPrimes) {
		this.numberOfPrimes = numberOfPrimes;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new PrimesCollectionIterator();
	}
	
	/**
	 * An iterator that can iterate through elements of {@link PrimesCollection}.
	 * <p>
	 * Since the collection this iterator iterates through has no internal storage,
	 * but instead creates elements as needed, the iterator implements a method
	 * that knows how to generate those elements (in other words, it generates consecutive
	 * prime numbers).
	 * <p>
	 * The implementation is made more efficient by remembering what the last generated prime
	 * was, so next time the {@link #next()} method is called, we will keep generating primes
	 * from that point.
	 * <p>
	 * The algorithm for generating primes used is the one that assumes the number is prime
	 * and then tries to find if there are any divisors of that number up to its square
	 * root to disprove the prime property.
	 * 
	 * @author Ivan Skorupan
	 */
	private class PrimesCollectionIterator implements Iterator<Integer> {
		
		/**
		 * NUmber of primes this iterator has already generated.
		 */
		private int primesGenerated;
		
		/**
		 * The last prime this iterator has generated.
		 */
		private int lastGeneratedPrime = 1;
		
		@Override
		public boolean hasNext() {
			return primesGenerated < numberOfPrimes;
		}
		
		@Override
		public Integer next() {
			return nextPrime();
		}
		
		/**
		 * This method generates the next prime number this iterator should return.
		 * <p>
		 * The algorithm used is described in {@link PrimesCollectionIterator}.
		 * 
		 * @return the next consecutive prime for this iterator
		 */
		private Integer nextPrime() {
			// start from the number just after the last generated prime
			int i = lastGeneratedPrime + 1;
			
			while(true) {
				// assume the number we're testing is prime
				boolean isPrime = true;
				
				// try to disprove the number's prime property by trying to find
				// any of its divisors up to its square root
				for(int j = 2; j <= sqrt(i); j++) {
					if(i % j == 0) {
						// if we found a divisor that's not 1 or the number itself,
						// set its prime property to false and exit this loop
						isPrime = false;
						break;
					}
				}
				
				// if the number is prime, update the iterator's fields and return that number
				if(isPrime) {
					primesGenerated++;
					lastGeneratedPrime = i;
					return i;
				}
				
				// keep searching for next consecutive prime number one by one
				i++;
			}
		}
		
	}
	
}
