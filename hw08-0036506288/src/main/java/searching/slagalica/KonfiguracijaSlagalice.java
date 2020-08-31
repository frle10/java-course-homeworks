package searching.slagalica;

import java.util.Arrays;

/**
 * Models one configuration (state) of the puzzle described
 * in problem 2 of the 8th homework.
 * 
 * @author Ivan Skorupan
 */
public class KonfiguracijaSlagalice {
	
	/**
	 * The actual puzzle state as an array of integers in certain order.
	 */
	private int[] slagalica;

	/**
	 * COnstructs a new {@link KonfiguracijaSlagalice} object.
	 * 
	 * @param slagalica - a specific configuration of integer slots in the puzzle
	 * (one concrete puzzle state)
	 */
	public KonfiguracijaSlagalice(int[] slagalica) {
		this.slagalica = slagalica;
	}

	/**
	 * Returns a copy of internal puzzle state array.
	 * 
	 * @return copy of internal puzzle state array
	 */
	public int[] getPolje() {
		return Arrays.copyOf(slagalica, slagalica.length);
	}
	
	/**
	 * Returns the index at which the empty slot is in the puzzle.
	 * <p>
	 * This index is equal to the index of digit 0 in the internal state array.
	 * 
	 * @return current index of space in the puzzle
	 */
	public int indexOfSpace() {
		for(int i = 0; i < slagalica.length; i++) {
			if(slagalica[i] == 0) {
				return i;
			}
		}
		
		return 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(slagalica);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof KonfiguracijaSlagalice))
			return false;
		KonfiguracijaSlagalice other = (KonfiguracijaSlagalice) obj;
		return Arrays.equals(slagalica, other.slagalica);
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		
		for(int i = 0; i < slagalica.length; i++) {
			text.append(slagalica[i] == 0 ? "*" : slagalica[i]);
			text.append(i % 3 == 2 ? '\n' : ' ');
		}
		
		return text.deleteCharAt(text.length() - 1).toString();
	}
	
}
