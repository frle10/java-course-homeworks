package hr.fer.zemris.java.hw05.db;

/**
 * Models objects that can filter database records using arbitrary conditions.
 * 
 * @author Ivan Skorupan
 */
public interface IFilter {
	
	/**
	 * Tests if a given database record can pass this filter using criteria
	 * implemented in this method. 
	 * 
	 * @param record - a record to test
	 * @return <code>true</code> if <code>record</code> passed the filter, <code>false</code> otherwise
	 */
	public boolean accepts(StudentRecord record);
	
}
