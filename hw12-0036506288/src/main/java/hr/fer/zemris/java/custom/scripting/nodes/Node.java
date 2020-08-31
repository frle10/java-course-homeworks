package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;
import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Base class for all graph nodes.
 * 
 * @author Ivan Skorupan
 */
public abstract class Node {

	/**
	 * Internally managed {@link ArrayIndexedCollection} list of direct children
	 * for this node.
	 */
	private ArrayIndexedCollection children;

	/**
	 * Adds given child to an internally managed collection of children.
	 * <p>
	 * Creates an instance of {@link #children} on the first call.
	 * 
	 * @param child - a child node to be added to the collection of children
	 */
	public void addChildNode(Node child) {
		if(children == null) {
			children = new ArrayIndexedCollection();
		}

		children.add(child);
	}

	/**
	 * Returns the number of direct children for this {@link Node}.
	 * 
	 * @return number of direct children for this {@link Node}
	 */
	public int numberOfChildren() {
		return (children == null) ? 0 : children.size();
	}

	/**
	 * Returns the selected child or throws an appropriate exception if
	 * the index is invalid.
	 * 
	 * @param index - position from which to fetch the child
	 * @return child {@link Node} at specified index
	 * @throws NullPointerException if <code>children</code> is <code>null</code>
	 * @throws IndexOutOfBoundsException if <code>index</code> is invalid
	 */
	public Node getChild(int index) {
		if(children == null) {
			throw new NullPointerException("The children array is not yet initialized!");
		}
		
		if(index < 0 || index > children.size() - 1) {
			throw new IndexOutOfBoundsException("The index is invalid!");
		}
		
		return (Node)children.get(index);
	}
	
	/**
	 * Accepts a visitor that is visiting this node. The <code>visitor</code>
	 * should have an appropriate method used for analyzing/consuming
	 * the visited type of node, so the visited node simply calls that
	 * method (usually method <code>visitTypeOfNode()</code>).
	 * 
	 * @param visitor - the visitor to accept the visit of
	 */
	public abstract void accept(INodeVisitor visitor);

	@Override
	public String toString() {
		StringBuilder nodeText = new StringBuilder();

		if(children != null) {
			Object[] childrenNodes = children.toArray();
			for(Object childrenNode : childrenNodes) {
				if(childrenNode instanceof Node) {
					nodeText.append(((Node)childrenNode).toString());
				}
			}
		}

		return nodeText.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(children);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		
		Node other = (Node) obj;
		
		if (children == null && other.children == null)
			return true;
		if (children != null && other.children != null)
			return Arrays.equals(this.children.toArray(), other.children.toArray());
		
		return false;
	}

}
