package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a static method for formatting a given list of student records for nice output.
 * 
 * @author Ivan Skorupan
 */
public class RecordFormatter {
	
	/**
	 * A variable which remembers the maximum last name length
	 * encountered in a student records list.
	 */
	private static int maxLastNameLength = 0;
	
	/**
	 * A variable which remembers the maximum first name length
	 * encountered in a student records list.
	 */
	private static int maxFirstNameLength = 0;
	
	/**
	 * Returns a list of strings that should be printed out one by one.
	 * 
	 * @param records - a list of student records that needs to be formatted for output
	 * @return a list of strings in desired output format
	 */
	public static List<String> format(List<StudentRecord> records) {
		List<String> lines = new ArrayList<>();
		
		for(StudentRecord record : records) {
			if(record.getFirstName().length() > maxFirstNameLength) {
				maxFirstNameLength = record.getFirstName().length();
			}

			if(record.getLastName().length() > maxLastNameLength) {
				maxLastNameLength = record.getLastName().length();
			}
		}

		String border = makeBorder();
		lines.add(border);
		
		for(StudentRecord record : records) {
			String recordLine = makeRecordLine(record);
			lines.add(recordLine);
		}
		
		lines.add(border);
		
		return lines;
	}

	/**
	 * Generates an output line for a given student record.
	 * 
	 * @param record - a student record whose output line should be generated
	 * @return generated output string for given record
	 */
	private static String makeRecordLine(StudentRecord record) {
		StringBuilder recordLine = new StringBuilder();
		
		recordLine.append("| ");
		recordLine.append(record.getJmbag());
		recordLine.append(" | ");
		recordLine.append(record.getLastName());
		
		int lastNameLength = record.getLastName().length();
		for(int i = 0; i < (maxLastNameLength - lastNameLength); i++) {
			recordLine.append(" ");
		}
		
		recordLine.append(" | ");
		recordLine.append(record.getFirstName());
		
		int firstNameLength = record.getFirstName().length();
		for(int i = 0; i < (maxFirstNameLength - firstNameLength); i++) {
			recordLine.append(" ");
		}
		
		recordLine.append(" | ");
		recordLine.append(record.getFinalGrade());
		recordLine.append(" |");
		
		return recordLine.toString();
	}

	/**
	 * Generates and returns a border string.
	 * 
	 * @return a string representing the query result's border
	 */
	private static String makeBorder() {
		StringBuilder border = new StringBuilder();
		
		border.append("+============+=");

		for(int i = 0; i < maxLastNameLength; i++) {
			border.append("=");
		}

		border.append("=+=");

		for(int i = 0; i < maxFirstNameLength; i++) {
			border.append("=");
		}

		border.append("=+===+");
		
		return border.toString();
	}

}
