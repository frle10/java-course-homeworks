package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Models one student's record.
 * <p>
 * There cannot exist multiple records for the same student.
 * 
 * @author Ivan Skorupan
 */
public class StudentRecord {
	
	/**
	 * This student's jmbag.
	 */
	private String jmbag;
	
	/**
	 * This student's last name.
	 */
	private String lastName;
	
	/**
	 * This student's first name.
	 */
	private String firstName;
	
	/**
	 * This student's final grade.
	 */
	private int finalGrade;
	
	/**
	 * Constructs a new {@link StudentRecord} object.
	 * 
	 * @param jmbag - this student's jmbag
	 * @param lastName - this student's last name
	 * @param firstName - this student's first name
	 * @param finalGrade - this student's final grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}
	
	/**
	 * Getter for jmbag.
	 * 
	 * @return this student's jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}
	
	/**
	 * Getter for last name.
	 * 
	 * @return this student's last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Getter for first name.
	 * 
	 * @return this student's first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Getter for final grade.
	 * 
	 * @return this student's final grade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StudentRecord))
			return false;
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}

	@Override
	public String toString() {
		return "[jmbag=" + jmbag + ", lastName=" + lastName + ", firstName=" + firstName + ", finalGrade="
				+ finalGrade + "]";
	}
	
}
