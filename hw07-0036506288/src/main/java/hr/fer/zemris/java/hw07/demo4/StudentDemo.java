package hr.fer.zemris.java.hw07.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Demo program used for solving the 4th problem in 7th homework.
 * <p>
 * The tasks require the use of new stream API.
 * 
 * @author Ivan Skorupan
 */
public class StudentDemo {

	/**
	 * Entry point of this demo program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		Path studentsFile = Paths.get("./src/main/resources/studenti.txt");
		List<String> lines = null;
		try {
			lines = Files.readAllLines(studentsFile);
		} catch (IOException e) {
			System.out.println("There was a problem reading the file!");
			System.exit(-1);
		}
		
		List<StudentRecord> records = convert(lines);
		
		long broj = vratiBodovaViseOd25(records);
		
		System.out.println("Zadatak 1");
		System.out.println("=========");
		System.out.println(broj);
		
		long broj5 = vratiBrojOdlikasa(records);
		
		System.out.println("Zadatak 2");
		System.out.println("=========");
		System.out.println(broj5);
		
		List<StudentRecord> odlikasi = vratiListuOdlikasa(records);
		
		System.out.println("Zadatak 3");
		System.out.println("=========");
		odlikasi.forEach((o) -> System.out.println(o));
		
		List<StudentRecord> odlikasiSortirano = vratiSortiranuListuOdlikasa(records);
		
		System.out.println("Zadatak 4");
		System.out.println("=========");
		odlikasiSortirano.forEach((o) -> System.out.println(o));
		
		List<String> nepolozeniJMBAGovi = vratiPopisNepolozenih(records);
		
		System.out.println("Zadatak 5");
		System.out.println("=========");
		nepolozeniJMBAGovi.forEach((jmbag) -> System.out.println(jmbag));
		
		Map<Integer, List<StudentRecord>> mapaPoOcjenama = razvrstajStudentePoOcjenama(records);
		
		System.out.println("Zadatak 6");
		System.out.println("=========");
		mapaPoOcjenama.forEach((k, v) -> System.out.println(k + " => " + v));
		
		Map<Integer, Integer> mapaPoOcjenama2 = vratiBrojStudenataPoOcjenama(records);
		
		System.out.println("Zadatak 7");
		System.out.println("=========");
		mapaPoOcjenama2.forEach((k, v) -> System.out.println(k + " => " + v));
		
		Map<Boolean, List<StudentRecord>> prolazNeprolaz = razvrstajProlazPad(records);
		
		System.out.println("Zadatak 8");
		System.out.println("=========");
		prolazNeprolaz.forEach((k, v) -> System.out.println(k + " => " + v));
	}
	
	/**
	 * Finds number of students whose sum of points from midterm exam, final exam
	 * and labs exceeds 25 in <code>records</code> and returns that number.
	 * 
	 * @param records - list of student records
	 * @return number of students whose sum of midterm exam, final exam and lab points exceeds 25
	 */
	public static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream()
				.mapToDouble((sr) -> sr.getMidtermExamPoints() + sr.getFinalExamPoints() + sr.getLabPoints())
				.filter((d) -> d > 25)
				.count();
	}
	
	/**
	 * Finds number of excellent students in <code>records</code> and returns that number.
	 * 
	 * @param records - list of student records
	 * @return number of excellent students
	 */
	public static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream()
				.filter((sr) -> sr.getGrade() == 5)
				.count();
	}
	
	/**
	 * Generates a list of excellent students from <code>records</code> and returns it.
	 * 
	 * @param records - list of student records
	 * @return list of excellent students
	 */
	public static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records){
		return records.stream()
				.filter((sr) -> sr.getGrade() == 5)
				.collect(Collectors.toList());
	}
	
	/**
	 * Generates a list of excellent students from <code>records</code>, but sorts it in
	 * descending manner by total number of points and returns it.
	 * 
	 * @param records - list of student records
	 * @return sorted list of excellent students (by total number of points, descending)
	 */
	public static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		Comparator<StudentRecord> pointsComparator = (sr1, sr2) -> {
			Double sum1 = sr1.getMidtermExamPoints() + sr1.getFinalExamPoints() + sr1.getLabPoints();
			Double sum2 = sr2.getMidtermExamPoints() + sr2.getFinalExamPoints() + sr2.getLabPoints();
			
			return sum2.compareTo(sum1);
		};
		
		return records.stream()
				.filter((sr) -> sr.getGrade() == 5)
				.sorted(pointsComparator)
				.collect(Collectors.toList());
	}
	
	/**
	 * Generates a list of students who didn't pass the subject from <code>records</code>
	 * and returns it.
	 * 
	 * @param records - list of student records
	 * @return list of students who didn't pass the subject
	 */
	public static List<String> vratiPopisNepolozenih(List<StudentRecord> records){
		return records.stream()
				.filter((sr) -> sr.getGrade() == 1)
				.map(StudentRecord::getJmbag)
				.sorted((s1, s2) -> s1.compareTo(s2))
				.collect(Collectors.toList());
	}
	
	/**
	 * Generates a map from <code>records</code> whose keys are grades and values are lists
	 * of students with those grades and returns it. 
	 * 
	 * @param records - list of student records
	 * @return map of students mapped by grades
	 */
	public static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.groupingBy(StudentRecord::getGrade));
	}
	
	/**
	 * Generates a map from <code>records</code> whose keys are grades and the value
	 * is number of students with corresponding grade and returns it.
	 * 
	 * @param records - list of student records
	 * @return map of number of students mapped by grades
	 */
	public static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records){
		return records.stream()
				.collect(Collectors.toMap(StudentRecord::getGrade, (sr) -> 1, (int1, int2) -> int1 + int2));
	}
	
	/**
	 * Generates a map from <code>records</code> whose keys are <code>true</code> and
	 * <code>false</code> and values are lists of students who either passed
	 * (key is <code>true</code>) or didn't pass (key is <code>false</code>)
	 * the subject and returns it.
	 * 
	 * @param records - list of student records
	 * @return map of students mapped by subject passage
	 */
	public static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records){
		return records.stream()
				.collect(Collectors.partitioningBy((sr) -> sr.getGrade() != 1));
	}
	
	/**
	 * Converts the lines of input file into concrete {@link StudentRecord}
	 * objects.
	 * <p>
	 * This method implements the parsing of information from each line of input
	 * file to create {@link StudentRecord} objects.
	 * 
	 * @param lines - list of input file lines as strings
	 * @return a list of generated student records
	 */
	private static List<StudentRecord> convert(List<String> lines) {
		List<StudentRecord> studentRecords = new ArrayList<>();
		
		for(String line : lines) {
			if(line.isEmpty()) continue;
			String[] tokens = line.split("\\t");
			
			String jmbag = tokens[0];
			String surname = tokens[1];
			String name = tokens[2];
			double midtermExamPoints = Double.parseDouble(tokens[3]);
			double finalExamPoints = Double.parseDouble(tokens[4]);
			double labPoints = Double.parseDouble(tokens[5]);
			int grade = Integer.parseInt(tokens[6]);
			
			StudentRecord record = new StudentRecord(jmbag, surname, name, midtermExamPoints,
					finalExamPoints, labPoints, grade);
			studentRecords.add(record);
		}
		
		return studentRecords;
	}
	
}
