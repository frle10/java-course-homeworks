package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerState;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * A parser for structured document format described in problem 3 of the 3rd homework.
 * 
 * @author Ivan Skorupan
 */
public class SmartScriptParser {

	/**
	 * An internal {@link SmartScriptLexer} instance to use for generating tokens.
	 * 
	 * @see SmartScriptLexer
	 */
	private SmartScriptLexer lexer;

	/**
	 * A tree representing the parsed script.
	 * 
	 * @see DocumentNode
	 */
	private DocumentNode documentNode;
	
	/**
	 * A helper stack used for easier creation of document model tree.
	 * 
	 * @see ObjectStack
	 */
	private ObjectStack stack;

	/**
	 * Constructs a new {@link SmartScriptParser} object initialized with given
	 * <code>documentBody</code> string and creates an instance of {@link SmartScriptLexer}
	 * also initialized with <code>documentBody</code>.
	 * 
	 * @param documentBody - document text to parse
	 * @throws SmartScriptParserException if <code>documentBody</code> is <code>null</code>
	 */
	public SmartScriptParser(String documentBody) {
		if(documentBody == null) {
			throw new SmartScriptParserException("Document body cannot be null!");
		}
		this.lexer = new SmartScriptLexer(documentBody);
		this.documentNode = new DocumentNode();
		this.stack = new ObjectStack();
		this.stack.push(documentNode);
		parse();
	}
	
	/**
	 * Returns the top node of document model tree, an instance of {@link DocumentNode}.
	 * 
	 * @return top node in this document model tree
	 */
	public DocumentNode getDocumentNode() {
		return documentNode;
	}
	
	/**
	 * Helper method that checks if the current token is of given type.
	 * 
	 * @param type - type to compare against
	 * @return <code>true</code> if current token is of type <code>type</code>, <code>false</code> otherwise
	 * @throws SmartScriptParserException if <code>type</code> is <code>null</code>
	 */
	private boolean isTokenOfType(SmartScriptTokenType type) {
		if(type == null) {
			throw new SmartScriptParserException("The token cannot be of type null!");
		}
		
		return lexer.getToken().getType() == type;
	}

	/**
	 * Helper method that represents an implementation of this parser.
	 * 
	 * @throws SmartScriptParserException if an invalid tag gets detected or the internal stack
	 * size at the end of parsing is not 1
	 */
	private void parse() {
		while(true) {
			askForNextToken();

			// if we reached the end of the document, we are done
			if(isTokenOfType(SmartScriptTokenType.EOF)) {
				break;
			}

			// if we reach an opening of a tag, change the lexer state to tag mode
			if(isTokenOfType(SmartScriptTokenType.TAG_OPEN)) {
				lexer.setState(SmartScriptLexerState.TAG);
				askForNextToken();

				if(isTokenOfType(SmartScriptTokenType.IDENT)) {
					String tagName = lexer.getToken().getValue().toString().toUpperCase();

					// check if we have a for loop tag
					if(tagName.equals("FOR")) {
						ForLoopNode forLoop = parseForLoopNode();
						
						try {
							// add this for loop node as a child of the top stack element
							((Node)stack.peek()).addChildNode(forLoop);
						} catch(EmptyStackException ex) {
							throw new EmptyStackException("Cannot peek an element because the stack is empty!");
						}
						
						// push this for loop node to the stack
						stack.push(forLoop);
					} else if(tagName.equals("END")) { // check if we have an end tag
						parseEndTag();
					} else {
						// no supported tag names were found
						throw new SmartScriptParserException("Tag with name: " + tagName + " is unsupported!");
					}
				} else if(isTokenOfType(SmartScriptTokenType.SYMBOL)) {
					String tagSymbol = lexer.getToken().getValue().toString();
					
					// check if we have an echo tag
					if(tagSymbol.equals("=")) {
						try {
							// add this echo node as a child of the top stack element
							((Node)stack.peek()).addChildNode(parseEchoNode());
						} catch(EmptyStackException ex) {
							throw new SmartScriptParserException("Cannot peek an element because the stack is empty!");
						}
					} else {
						// no supported tag names (symbols) were found
						throw new SmartScriptParserException("Tag with symbol: " + tagSymbol + " is unsupported!");
					}
				} else {
					// if after opening a tag there is a token that isn't a valid variable name or a symbol, we have an invalid tag
					throw new SmartScriptParserException("A valid tag name must be a valid identifier or a symbol!");
				}

				continue;
			}
			
			// if token is of textual type, add the text node to this document
			if(isTokenOfType(SmartScriptTokenType.TEXT)) {
				try {
					// add this text node as a child of the top stack element
					((Node)stack.peek()).addChildNode(new TextNode(lexer.getToken().getValue().toString()));
				} catch(EmptyStackException ex) {
					throw new SmartScriptParserException("Cannot peek an element because the stack is empty!");
				}
				
				continue;
			}
		}
		
		// if it's not only document node on the stack, there is something wrong
		if(stack.size() != 1) {
			throw new SmartScriptParserException("Expected internal stack size at the end of parsing is 1, but was: " + stack.size());
		}
	}
	
