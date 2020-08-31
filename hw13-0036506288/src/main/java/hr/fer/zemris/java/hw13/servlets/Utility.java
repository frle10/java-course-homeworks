package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

/**
 * This is a simple utility class which provides two
 * <code>public static</code> methods for loading certain
 * voting data in a map.
 * 
 * @author Ivan Skorupan
 */
public class Utility {
	
	/**
	 * Relative path to the voting definition file.
	 */
	private static final String VOTING_DEFINITION = "/WEB-INF/glasanje-definicija.txt";
	
	/**
	 * Relative path to the voting results file.
	 */
	private static final String VOTING_RESULTS = "/WEB-INF/glasanje-rezultati.txt";
	
	/**
	 * Reads data from file {@link #VOTING_DEFINITION} and organizes it
	 * into a map whose values are band entries and keys are band IDs.
	 * 
	 * @param req - request object
	 * @return a new map containing bands data
	 * @throws IOException if an IO error occurs during file reading
	 */
	public static Map<Integer, BandEntry> getBandsMap(HttpServletRequest req) throws IOException {
		String fileName = req.getServletContext().getRealPath(VOTING_DEFINITION);
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		Map<Integer, BandEntry> bands = new TreeMap<>();
		
		for(String line : lines) {
			String[] lineTokens = line.split("\\t");
			Integer id = Integer.parseInt(lineTokens[0]);
			String bandName = lineTokens[1];
			String songLink = lineTokens[2];

			bands.put(id, new BandEntry(bandName, songLink, 0));
		}
		
		return bands;
	}
	
	/**
	 * Reads data from file {@link #VOTING_RESULTS} and organizes it into
	 * a map whose value are band entries but with number of votes field
	 * set and whose keys are band IDs.
	 * 
	 * @param req - request object
	 * @return a new map containing voting results data
	 * @throws IOException if an IO error occurs during file reading
	 */
	public static Map<Integer, BandEntry> getVotingResults(HttpServletRequest req) throws IOException {
		String fileName = req.getServletContext().getRealPath(VOTING_RESULTS);
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		
		Map<Integer, BandEntry> bands = getBandsMap(req);
		for(String line : lines) {
			String[] lineTokens = line.split("\\t");
			Integer id = Integer.parseInt(lineTokens[0]);
			Integer numberOfVotes = Integer.parseInt(lineTokens[1]);
			
			bands.get(id).setNumberOfVotes(numberOfVotes);
		}
		
		return bands;
	}
	
}
