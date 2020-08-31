package hr.fer.zemris.java.custom.scripting.exec;

import static hr.fer.zemris.java.custom.scripting.exec.SmartScriptFunctions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.SmartHttpServer;

/**
 * This class models a kind of interpreter that can execute a
 * SmartScript whose structure was described in homework 3.
 * <p>
 * The engine takes a document node and a request context in order to
 * be able to execute the whole script and write the execution results
 * to the client through our {@link SmartHttpServer}.
 * 
 * @author Ivan Skorupan
 */
public class SmartScriptEngine {
	
	/**
	 * Document node to start the execution from.
	 */
	private DocumentNode documentNode;
	
	/**
	 * An HTTP request object we are using to send execution result data to our client. 
	 */
	private RequestContext requestContext;
	
	/**
	 * A multistack used to store variable names defined in a given SmartScript as keys
	 * and their values as entry values.
	 */
	private ObjectMultistack multistack = new ObjectMultistack();
	
	/**
	 * A visitor that this engine will use to visit document nodes and execute their
	 * parts one by one (like an interpreter).
	 */
	private INodeVisitor visitor = new INodeVisitor() {
		
		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				System.out.println("There was a problem while writing the TextNode data to the output stream!");
			}
		}
		
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String varName = node.getVariable().getName();
			ValueWrapper start = new ValueWrapper(parseValue(node.getStartExpression()));
			ValueWrapper end = new ValueWrapper(parseValue(node.getEndExpression()));
			ValueWrapper step = new ValueWrapper((node.getStepExpression() == null) ?
					1 : parseValue(node.getStepExpression()));
			multistack.push(varName, start);
			
			while(multistack.peek(varName).numCompare(end.getValue()) <= 0) {
				for(int i = 0; i < node.numberOfChildren(); i++) {
					node.getChild(i).accept(this);
				}
				
				multistack.peek(varName).add(step.getValue());
			}
			
			multistack.pop(varName);
		}
		
		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<ValueWrapper> temporaryStack = new Stack<>();
			Element[] elements = node.getElements();
			
			for(Element element : elements) {
				if(element instanceof ElementConstantInteger) {
					temporaryStack.push(new ValueWrapper(((ElementConstantInteger) element).getValue()));
				} else if(element instanceof ElementConstantDouble) {
					temporaryStack.push(new ValueWrapper(((ElementConstantDouble) element).getValue()));
				} else if(element instanceof ElementString) {
					temporaryStack.push(new ValueWrapper(((ElementString) element).getValue()));
				} else if(element instanceof ElementVariable) {
					temporaryStack.push(new ValueWrapper(multistack.peek(((ElementVariable) element).getName()).getValue()));
				} else if(element instanceof ElementOperator) {
					performOperation(((ElementOperator) element).getSymbol(), temporaryStack);
				} else if(element instanceof ElementFunction) {
					performFunction(((ElementFunction) element).getName(), temporaryStack);
				}
			}
			
			List<ValueWrapper> values = new ArrayList<>();
			while(!temporaryStack.isEmpty()) {
				values.add(temporaryStack.pop());
			}
			
			for(int i = values.size() - 1; i >= 0; i--) {
				try {
					requestContext.write(values.get(i).getValue().toString());
				} catch (IOException e) {
					System.out.println("There was a problem while writing data to the output stream!");
					return;
				}
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}
	};
	
	/**
	 * Constructs a new {@link SmartScriptEngine} object.
	 * 
	 * @param documentNode - top node in document tree from which to start execution
	 * @param requestContext - a request that ran the engine and that should be worked with
	 * while executing the given script
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = Objects.requireNonNull(documentNode);
		this.requestContext = Objects.requireNonNull(requestContext);
	}
	
	/**
	 * Starts the given script's execution by calling the
	 * {@link Node#accept(INodeVisitor) accept()} method of
	 * <code>documentNode</code>, which will in turn call all
	 * the document's children's <code>accept()</code> methods.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
	
	/**
	 * Returns the value of given <code>expression</code> that is of type
	 * {@link Element}. In case the <code>expression</code> is a {@link ElementVariable},
	 * its value is fetched from the multistack and returned.
	 * 
	 * @param expression - the expression to parse a numerical value from (value as a string, not actual number)
	 * @return the value of given <code>expression</code>
	 */
	private Object parseValue(Element expression) {
		if(expression instanceof ElementVariable) {
			return multistack.peek(((ElementVariable) expression).getName()).getValue();
		} else {
			return expression.asText();
		}
	}
	
	/**
	 * Takes a function name <code>function</code> and a reference to a <code>temporaryStack</code>
	 * that is used while performing functions supported by SmartScripts.
	 * <p>
	 * Based on given function name, an appropriate function is called from {@link SmartScriptFunctions}
	 * class.
	 * 
	 * @param function - function name
	 * @param temporaryStack - temporary stack that is used for executing SmartScript functions
	 */
	private void performFunction(String function, Stack<ValueWrapper> temporaryStack) {
		if(function.equals("sin")) sine(temporaryStack);
		else if(function.equals("decfmt")) decfmt(temporaryStack);
		else if(function.equals("dup")) dup(temporaryStack);
		else if(function.equals("swap")) swap(temporaryStack);
		else if(function.equals("setMimeType")) setMimeType(requestContext, temporaryStack);
		else if(function.equals("paramGet")) paramGet(requestContext, temporaryStack);
		else if(function.equals("pparamGet")) pparamGet(requestContext, temporaryStack);
		else if(function.equals("pparamSet")) pparamSet(requestContext, temporaryStack);
		else if(function.equals("pparamDel")) pparamDel(requestContext, temporaryStack);
		else if(function.equals("tparamGet")) tparamGet(requestContext, temporaryStack);
		else if(function.equals("tparamSet")) tparamSet(requestContext, temporaryStack);
		else if(function.equals("tparamDel")) tParamDel(requestContext, temporaryStack);
	}
	
	/**
	 * Takes an operation <code>symbol</code> and a reference to <code>temporaryStack</code>
	 * which is used to fetch the operation arguments and then executes the operation.
	 * <p>
	 * Operations supported are:
	 * <ul>
	 * 	<li>Addition</li>
	 * 	<li>Subtraction</li>
	 * 	<li>Multiplication</li>
	 * 	<li>Division</li>
	 * </ul>
	 * 
	 * @param symbol - the mathematical operation symbol
	 * @param temporaryStack - temporary stack to get the operation arguments from
	 * @throws UnsupportedOperationException if the given operation <code>symbol</code> is unsupported
	 */
	private void performOperation(String symbol, Stack<ValueWrapper> temporaryStack) {
		ValueWrapper second = temporaryStack.pop();
		ValueWrapper first = temporaryStack.pop();
		
		if(symbol.equals("+")) {
			first.add(second.getValue());
		} else if(symbol.equals("-")) {
			first.subtract(second.getValue());
		} else if(symbol.equals("*")) {
			first.multiply(second.getValue());
		} else if(symbol.equals("/")) {
			first.divide(second.getValue());
		} else {
			throw new UnsupportedOperationException("The echo node operation " + symbol + " is unsupported!");
		}
		
		temporaryStack.push(first);
	}
	
}
