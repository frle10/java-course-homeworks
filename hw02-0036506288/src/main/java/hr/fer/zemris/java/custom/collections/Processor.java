package hr.fer.zemris.java.custom.collections;

/**
 * This class presents a model of an object capable of performing a
 * certain operation on the passed object.
 * <p>
 * In order for a class to be a processor it needs to extend this class and
 * override the method {@link #process(Object value)} with which it can
 * perform a desired operation on a passed object.
 * 
 * @author Ivan Skorupan
 */
public class Processor {
	
	/**
	 * This method performs any kind of action on the passed <code>value</code> object.
	 * 
	 * @param value - an object to perform an action on
	 */
	public void process(Object value) {
		
	}
	
}
