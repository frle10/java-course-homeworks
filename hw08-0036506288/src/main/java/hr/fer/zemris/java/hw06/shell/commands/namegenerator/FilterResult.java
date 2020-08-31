package hr.fer.zemris.java.hw06.shell.commands.namegenerator;

import java.util.regex.Matcher;

/**
 * Objects of this type represent one selected file along with
 * information about the number of groups found by the
 * {@link Matcher} object and a method for getting the groups.
 * 
 * @author Ivan Skorupan
 */
public class FilterResult {
	
	/**
	 * A matcher that was used to match the represented file
	 * with a certain pattern.
	 */
	private Matcher matcher;
	
	/**
	 * Constructs a new {@link FilterResult} object and
	 * takes a matcher as an argument. The matcher contains
	 * all the info we need for this class.
	 * 
	 * @param matcher
	 */
	public FilterResult(Matcher matcher) {
		this.matcher = matcher;
	}
	
	/**
	 * Returns the group with specified index.
	 * 
	 * @param index - index of group to be returned
	 * @return group of specified index
	 */
	public String group(int index) {
		return matcher.group(index);
	}
	
	/**
	 * Returns the number of groups found for the represented
	 * file (including the implicit group 0).
	 * 
	 * @return number of groups found including implicit group 0
	 */
	public int numberOfGroups() {
		return matcher.groupCount() + 1;
	}
	
	/**
	 * Returns the name of represented file.
	 */
	public String toString() {
		return matcher.group(0);
	}
	
}
