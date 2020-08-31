package hr.fer.zemris.java.custom.collections;

/**
 * Models an object that takes another object and tests whether or not the passed
 * object is acceptable.
 * 
 * @author Ivan Skorupan
 */
public interface Tester {
	
	/**
	 * Tests if the passed object is acceptable or not.
	 * 
	 * @param obj - an object to be tested
	 * @return <code>true</code> if <code>obj</code> is accepted, <code>false</code> otherwise
	 */
	boolean test(Object obj);
	
}
