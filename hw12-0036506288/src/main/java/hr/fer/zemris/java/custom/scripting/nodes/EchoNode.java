package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * A node representing a command which generates some textual output
 * dynamically.
 * 
 * @author Ivan Skorupan
 */
public class EchoNode extends Node {
	
	/**
	 * An array of elements that this echo node contains.
	 */
	private Element[] elements;
	
	/**
	 * Constructs a new {@link EchoNode} object intialized with
	 * a list of its elements.
	 * 
	 * @param elements - an array of elements contained in this node
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}

	/**
	 * Getter for {@link #elements} field.
	 * 
	 * @return internal array of {@link #elements} of this node
	 */
	public Element[] getElements() {
		return elements;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
	
	@Override
	public String toString() {
		StringBuilder echoNode = new StringBuilder();
		
		echoNode.append("{$= ");
		
		for(Element element : elements) {
			echoNode.append(element.asText() + " ");
		}
		
		echoNode.append("$}");
		
		return echoNode.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(elements);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof EchoNode))
			return false;
		EchoNode other = (EchoNode) obj;
		return Arrays.equals(elements, other.elements);
	}
	
}