	/**
	 * Parses the END tag and if the tag is correct, pops one element from the internal stack.
	 * 
	 */
	private void parseEndTag() {
		askForNextToken();
		
		if(!isTokenOfType(SmartScriptTokenType.TAG_CLOSE)) {
			throw new SmartScriptParserException("The END tag should have no additional tokens after it's name and should be closed immediately!");
		}
		
		try {
			stack.pop();
		} catch(EmptyStackException ex) {
			throw new SmartScriptParserException("Cannot pop an element because the stack is empty!");
		}
		
		lexer.setState(SmartScriptLexerState.BODY);
	}
	
	/**
	 * Parses the echo tag.
	 * 
	 * @return an instance of {@link EchoNode} to be put in the document model tree
	 * @throws SmartScriptParserException if EOF was reached before tag closing or if there is another tag opening inside this tag
	 * or if there was an error reading the elements of this echo node
	 */
	private EchoNode parseEchoNode() {
		ArrayIndexedCollection elementsArray = new ArrayIndexedCollection();
		Element[] elements;
		
		while(true) {
			askForNextToken();
			
			if(isTokenOfType(SmartScriptTokenType.EOF)) {
				throw new SmartScriptParserException("The end of file was reached before the echo tag was closed!");
			}
			
			if(isTokenOfType(SmartScriptTokenType.TAG_OPEN)) {
				throw new SmartScriptParserException("Another tag cannot be opened inside an echo tag!");
			}
			
			if(isTokenOfType(SmartScriptTokenType.TAG_CLOSE)) {
				lexer.setState(SmartScriptLexerState.BODY);
				
				Object[] tempArray = elementsArray.toArray();
				elements = new Element[tempArray.length];
				
				for(int i = 0; i < tempArray.length; i++) {
					if(!(tempArray[i] instanceof Element)) {
						throw new SmartScriptParserException("There was an error reading the elements of an echo tag, because an invalid object type was found!");
					}
					
					elements[i] = (Element)tempArray[i];
				}
				
				return new EchoNode(elements);
			}
			
			elementsArray.add(parseEchoNodeElement());
		}
	}
	
	/**
	 * Parses a detected element of an echo tag.
	 * 
	 * @throws SmartScriptParserException if an unsupported operator is detected or no element instance was created
	 */
	private Element parseEchoNodeElement() {
		String currentTokenValue = lexer.getToken().getValue().toString();
		
		if(isTokenOfType(SmartScriptTokenType.INTEGER)) {
			return new ElementConstantInteger(Integer.valueOf(currentTokenValue));
		} else if(isTokenOfType(SmartScriptTokenType.DOUBLE)) {
			return new ElementConstantDouble(Double.valueOf(currentTokenValue));
		} else if(isTokenOfType(SmartScriptTokenType.FUNC)) {
			return new ElementFunction(currentTokenValue);
		} else if(isTokenOfType(SmartScriptTokenType.IDENT)) {
			return new ElementVariable(currentTokenValue);
		} else if(isTokenOfType(SmartScriptTokenType.TEXT)) {
			return new ElementString(currentTokenValue);
		} else if(isTokenOfType(SmartScriptTokenType.SYMBOL)) {
			if(!isSupportedOperator(currentTokenValue)) {
				throw new SmartScriptParserException("An unsupported operator was detected!");
			}
			
			return new ElementOperator(currentTokenValue);
		}
		
		throw new SmartScriptParserException("No Element instance was created because an unknown token type appeared!");
	}

