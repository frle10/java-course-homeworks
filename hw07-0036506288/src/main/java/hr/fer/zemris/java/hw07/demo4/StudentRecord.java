package hr.fer.zemris.java.hw07.demo4;

import java.util.Objects;

/**
 * Models a simple student record which contains basic information
 * about a single student.
 * <p>
 * The information contained is:
 * <ul>
 * 	<li>JMBAG</li>
 * 	<li>surname</li>
 * 	<li>name</li>
 * 	<li>points on midterm exam</li>
 * 	<li>points on final exam</li>
 * 	<li>points on labs</li>
 * 	<li>grade</li>
 * </ul>
 * 
 * @author Ivan Skorupan
 */
public class StudentRecord {
	
	/**
	 * Student's JMBAG.
	 */
	private String jmbag;
	
	/**
	 * Student's surname.
	 */
	private String surname;
	
	/**
	 * Student's name.
	 */
	private String name;
	
	/**
	 * Student's midterm exam points.
	 */
	private double midtermExamPoints;
	
	/**
	 * Student's final exam points.
	 */
	private double finalExamPoints;
	
	/**
	 * Student's lab points.
	 */
	private double labPoints;
	
	/**
	 * Student's grade.
	 */
	private int grade;
	
	/**
	 * Constructs a new {@link StudentRecord} object.
	 * <p>
	 * The constructor takes student information described in
	 * {@link StudentRecord} documentation as arguments.
	 * 
	 * @param jmbag - student's jmbag
	 * @param surname - student's surname
	 * @param name - student's name
	 * @param midtermExamPoints - student's points on midterm exam
	 * @param finalExamPoints - student's points on final exam
	 * @param labPoints - student's points on laboratory exercises
	 * @param grade - student's grade
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public StudentRecord(String jmbag, String surname, String name, double midtermExamPoints, double finalExamPoints,
			double labPoints, int grade) {
		this.jmbag = Objects.requireNonNull(jmbag);
		this.surname = Objects.requireNonNull(surname);
		this.name = Objects.requireNonNull(name);
		this.midtermExamPoints = Objects.requireNonNull(midtermExamPoints);
		this.finalExamPoints = Objects.requireNonNull(finalExamPoints);
		this.labPoints = Objects.requireNonNull(labPoints);
		this.grade = Objects.requireNonNull(grade);
	}

	/**
	 * Getter for JMBAG.
	 * 
	 * @return student's JMBAG
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter for surname.
	 * 
	 * @return student's surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Getter for name.
	 * 
	 * @return student's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for midterm exam points.
	 * 
	 * @return student's points on midterm exam
	 */
	public double getMidtermExamPoints() {
		return midtermExamPoints;
	}

	/**
	 * Getter for final exam points.
	 * 
	 * @return student's points on final exam
	 */
	public double getFinalExamPoints() {
		return finalExamPoints;
	}

	/**
	 * Getter for lab points.
	 * 
	 * @return student's points on laboratory exercises
	 */
	public double getLabPoints() {
		return labPoints;
	}

	/**
	 * Getter for grade.
	 * 
	 * @return student's grade
	 */
	public int getGrade() {
		return grade;
	}

	@Override
	public String toString() {
		return jmbag + '\t' + surname + '\t' + name + '\t' + midtermExamPoints + '\t'
				+ finalExamPoints + '\t' + labPoints + '\t' + grade;
	}
	
	
	
}
