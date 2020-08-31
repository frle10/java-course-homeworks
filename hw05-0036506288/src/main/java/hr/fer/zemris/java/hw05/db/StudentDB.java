package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * *************************************************************************************
 * UPUTA RECENZENTU! Datoteka "database.txt" nalazi se u src/main/resources folderu,   *
 * pa samo promijeni put do datoteke dolje u programu jer će se vjerojatno             *
 * razlikovati na tvom računalu. :)                                                    *
 * *************************************************************************************
 */

/**
 * When started, program reads data from file "database.txt".
 * <p>
 * If in provided file there are duplicate jmbags, or if finalGrade is not a number between 1 and 5,
 * program terminates with appropriate message to the user.
 * 
 * @author Ivan Skorupan
 */
public class StudentDB {

	/**
	 * Index of first character after the query command and a space directly after it.
	 */
	private static final int INDEX_AFTER_QUERY_COMMAND = 6;
	
	/**
	 * Number of border lines in console output.
	 */
	private static final int NUMBER_OF_BORDER_LINES = 2;

	/**
	 * Entry point when running the program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		List<String> lines = null;

		try {
			lines = Files.readAllLines(Paths.get("D:\\Projects\\EclipseWorkspaces\\Homeworks\\hw05-0036506288\\src\\main\\resources\\database.txt"), StandardCharsets.UTF_8);
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(-1);
		}

		StudentDatabase database = new StudentDatabase(lines);

		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.print("> ");

			String query = null;
			if(scanner.hasNext()) {
				query = scanner.nextLine().trim();
			}

			if(query.startsWith("query ")) {
				QueryParser parser = null;
				
				try {
					parser = new QueryParser(query.substring(INDEX_AFTER_QUERY_COMMAND));
				} catch(QueryParserException ex) {
					System.out.println("There was a problem parsing your query!");
					System.out.println();
					continue;
				}
				
				List<ConditionalExpression> conditionalExpressions = parser.getQuery();
				
				List<StudentRecord> records = new ArrayList<>();
				
				if(parser.isDirectQuery()) {
					StudentRecord jmbagRecord = database.forJMBAG(parser.getQueriedJMBAG());
					
					if(jmbagRecord != null) {
						records.add(database.forJMBAG(parser.getQueriedJMBAG()));
					}
				} else {
					records = selectFromDatabaseWithFiltering(database, conditionalExpressions);
				}
				
				List<String> output = RecordFormatter.format(records);
				
				if(parser.isDirectQuery()) {
					System.out.println("Using index for record retrieval.");
				}
				
				if(output.size() > 2) {
					output.forEach(System.out::println);
				}
				
				System.out.println("Records selected: " + (output.size() - NUMBER_OF_BORDER_LINES));
				System.out.println();
			} else if(query.equals("exit")) {
				System.out.println("Goodbye!");
				break;
			} else {
				System.out.println("An unsupported command was entered!");
				System.out.println();
			}
		}
		
		scanner.close();
	}
	
	/**
	 * Determines which records should be written based on filtering condition and returns a list of those records.
	 * 
	 * @param database - a student database whose records to filter
	 * @param conditionalExpressions - a list of conditional expressions to check against and use for filtering records
	 * @return a list of student records that satisfy given conditional expressions
	 */
	private static List<StudentRecord> selectFromDatabaseWithFiltering(StudentDatabase database, List<ConditionalExpression> conditionalExpressions) {
		return database.filter(new IFilter() {

			@Override
			public boolean accepts(StudentRecord record) {
				for(ConditionalExpression condExpr : conditionalExpressions) {
					IFieldValueGetter fieldGetter = condExpr.getFieldGetter();
					String literal = condExpr.getStringLiteral();
					IComparisonOperator compOper = condExpr.getComparisonOperator();

					if(!compOper.satisfied(fieldGetter.get(record), literal)) {
						return false;
					}
				}

				return true;
			}

		});
	}

}
