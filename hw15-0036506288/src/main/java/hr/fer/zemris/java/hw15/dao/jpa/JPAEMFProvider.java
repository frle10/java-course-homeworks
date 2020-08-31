package hr.fer.zemris.java.hw15.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * This class is a provider of a {@link EntityManagerFactory} object.
 * <p>
 * It offers two methods, one setter and one getter for the entity manager
 * factory object contained in this class. However, this is not a singleton since
 * someone else needs to set the object inside this class first.
 * 
 * @author Ivan Skorupan
 */
public class JPAEMFProvider {

	/**
	 * {@link EntityManagerFactory} object this class provides.
	 */
	public static EntityManagerFactory emf;
	
	/**
	 * Getter for internal {@link EntityManagerFactory} object.
	 * 
	 * @return an entity manager factory object
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Setter for internal {@link EntityManagerFactory} object.
	 * 
	 * @param emf - entity manager factory object
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}