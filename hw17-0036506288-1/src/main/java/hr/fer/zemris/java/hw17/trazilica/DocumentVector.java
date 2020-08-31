package hr.fer.zemris.java.hw17.trazilica;

import static java.lang.Math.*;

import java.nio.file.Path;
import java.util.Objects;

/**
 * This class models a vector with N components (user provided).
 * <p>
 * Only possible mathematical operations are the dot product and
 * the norm.
 * <p>
 * Those operations are provided because they are the only ones
 * needed for this search engine implementation.
 * <p>
 * Each of these vectors can be connected to a certain file through
 * a {@link Path} field.
 * 
 * @author Ivan Skorupan
 */
public class DocumentVector {
	
	/**
	 * File (document) whose vector this is.
	 */
	private Path documentPath;
	
	/**
	 * Components of this vector.
	 */
	private double[] components;
	
	/**
	 * Constructs a new {@link DocumentVector} object.
	 * 
	 * @param documentPath - file whose vector this is (can be <code>null</code>)
	 * @param size - number of vector components
	 */
	public DocumentVector(Path documentPath, int size) {
		this.documentPath = documentPath;
		components = new double[size];
	}
	
	/**
	 * Calculates and returns the dot product of this vector and vector
	 * <code>other</code>.
	 * 
	 * @param other - vector to scalar multiply this vector by
	 * @return dot product result
	 */
	public double dotProduct(DocumentVector other) {
		Objects.requireNonNull(other);
		if(components.length != other.components.length) {
			throw new IllegalArgumentException("The vectors must have the same number of components!");
		}
		
		double result = 0;
		for(int i = 0; i < components.length; i++) {
			result += components[i] * other.components[i];
		}
		
		return result;
	}
	
	/**
	 * Calculates and returns the norm of this vector.
	 * 
	 * @return this vector's norm
	 */
	public double norm() {
		double squareSum = 0;
		for(int i = 0; i < components.length; i++) {
			squareSum += pow(components[i], 2);
		}
		
		return sqrt(squareSum);
	}
	
	/**
	 * Getter for document path.
	 * 
	 * @return document path
	 */
	public Path getDocumentPath() {
		return documentPath;
	}
	
	/**
	 * Getter for this vector's components.
	 * 
	 * @return vector components
	 */
	public double[] getComponents() {
		return components;
	}
	
}
