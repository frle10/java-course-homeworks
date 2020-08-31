package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Models an element that represents a valid function.
 * 
 * @author Ivan Skorupan
 */
public class ElementFunction extends Element {
	
	/**
	 * Name of this function.
	 */
	private String name;
	
	/**
	 * Constructs a new {@link ElementFunction} object initialized with
	 * its {@link #name}.
	 * 
	 * @param name - name of this function
	 * @throws NullPointerException if <code>name</code> is <code>null</code>
	 */
	public ElementFunction(String name) {
		this.name = Objects.requireNonNull(name);
	}
	
	/**
	 * Returns the name of this function.
	 * 
	 * @return name of this function
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String asText() {
		return "@" + name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ElementFunction))
			return false;
		ElementFunction other = (ElementFunction) obj;
		return Objects.equals(name, other.name);
	}
	
	
	
}
