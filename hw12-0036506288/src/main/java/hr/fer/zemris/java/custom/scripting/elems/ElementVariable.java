package hr.fer.zemris.java.custom.scripting.elems;

import java.util.Objects;

/**
 * Models an expression that represents a variable name.
 * 
 * @author Ivan Skorupan
 */
public class ElementVariable extends Element {
	
	/**
	 * Name of this variable.
	 */
	private String name;
	
	/**
	 * Constructs a new {@link ElementVariable} object initialized with
	 * its {@link #name}.
	 * 
	 * @param name - name of this variable
	 * @throws NullPointerException if <code>name</code> is <code>null</code>
	 */
	public ElementVariable(String name) {
		this.name = Objects.requireNonNull(name);
	}
	
	/**
	 * Returns this variable's {@link #name}.
	 * 
	 * @return name of this variable
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String asText() {
		return name;
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
		if (!(obj instanceof ElementVariable))
			return false;
		ElementVariable other = (ElementVariable) obj;
		return Objects.equals(name, other.name);
	}
	
}
