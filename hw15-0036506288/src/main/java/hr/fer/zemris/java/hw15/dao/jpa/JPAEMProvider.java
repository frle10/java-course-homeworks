package hr.fer.zemris.java.hw15.dao.jpa;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.hw15.dao.DAOException;

/**
 * This class is a {@link EntityManager} object provider.
 * <p>
 * It stores entity managers in a {@link ThreadLocal} map, so that
 * every time a thread asks for such an object, it will get its own.
 * 
 * @author Ivan Skorupan
 */
public class JPAEMProvider {

	private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();

	/**
	 * Fetches current thread's entity manager from {@link ThreadLocal} map.
	 * 
	 * @return current thread's entity manager object
	 */
	public static EntityManager getEntityManager() {
		EntityManager em = locals.get();
		if(em==null) {
			em = JPAEMFProvider.getEmf().createEntityManager();
			em.getTransaction().begin();
			locals.set(em);
		}
		return em;
	}

	/**
	 * Closes current thread's entity manager.
	 * 
	 * @throws DAOException if there is an error while closing the entity manager
	 */
	public static void close() throws DAOException {
		EntityManager em = locals.get();
		if(em==null) {
			return;
		}
		DAOException dex = null;
		try {
			em.getTransaction().commit();
		} catch(Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			em.close();
		} catch(Exception ex) {
			if(dex!=null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if(dex!=null) throw dex;
	}
	
}