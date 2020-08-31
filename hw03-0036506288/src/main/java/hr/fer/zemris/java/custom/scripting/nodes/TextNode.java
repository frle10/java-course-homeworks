package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

/**
 * A node representing a piece of textual data.
 * 
 * @author Ivan Skorupan
 */
public class TextNode extends Node {
	
	/**
	 * Textual data that this node contains.
	 */
	private String text;
	
	/**
	 * Constructs a new {@link TextNode} object.
	 * 
	 * @param text - this node's textual data
	 * @throws NullPointerException if <code>text</code> is <code>null</code>
	 */
	public TextNode(String text) {
		this.text = Objects.requireNonNull(text);
	}
	
	/**
	 * Getter for {@link #text} field.
	 * 
	 * @return - this node's textual data as a {@link String}
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		char[] textCharArray = text.toCharArray();
		StringBuilder recoveredText = new StringBuilder();
		
		for(char c : textCharArray) {
			if(c == '\\') {
				recoveredText.append("\\\\");
			} else if(c == '{') {
				recoveredText.append("\\{");
			} else {
				recoveredText.append(c);
			}
		}
		
		return recoveredText.toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(text);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TextNode))
			return false;
		TextNode other = (TextNode) obj;
		return text.equals(other.text);
	}
	
}
