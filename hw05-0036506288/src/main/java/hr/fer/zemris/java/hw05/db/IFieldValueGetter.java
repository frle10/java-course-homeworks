package hr.fer.zemris.java.hw05.db;

/**
 * A strategy which is responsible for obtaining a requested field
 * value from given {@link StudentRecord}.
 * 
 * @author Ivan Skorupan
 */
public interface IFieldValueGetter {
	
	/**
	 * Gets a desired field from given {@link StudentRecord} object.
	 * 
	 * @param record - student record to retrieve the field from
	 * @return value of this record's desired field
	 */
	public String get(StudentRecord record);
	
}
