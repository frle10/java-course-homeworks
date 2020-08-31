package hr.fer.zemris.java.hw06.shell.commands.namegenerator;

import java.util.Objects;

/**
 * Objects of this type generate parts of new file names by writing into
 * the given {@link StringBuilder} object in method
 * {@link #execute(FilterResult, StringBuilder) execute()}.
 * 
 * @author Ivan Skorupan
 */
public interface NameBuilder {
	
	/**
	 * Generates a part of the new file name for given <code>result</code>
	 * and writes the generated string into <code>sb</code>. 
	 * 
	 * @param result - a filter result object that represents the selected file for
	 * which we are currently generating a new name
	 * @param sb - a {@link StringBuilder} object to write the generated file name part to
	 */
	void execute(FilterResult result, StringBuilder sb);
	
	/**
	 * Returns a {@link NameBuilder} object that's a binary composite
	 * and will call the {@link #execute(FilterResult, StringBuilder) execute()}
	 * method of both the current name builder and provided name builder <code>other</code>.
	 * 
	 * @param other - another name builder to join with current into a binary composite
	 * @return a binary composite {@link NameBuilder} object
	 */
	default NameBuilder then(NameBuilder other) {
		Objects.requireNonNull(other);
		return (fr, sb) -> {
			execute(fr, sb);
			other.execute(fr, sb);
		};
	}
	
}
