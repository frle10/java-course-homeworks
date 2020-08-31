package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * This interface models objects that know how to visit nodes
 * of a document tree created by {@link SmartScriptParser} and
 * perform appropriate operations on them.
 * <p>
 * In other words, objects of this type are visitors in the
 * Visitor Design Pattern.
 * 	
 * @author Ivan Skorupan
 */
public interface INodeVisitor {
	
	/**
	 * Action this visitor should perform when it visits a
	 * {@link TextNode}.
	 * 
	 * @param node - {@link TextNode} to perform an action on
	 */
	public void visitTextNode(TextNode node);
	
	/**
	 * Action this visitor should perform when it visits a
	 * {@link ForLoopNode}.
	 * 
	 * @param node - {@link ForLoopNode} to perform an action on
	 */
	public void visitForLoopNode(ForLoopNode node);
	
	/**
	 * Action this visitor should perform when it visits a
	 * {@link EchoNode}.
	 * 
	 * @param node - {@link EchoNode} to perform an action on
	 */
	public void visitEchoNode(EchoNode node);
	
	/**
	 * Action this visitor should perform when it visits a
	 * {@link DocumentNode}.
	 * 
	 * @param node - {@link DocumentNode} to perform an action on
	 */
	public void visitDocumentNode(DocumentNode node);
	
}
