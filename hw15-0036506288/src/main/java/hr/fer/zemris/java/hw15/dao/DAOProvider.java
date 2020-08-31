package hr.fer.zemris.java.hw15.dao;

import hr.fer.zemris.java.hw15.dao.jpa.JPADAOImpl;

/**
 * A singleton class that knows what needs to be returned as a
 * data persistence subsystem access service provider.
 * <p>
 * Note that, even though the decision here is hard-coded, the
 * name of the class whose instance is being created could have been
 * read dynamically from a configuration file and dynamically loaded.
 * <p>
 * Using that idea, we could change the available implementations
 * without re-compiling the code.
 * 
 * @author Ivan Skorupan
 */
public class DAOProvider {

	/**
	 * Singleton object of this class.
	 */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Getter for an instance of a singleton object of this class.
	 * 
	 * @return an object that encapsulates access to data persistence layer
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}