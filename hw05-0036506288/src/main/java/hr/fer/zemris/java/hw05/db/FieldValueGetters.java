package hr.fer.zemris.java.hw05.db;

/**
 * Implements concrete strategies for each field of {@link StudentRecord} class.
 * 
 * @author Ivan Skorupan
 */
public class FieldValueGetters {
	
	/**
	 * Returns student's first name.
	 */
	public static final IFieldValueGetter FIRST_NAME = r -> r.getFirstName();
	
	/**
	 * Returns student's last name.
	 */
	public static final IFieldValueGetter LAST_NAME = r -> r.getLastName();
	
	/**
	 * Returns student's jmbag.
	 */
	public static final IFieldValueGetter JMBAG = r -> r.getJmbag();
	
}
