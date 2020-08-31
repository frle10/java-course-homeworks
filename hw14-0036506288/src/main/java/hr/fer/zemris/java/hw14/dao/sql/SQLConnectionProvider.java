package hr.fer.zemris.java.hw14.dao.sql;

import java.sql.Connection;

/**
 * Storage of database connections into a {@link ThreadLocal} object. ThreadLocal
 * is actually a map whose keys are id's of threads that are performing operations
 * upon the map.
 * 
 * @author Ivan Skorupan
 */
public class SQLConnectionProvider {

	/**
	 * A {@link ThreadLocal} object for storing database connections.
	 */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();
	
	/**
	 * Set the connection for current thread (or delete an entry from the map if argument is <code>null</code>).
	 * 
	 * @param con - database connection
	 */
	public static void setConnection(Connection con) {
		if(con==null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}
	
	/**
	 * Fetch a connection that current thread (the caller) may use.
	 * 
	 * @return a database connection for current thread
	 */
	public static Connection getConnection() {
		return connections.get();
	}
	
}