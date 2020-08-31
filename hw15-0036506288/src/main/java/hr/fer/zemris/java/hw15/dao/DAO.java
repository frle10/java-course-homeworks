package hr.fer.zemris.java.hw15.dao;

import hr.fer.zemris.java.hw15.model.BlogEntry;

/**
 * Interface towards a subsystem for data persistence.
 * 
 * @author Ivan Skorupan
 */
public interface DAO {

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 * 
	 * @param id ključ zapisa
	 * @return entry ili <code>null</code> ako entry ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;
	
}