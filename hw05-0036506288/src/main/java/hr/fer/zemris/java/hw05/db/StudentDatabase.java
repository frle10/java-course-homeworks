package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Models a database of students by utilizing an internal list of student records
 * (basically table cells containing student info).
 * <p>
 * It also implements an index for fast retrieval of student records in O(1).
 * <p>
 * Duplicate entries in this database are <b>not</b> allowed.
 * 
 * @author Ivan Skorupan
 */
public class StudentDatabase {
	
	/**
	 * An internal list of student records.
	 */
	private List<StudentRecord> studentRecords;
	
	/**
	 * A map containing all student record's mapped under corresponding jmbags. This is the
	 * collection used for fast retrieval of records.
	 */
	private Map<String, StudentRecord> jmbagMap;

	/**
	 * Constructs a new {@link StudentDatabase} object.
	 * 
	 * @param studentRecords - a list of studentRecords to be analyzed and added to this database
	 * @throws NullPointerException if <code>studentRecords</code> is <code>null</code>
	 */
	public StudentDatabase(List<String> studentRecords) {
		Objects.requireNonNull(studentRecords);
		this.jmbagMap = new HashMap<>();
		this.studentRecords = new ArrayList<>();
		
		for(String recordText : studentRecords) {
			StudentRecord record = makeStudentRecord(recordText);
			
			if(jmbagMap.get(record.getJmbag()) == null) {
				this.studentRecords.add(record);
				this.jmbagMap.put(record.getJmbag(), record);
			} else {
				throw new IllegalArgumentException("Duplicate JMBAGs are not allowed!");
			}
		}
	}
	
	/**
	 * Obtains student records using given jmbag in O(1) complexity.
	 * 
	 * @param jmbag - jmbag of student whose record to retrieve from database
	 * @return record of student whose jmbag was given or <code>null</code> if no such record exists
	 * @throws NullPointerException if <code>jmbag</code> is <code>null</code>
	 */
	public StudentRecord forJMBAG(String jmbag) {
		Objects.requireNonNull(jmbag);
		
		return jmbagMap.get(jmbag);
	}
	
	/**
	 * Loops through all student records in the internal list and calls the
	 * {@link IFilter#accepts(StudentRecord) accepts()} method on given filter object
	 * with current record.
	 * <p>
	 * All records for whom the aforementioned method returns <code>true</code> are added to
	 * a temporary list that is being returned at the end.
	 * 
	 * @param filter - a filter object to use for filtering database records
	 * @return a list of student records that satisfy the filter
	 * @throws NullPointerException if <code>filter</code> is <code>null</code>
	 */
	public List<StudentRecord> filter(IFilter filter) {
		Objects.requireNonNull(filter);
		List<StudentRecord> temporaryList = new ArrayList<>();
		
		for(StudentRecord record : studentRecords) {
			if(filter.accepts(record)) {
				temporaryList.add(record);
			}
		}
		
		return temporaryList;
	}
	
	/**
	 * Makes a new {@link StudentRecord} object from the given record's string.
	 * 
	 * @param recordText - record string to parse for a new record object
	 * @return a new {@link StudentRecord} object parsed from <code>recordText</code>
	 */
	private StudentRecord makeStudentRecord(String recordText) {
		String[] recordTokens = recordText.trim().replaceAll("\\s+", " ").split(" ");
		
		if(recordTokens.length < 4) {
			throw new IllegalArgumentException("Record: " + recordText + " is invalid, there should be at least 4 attributes!");
		}
		
		String jmbag = recordTokens[0];
		char[] temp = jmbag.toCharArray();
		for(char character : temp) {
			if(!Character.isDigit(character)) {
				throw new IllegalArgumentException("JMBAG can contain only digits 0-9!");
			}
		}
		
		String lastName = readLastName(recordTokens);
		String firstName = recordTokens[recordTokens.length - 2];
		
		int finalGrade = 0;
		
		try {
			finalGrade = Integer.parseInt(recordTokens[recordTokens.length - 1]);
		} catch(NumberFormatException ex) {
			throw new NumberFormatException("The final grade must be a valid integer!");
		}
		
		if(finalGrade < 1 || finalGrade > 5) {
			throw new IllegalArgumentException("The final grade must be between 1 and 5!");
		}
		
		return new StudentRecord(jmbag, lastName, firstName, finalGrade);
	}

	/**
	 * Parses the last name of student from given record's tokens array.
	 * 
	 * @param recordTokens - a string array containing current record's tokens
	 * @return current student's last name as a {@link String} object
	 */
	private String readLastName(String[] recordTokens) {
		StringBuilder lastName = new StringBuilder();
		
		for(int i = 1; i < recordTokens.length - 2; i++) {
			lastName.append(recordTokens[i]);
		}
		
		return lastName.toString();
	}
}
