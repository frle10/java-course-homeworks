package hr.fer.zemris.java.hw17.trazilica;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * This class models a file visitor that knows how to pull words out of documents and
 * build a search engine vocabulary from them.
 * <p>
 * A word is defined as any continuous sequence of alphabetical characters.
 * 
 * @author Ivan Skorupan
 */
public class VocabularyBuilder extends SimpleFileVisitor<Path> {
	
	/**
	 * List of distinct words in all documents in given directory subtree.
	 */
	private List<String> vocabulary = new ArrayList<>();
	
	/**
	 * List of words that should not be included in the vocabulary.
	 */
	private List<String> stoppingWords;
	
	/**
	 * Number of documents that were walked.
	 */
	private int numberOfDocuments;
	
	/**
	 * Constructs a new {@link VocabularyBuilder} object.
	 * 
	 * @throws IOException if there is an IO error while reading the stopping words
	 */
	public VocabularyBuilder() throws IOException {
		Path stoppingWordsPath = Paths.get("./src/main/resources/hrvatski_stoprijeci.txt");
		stoppingWords = Files.readAllLines(stoppingWordsPath);
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		numberOfDocuments++;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(Files.newInputStream(file)), "UTF-8"))) {
			while(true) {
				String line = br.readLine();
				if(line == null) break;
				
				char[] lineChars = line.trim().toCharArray();
				StringBuilder word = new StringBuilder();
				for(int i = 0; i < lineChars.length; i++) {
					char character = lineChars[i];
					if(Character.isAlphabetic(character)) {
						word.append(character);
					} else {
						updateVocabulary(word.toString().toLowerCase());
						word = new StringBuilder();
					}
				}
				
				if(word.length() != 0) {
					updateVocabulary(word.toString().toLowerCase());
				}
			}
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	/**
	 * Updates the vocabulary with the given <code>word</code> if
	 * the word is not already in the vocabulary and is not a
	 * stopping word.
	 * 
	 * @param word - word to possibly add to the vocabulary
	 */
	private void updateVocabulary(String word) {
		if(!stoppingWords.contains(word) && !vocabulary.contains(word)) {
			vocabulary.add(word);
		}
	}
	
	/**
	 * Getter for the vocabulary.
	 * 
	 * @return vocabulary
	 */
	public List<String> getVocabulary() {
		return vocabulary;
	}

	/**
	 * Getter for number of walked documents.
	 * 
	 * @return number of walked documents
	 */
	public int getNumberOfDocuments() {
		return numberOfDocuments;
	}
	
}
