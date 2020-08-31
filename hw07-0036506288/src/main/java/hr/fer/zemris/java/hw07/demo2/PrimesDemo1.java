package hr.fer.zemris.java.hw07.demo2;

/**
 * A demo class that does a show-case of {@link PrimesCollection} implementation.
 * 
 * @author Ivan Skorupan
 * @see PrimesDemo2
 */
public class PrimesDemo1 {
	
	/**
	 * Entry point of this demo program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(5); // 5: how many of them
		
		for(Integer prime : primesCollection) {
			System.out.println("Got prime: " + prime);
		}

	}

}
