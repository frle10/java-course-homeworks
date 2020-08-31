package hr.fer.zemris.java.hw07.demo2;

/**
 * A demo class that does a show-case of {@link PrimesCollection} implementation.
 * 
 * @author Ivan Skorupan
 * @see PrimesDemo1
 */
public class PrimesDemo2 {

	/**
	 * Entry point of this demo program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		PrimesCollection primesCollection = new PrimesCollection(2);
		
		for(Integer prime : primesCollection) {
			for(Integer prime2 : primesCollection) {
				System.out.println("Got prime pair: " + prime + ", " + prime2);
			}
		}
	}

}
