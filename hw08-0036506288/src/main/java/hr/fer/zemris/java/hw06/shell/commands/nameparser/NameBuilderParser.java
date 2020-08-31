package hr.fer.zemris.java.hw06.shell.commands.nameparser;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.commands.MassrenameShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.namegenerator.NameBuilder;
import hr.fer.zemris.java.hw06.shell.commands.namegenerator.DefaultNameBuilders;
import hr.fer.zemris.java.hw06.shell.commands.namelexer.NameBuilderLexer;
import hr.fer.zemris.java.hw06.shell.commands.namelexer.NameBuilderLexerException;
import hr.fer.zemris.java.hw06.shell.commands.namelexer.NameBuilderTokenType;

/**
 * A simple parser that parses the expressions used as the last argument
 * of {@link MassrenameShellCommand massrename} shell command when MODIFIER
 * is "show" or "execute".
 * 
 * @author Ivan Skorupan
 */
public class NameBuilderParser {
	
	/**
	 * Lexer that will be used for generating tokens of the given expression.
	 */
	private NameBuilderLexer lexer;
	
	/**
	 * List of {@link NameBuilder} objects that this parser will generate throughout the
	 * parsing proccess.
	 */
	private List<NameBuilder> nameBuilders;
	
	/**
	 * Constructs a new {@link NameBuilderParser} object.
	 * 
	 * @param expression - expression to be parsed
	 */
	public NameBuilderParser(String expression) {
		if(expression == null) {
			throw new NameBuilderParserException("Expression cannot be null!");
		}
		this.lexer = new NameBuilderLexer(expression);
		this.nameBuilders = new ArrayList<>();
		parse();
	}
	
	/**
	 * Returns a {@link NameBuilder} object through the use of
	 * {@link DefaultNameBuilders#all(List) all()} method.
	 * 
	 * @return a composite {@link NameBuilder} object
	 */
	public NameBuilder getNameBuilder() {
		return DefaultNameBuilders.all(nameBuilders);
	}
	
	/**
	 * Helper method that checks if the current token is of given type.
	 * 
	 * @param type - type to compare against
	 * @return <code>true</code> if current token is of type <code>type</code>, <code>false</code> otherwise
	 * @throws NameBuilderParserException if <code>type</code> is <code>null</code>
	 */
	private boolean isTokenOfType(NameBuilderTokenType type) {
		if(type == null) {
			throw new NameBuilderParserException("The token cannot be of type null!");
		}
		
		return lexer.getToken().getType() == type;
	}
	
	/**
	 * Helper method that represents an implementation of this parser.
	 * 
	 * @throws NameBuilderParserException if an invalid substitution expression is detected
	 */
	private void parse() {
		while(true) {
			askForNextToken();
			
			if(isTokenOfType(NameBuilderTokenType.EOL)) {
				break;
			}
			
			if(isTokenOfType(NameBuilderTokenType.TEXT)) {
				nameBuilders.add(DefaultNameBuilders.text(lexer.getToken().getValue()));
				continue;
			}
			
			if(isTokenOfType(NameBuilderTokenType.GROUP)) {
				int index = Integer.parseInt(lexer.getToken().getValue());
				nameBuilders.add(DefaultNameBuilders.group(index));
				continue;
			}
			
			if(isTokenOfType(NameBuilderTokenType.EXTGROUP)) {
				String[] specifiers = lexer.getToken().getValue().split(",");
				if(specifiers.length != 2) {
					throw new NameBuilderParserException("Two specifiers were expected in (group, description) substitution command!");
				}
				
				int index = Integer.parseInt(specifiers[0]);
				String paddingAndWidth = specifiers[1];
				char padding = ' ';
				int minWidth = 0;
				
				if(paddingAndWidth.startsWith("0")) {
					padding = '0';
					minWidth = Integer.parseInt(paddingAndWidth.substring(1));
				} else {
					minWidth = Integer.parseInt(paddingAndWidth);
				}
				
				nameBuilders.add(DefaultNameBuilders.group(index, padding, minWidth));
				continue;
			}
		}
	}
	
	/**
	 * Helper method that calls the lexer method that generates its next token, but
	 * also handles the possible lexer exceptions thrown.
	 */
	private void askForNextToken() {
		try {
			lexer.nextToken();
		} catch(NameBuilderLexerException ex) {
			throw new NameBuilderParserException(ex.getMessage());
		}
	}
	
}
