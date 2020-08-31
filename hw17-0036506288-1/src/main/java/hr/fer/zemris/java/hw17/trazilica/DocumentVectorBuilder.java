package hr.fer.zemris.java.hw17.trazilica;

import static java.lang.Math.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class models a file visitor which builds TF and TF-IDF vectors for documents
 * in a given directory subtree.
 * <p>
 * It also builds the helping IDF vector.
 * 
 * @author Ivan Skorupan
 */
public class DocumentVectorBuilder extends SimpleFileVisitor<Path> {
	
	/**
	 * List of words that were already visited in current file.
	 */
	private List<String> visited = new ArrayList<>();
	
	/**
	 * List of term frequency (TF) vectors.
	 */
	private List<DocumentVector> tfVectors = new ArrayList<>();
	
	/**
	 * List of term frequency - inverse document frequency (TF-IDF) vectors.
	 */
	private List<DocumentVector> tfIdfVectors = new ArrayList<>();
	
	/**
	 * The inverse document frequency (IDF) vector.
	 */
	private DocumentVector idfVector;
	
	/**
	 * Vector that holds information on how many documents contain a certain word
	 * from the vocabulary.
	 */
	private DocumentVector docsWithWord;
	
	/**
	 * A reference to {@link VocabularyBuilder} object that holds the vocabulary and
	 * other relevant information.
	 */
	private VocabularyBuilder vocabBuilder;
	
	/**
	 * Constructs a new {@link DocumentVectorBuilder} object.
	 * 
	 * @param vocabBuilder - vocabulary builder reference
	 * @throws NullPointerException if <code>vocabBuilder</code> is <code>null</code>
	 */
	public DocumentVectorBuilder(VocabularyBuilder vocabBuilder) {
		this.vocabBuilder = Objects.requireNonNull(vocabBuilder);
		idfVector = new DocumentVector(null, vocabBuilder.getVocabulary().size());
		docsWithWord = new DocumentVector(null, vocabBuilder.getVocabulary().size());
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		DocumentVector docVector = new DocumentVector(file, vocabBuilder.getVocabulary().size());
		tfVectors.add(docVector);
		
		visited.clear();
		
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
						updateVectors(word.toString().toLowerCase());
						word = new StringBuilder();
					}
				}
				
				if(word.length() != 0) {
					updateVectors(word.toString().toLowerCase());
				}
			}
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	/**
	 * This method updates the vectors that we need to calculate given a
	 * word that was just parsed from a document.
	 * 
	 * @param word - a word parsed from some document
	 */
	private void updateVectors(String word) {
		int wordIndex = vocabBuilder.getVocabulary().indexOf(word);
		if(wordIndex != -1) {
			if(!visited.contains(word)) {
				docsWithWord.getComponents()[wordIndex]++;
				visited.add(word);
			}
			tfVectors.get(tfVectors.size() - 1).getComponents()[wordIndex]++;
		}
	}
	
	/**
	 * Calculates the IDF vector.
	 * <p>
	 * This method should be called after walking the file tree with this visitor.
	 */
	public void calculateIdfVector() {
		for(int i = 0; i < vocabBuilder.getVocabulary().size(); i++) {
			idfVector.getComponents()[i] = log((double) vocabBuilder.getNumberOfDocuments() / docsWithWord.getComponents()[i]);
		}
	}
	
	/**
	 * Calculates the TF-IDF vectors. This method should be called
	 * after {@link #calculateIdfVector()}.
	 */
	public void calculateTfIdfVectors() {
		for(DocumentVector tfVector : tfVectors) {
			DocumentVector tfIdfVector = new DocumentVector(tfVector.getDocumentPath(), tfVector.getComponents().length);
			tfIdfVectors.add(tfIdfVector);
			for(int i = 0; i < vocabBuilder.getVocabulary().size(); i++) {
				tfIdfVector.getComponents()[i] = tfVector.getComponents()[i] * idfVector.getComponents()[i];
			}
		}
	}

	/**
	 * Getter for TF vectors.
	 * 
	 * @return TF vectors
	 */
	public List<DocumentVector> getTfVectors() {
		return tfVectors;
	}

	/**
	 * Getter for TF-IDF vectors.
	 * 
	 * @return TF-IDF vectors
	 */
	public List<DocumentVector> getTfIdfVectors() {
		return tfIdfVectors;
	}

	/**
	 * Getter for IDF vector.
	 * 
	 * @return IDF vector
	 */
	public DocumentVector getIdfVector() {
		return idfVector;
	}
	
}