	/**
	 * Checks if given symbol is a supported operator.
	 * 
	 * @return <code>true</code> if symbol is a supported operator, <code>false</code> otherwise
	 */
	private boolean isSupportedOperator(String operator) {
		String supportedOperators = "+-*/^";
		
		if(supportedOperators.contains(operator)) {
			return true;
		}
		
		return false;
	}

	/**
	 * Parses a for loop tag.
	 * 
	 * @return an instance of {@link ForLoopNode} to put in the document model tree
	 * @throws SmartScriptParserException if the first argument is not a variable name or if there is
	 * too many or too few arguments or if there is an invalid argument
	 */
	private ForLoopNode parseForLoopNode() {
		ElementVariable variable = null;
		Element startExpression = null;
		Element endExpression = null;
		Element stepExpression = null;

		askForNextToken();
		SmartScriptTokenType currentTokenType = lexer.getToken().getType();

		if(!(currentTokenType == SmartScriptTokenType.IDENT)) {
			throw new SmartScriptParserException("After opening a FOR tag, the next token should be this tag's name (i.e. \"FOR\")");
		}

		variable = new ElementVariable(lexer.getToken().getValue().toString());

		askForNextToken();
		startExpression = parseForLoopArgument();

		askForNextToken();
		endExpression = parseForLoopArgument();

		askForNextToken();
		if(lexer.getToken().getType() != SmartScriptTokenType.TAG_CLOSE) {
			stepExpression = parseForLoopArgument();

			askForNextToken();

			if(lexer.getToken().getType() != SmartScriptTokenType.TAG_CLOSE) {
				throw new SmartScriptParserException("There are too many arguments for a FOR tag!");
			}
		}

		lexer.setState(SmartScriptLexerState.BODY);
		return new ForLoopNode(variable, startExpression, endExpression, stepExpression);
	}

	/**
	 * Parses an argument inside for loop tag (all except the 1st argument which is the iteration variable,
	 * that was taken care of earlier).
	 * 
	 * @return an instance of {@link Element} which represents one of the for loop arguments
	 * @throws SmartScriptParserException if the argument is invalid
	 */
	private Element parseForLoopArgument() {
		Element expression = null;
		SmartScriptTokenType currentTokenType = lexer.getToken().getType();
		String currentTokenValue = lexer.getToken().getValue().toString();

		if(currentTokenType == SmartScriptTokenType.INTEGER) {
			expression = new ElementConstantInteger(Integer.valueOf(currentTokenValue));
		} else if(currentTokenType == SmartScriptTokenType.DOUBLE) {
			expression = new ElementConstantDouble(Double.valueOf(currentTokenValue));
		} else if(currentTokenType == SmartScriptTokenType.IDENT) {
			expression = new ElementVariable(currentTokenValue);
		} else if(currentTokenType == SmartScriptTokenType.TEXT) {
			expression = new ElementString(currentTokenValue);
		} else {
			throw new SmartScriptParserException("The FOR loop argument is invalid!");
		}

		return expression;
	}
	
	/**
	 * Helper method that calls the lexer method that generates its next token, but
	 * also handles the possible lexer exceptions thrown.
	 */
	private void askForNextToken() {
		try {
			lexer.nextToken();
		} catch(SmartScriptLexerException ex) {
			throw new SmartScriptParserException(ex.getMessage());
		}
	}

}
