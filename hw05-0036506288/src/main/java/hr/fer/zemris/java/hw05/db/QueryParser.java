package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw05.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw05.db.lexer.QueryLexerException;
import hr.fer.zemris.java.hw05.db.lexer.QueryTokenType;

/**
 * Implements a parser for database queries described in this homework (5th hw).
 * 
 * @author Ivan Skorupan
 */
public class QueryParser {

	/**
	 * An internal {@link QueryLexer} instance to use for generating tokens.
	 * 
	 * @see QueryLexer
	 */
	private QueryLexer lexer;

	/**
	 * A list of all conditional expressions within given query.
	 */
	private List<ConditionalExpression> conditionalExpressions;

	/**
	 * Constructs a new {@link SQueryParser} object initialized with given
	 * <code>query</code> string and creates an instance of {@link QueryLexer}
	 * also initialized with <code>query</code>.
	 * 
	 * @param query - query line text to parse
	 * @throws QueryParserException if <code>query</code> is <code>null</code>
	 */
	public QueryParser(String query) {
		if(query == null) {
			throw new QueryParserException("The query cannot be null!");
		}
		this.conditionalExpressions = new ArrayList<>();
		this.lexer = new QueryLexer(query);
		parse();
	}

	/**
	 * Tests if current query is a direct query.
	 * <p>
	 * A direct query has only one conditional expression where the field tested is "jmbag"
	 * and the operator used is "=".
	 * 
	 * @return <code>true</code> if current query is direct, <code>false</code> otherwise
	 */
	public boolean isDirectQuery() {
		if(conditionalExpressions.size() == 1) {
			if(conditionalExpressions.get(0).getFieldGetter().equals(FieldValueGetters.JMBAG)) {
				if(conditionalExpressions.get(0).getComparisonOperator().equals(ComparisonOperators.EQUALS)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns queried jmbag if the query was direct.
	 * 
	 * @return queried jmbag, unless the query was not direct
	 * @throws IllegalStateException if query was not direct
	 */
	public String getQueriedJMBAG() {
		if(isDirectQuery()) {
			return conditionalExpressions.get(0).getStringLiteral();
		}

		throw new IllegalStateException("The query was not direct!");
	}

	/**
	 * Returns a list of conditional expressions contained in the current query.
	 * 
	 * @return list of conditional expression in current query
	 */
	public List<ConditionalExpression> getQuery() {
		return conditionalExpressions;
	}

	/**
	 * Helper method that checks if the current token is of given type.
	 * 
	 * @param type - type to compare against
	 * @return <code>true</code> if current token is of type <code>type</code>, <code>false</code> otherwise
	 * @throws QueryParserException if <code>type</code> is <code>null</code>
	 */
	private boolean isTokenOfType(QueryTokenType type) {
		if(type == null) {
			throw new QueryParserException("The token cannot be of type null!");
		}

		return lexer.getToken().getType() == type;
	}
	
	/**
	 * Implements actual logic for this parser.
	 * 
	 * @throws QueryParserException if there was an error while parsing the given line
	 */
	private void parse() {
		while(true) {
			IFieldValueGetter fieldGetter = null;
			String stringLiteral = null;
			IComparisonOperator compOper = null;

			askForNextToken();

			if(isTokenOfType(QueryTokenType.EOL)) {
				break;
			}

			if(isTokenOfType(QueryTokenType.IDENTIFIER)) {
				String identifierName = lexer.getToken().getValue().toString();

				if(identifierName.equals("firstName")) {
					fieldGetter = FieldValueGetters.FIRST_NAME;
				} else if(identifierName.equals("lastName")) {
					fieldGetter = FieldValueGetters.LAST_NAME;
				} else if(identifierName.equals("jmbag")) {
					fieldGetter = FieldValueGetters.JMBAG;
				} else if(identifierName.toUpperCase().equals("AND")) {
					continue;
				} else {
					throw new QueryParserException("The attribute is not valid, only firstName, lastName and jmbag are supported!");
				}

				askForNextToken();
				
				if(isTokenOfType(QueryTokenType.OPERATOR)) {
					String operator = lexer.getToken().getValue().toString();
					
					if(operator.equals("<")) {
						compOper = ComparisonOperators.LESS;
					} else if(operator.equals("<=")) {
						compOper = ComparisonOperators.LESS_OR_EQUALS;
					} else if(operator.equals(">")) {
						compOper = ComparisonOperators.GREATER;
					} else if(operator.equals(">=")) {
						compOper = ComparisonOperators.GREATER_OR_EQUALS;
					} else if(operator.equals("=")) {
						compOper = ComparisonOperators.EQUALS;
					} else if(operator.equals("!=")) {
						compOper = ComparisonOperators.NOT_EQUALS;
					} else if(operator.equals("LIKE")) {
						compOper = ComparisonOperators.LIKE;
					} else {
						throw new QueryParserException("A valid operator should follow the attribute name!");
					}
				}
				
				askForNextToken();
				
				if(isTokenOfType(QueryTokenType.LITERAL)) {
					stringLiteral = lexer.getToken().getValue().toString();
					
					conditionalExpressions.add(new ConditionalExpression(fieldGetter, stringLiteral, compOper));
				} else {
					throw new QueryParserException("A string literal enclosed in quotes should follow the operator!");
				}
			} else {
				throw new QueryParserException("An attribute name must appear first in the conditional expression!");
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
		} catch(QueryLexerException ex) {
			throw new QueryParserException(ex.getMessage());
		}
	}

}
