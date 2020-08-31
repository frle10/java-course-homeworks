package hr.fer.zemris.java.hw06.shell.commands.namegenerator;

import java.util.List;
import java.util.Objects;

/**
 * Contains <code>public static</code> factory methods that build certain
 * types of {@link NameBuilder} objects.
 * <p>
 * There are 3 main types of {@link NameBuilder} objects:
 * <ul>
 * 	<li>text - if part of the new file name to generate is simple text</li>
 * 	<li>group with index - if part of the new file name to generate contains a substitution
 * 	command {groupNumber}</li>
 * 	<li>group with index, padding and minimum width - if part of the new file name to generate contains a substitution
 * 	command {groupNumber,format}</li>
 * </ul>
 * <p>
 * There is also a 4th type of {@link NameBuilder} available and that's the composite
 * one which would just call {@link NameBuilder#execute(FilterResult, StringBuilder) execute}
 * methods of all name builders given to it in a list.
 * 
 * @author Ivan Skorupan
 */
public class DefaultNameBuilders {
	
	/**
	 * Returns a new {@link NameBuilder} object that appends
	 * the given string <code>t</code> to the new file name
	 * string builder.
	 * 
	 * @param t - string to append
	 * @return a new {@link NameBuilder} object with described implementation
	 * @throws NullPointerException if <code>t</code> is <code>null</code>
	 */
	public static NameBuilder text(String t) {
		Objects.requireNonNull(t);
		return (fr, sb) -> sb.append(t);
	}
	
	/**
	 * Returns a new {@link NameBuilder} object that appends
	 * the group of specified <code>index</code> to the new file name
	 * string builder.
	 * 
	 * @param index - index of the group to be fetched and appended to the new file name
	 * @return a new {@link NameBuilder} object with described implementation
	 */
	public static NameBuilder group(int index) {
		return (fr, sb) -> sb.append(fr.group(index));
	}
	
	/**
	 * Returns a new {@link NameBuilder} object that appends
	 * the group of specified <code>index</code> to the new file name
	 * string builder.
	 * <p>
	 * This {@link NameBuilder} also takes into account the "format" part
	 * of the substitution command so this method takes two extra arguments:
	 * <code>padding</code> and <code>minWidth</code>.
	 * 
	 * @param index - index of the group to be fetched and appended to the new file name
	 * @param padding - character to fill the unfilled width with from the left
	 * @param minWidth - minimum width of the appended group, if group is smaller then fill with <code>padding</code>
	 * @return a new {@link NameBuilder} object with described implementation
	 */
	public static NameBuilder group(int index, char padding, int minWidth) {
		return (fr, sb) -> {
			String group = fr.group(index);
			if(minWidth > group.length()) {
				for(int i = 0; i < (minWidth - group.length()); i++) {
					sb.append(padding);
				}
			}
			sb.append(group);
		};
	}
	
	/**
	 * Returns a new {@link NameBuilder} object that takes a list of
	 * other {@link NameBuilder} objects and calls all their
	 * {@link NameBuilder#execute(FilterResult, StringBuilder) execute()}
	 * methods.
	 * 
	 * @param nameBuilders - a list of {@link NameBuilder} objects
	 * @return a new {@link NameBuilder} object with described implementation
	 * @throws NullPointerException if <code>nameBuilders</code> is <code>null</code>
	 */
	public static NameBuilder all(List<NameBuilder> nameBuilders) {
		Objects.requireNonNull(nameBuilders);
		return (fr, sb) -> {
			for(NameBuilder nameBuilder : nameBuilders) {
				nameBuilder.execute(fr, sb);
			}
		};
	}
	
}
