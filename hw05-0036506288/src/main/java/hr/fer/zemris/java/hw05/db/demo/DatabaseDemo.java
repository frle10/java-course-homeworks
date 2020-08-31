package hr.fer.zemris.java.hw05.db.demo;

import hr.fer.zemris.java.hw05.db.QueryParser;

/**
 * This program shows some code functionality for implemented student database.
 * 
 * @author Ivan Skorupan
 */
public class DatabaseDemo {

	/**
	 * Entry point when running the program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
		System.out.println("isDirectQuery(): " + qp1.isDirectQuery()); // true
		System.out.println("jmbag was: " + qp1.getQueriedJMBAG()); // 0123456789
		System.out.println("size: " + qp1.getQuery().size()); // 1
		
		System.out.println();
		
		QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
		System.out.println("isDirectQuery(): " + qp2.isDirectQuery()); // false
		
		// System.out.println(qp2.getQueriedJMBAG()); // would throw!
		try {
			System.out.println(qp2.getQueriedJMBAG());
		} catch(IllegalStateException ex) {
			System.out.println("It did throw!");
		}
		
		System.out.println("size: " + qp2.getQuery().size()); // 2

	}

}
