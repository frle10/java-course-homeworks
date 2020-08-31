package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Models an expression that is a simple string of characters.
 * 
 * @author Ivan Skorupan
 */
public class ElementString extends Element {
	
	/**
	 * Text of this string element.
	 */
	private String value;
	
	/**
	 * Constructs a new {@link ElementString} object initialized with
	 * its text.
	 * 
	 * @param value
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 */
	public ElementString(String value) {
		this.value = Objects.requireNonNull(value);
	}
	
	/**
	 * Return this string's text.
	 * 
	 * @return text of this string
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String asText() {
		char[] valueCharArray = value.toCharArray();
		StringBuilder recoveredText = new StringBuilder("\"");
		
		for(char c : valueCharArray) {
			if(c == '\\') {
				recoveredText.append("\\\\");
			} else if(c == '\"') {
				recoveredText.append("\\\"");
			} else if(c == '\n') {
				recoveredText.append("\\n");
			} else if(c == '\t') {
				recoveredText.append("\\t");
			} else if(c == '\r') {
				recoveredText.append("\\r");
			} else {
				recoveredText.append(c);
			}
		}
		
		return recoveredText.append("\"").toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ElementString))
			return false;
		ElementString other = (ElementString) obj;
		return Objects.equals(value, other.value);
	}
	
}
