package hr.fer.zemris.java.hw17.trazilica;

import java.nio.file.Path;

/**
 * This class models a query result.
 * <p>
 * A result contains the similarity value between the query and one document
 * and also a path to that document.
 * 
 * @author Ivan Skorupan
 */
public class QueryResult {
	
	/**
	 * TF-IDF similarity value between the query and a document.
	 */
	private double similarity;
	
	/**
	 * Document with whom the query was compared with.
	 */
	private Path documentPath;

	/**
	 * Constructs a new {@link QueryResult} object.
	 * 
	 * @param similarity - similarity value between the query and the document
	 * @param documentPath - path to document with whom query was compared
	 */
	public QueryResult(double similarity, Path documentPath) {
		this.similarity = similarity;
		this.documentPath = documentPath;
	}

	/**
	 * Getter for this query result's similarity value
	 * 
	 * @return similarity value
	 */
	public double getSimilarity() {
		return similarity;
	}

	/**
	 * Getter for this query result's document path.
	 * 
	 * @return document path of this query result
	 */
	public Path getDocumentPath() {
		return documentPath;
	}

	@Override
	public String toString() {
		return "(" + String.format("%.4f", similarity) + ") " + documentPath.toString();
	}
	
}
