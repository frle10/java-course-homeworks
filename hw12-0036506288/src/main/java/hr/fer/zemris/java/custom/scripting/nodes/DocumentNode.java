package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * A node representing an entire document.
 * 
 * @author Ivan Skorupan
 */
public class DocumentNode extends Node {

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
	
}
