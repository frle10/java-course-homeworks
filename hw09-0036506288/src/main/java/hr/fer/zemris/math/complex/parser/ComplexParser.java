package hr.fer.zemris.math.complex.parser;

import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.complex.lexer.ComplexLexer;
import hr.fer.zemris.math.complex.lexer.ComplexLexerException;
import hr.fer.zemris.math.complex.lexer.ComplexTokenType;

/**
 * A parser for complex numbers of form described in problem 2 of the 9th homework.
 * 
 * @author Ivan Skorupan
 */
public class ComplexParser {

	/**
	 * An internal {@link ComplexLexer} instance to use for generating tokens.
	 * 
	 * @see ComplexLexer
	 */
	private ComplexLexer lexer;
	
	/**
	 * Final instance of parsed complex number string, result of parsing.
	 */
	private Complex complexNumber;
	
	/**
	 * Parsed real part of complex number.
	 */
	private double real;
	
	/**
	 * Parsed imaginary part of complex number.
	 */
	private double imaginary;
	
	/**
	 * Sign of the imaginary part of complex number.
	 */
	private String imaginarySign = "+";

	/**
	 * Constructs a new {@link ComplexParser} object initialized with given
	 * <code>input</code> string and creates an instance of {@link ComplexLexer}
	 * also initialized with <code>input</code>.
	 * 
	 * @param input - input text to parse
	 * @throws ComplexParserException if <code>input</code> is <code>null</code>
	 */
	public ComplexParser(String input) {
		if(input == null) {
			throw new ComplexParserException("Input string cannot be null!");
		}
		this.lexer = new ComplexLexer(input);
		parse();
	}
	
	/**
	 * Getter for the result of parsing.
	 * 
	 * @return parsed complex number as an instance of {@link Complex}
	 */
	public Complex getComplexNumber() {
		return complexNumber;
	}
	
	/**
	 * Helper method that checks if the current token is of given type.
	 * 
	 * @param type - type to compare against
	 * @return <code>true</code> if current token is of type <code>type</code>, <code>false</code> otherwise
	 * @throws ComplexParserException if <code>type</code> is <code>null</code>
	 */
	private boolean isTokenOfType(ComplexTokenType type) {
		if(type == null) {
			throw new ComplexParserException("The given token cannot be of type null!");
		}
		
		return lexer.getToken().getType() == type;
	}

	/**
	 * Helper method that represents an implementation of this parser.
	 * 
	 * @throws ComplexParserException if the complex number string parsed is in any
	 * way invalid
	 */
	private void parse() {
		while(true) {
			askForNextToken();
			
			if(isTokenOfType(ComplexTokenType.EOL)) {
				throw new ComplexParserException("An empty string is not a valid complex number!");
			}
			
			if(isTokenOfType(ComplexTokenType.SYMBOL) || isTokenOfType(ComplexTokenType.IMAGINARY_UNIT)) {
				if(isTokenOfType(ComplexTokenType.SYMBOL)) {
					if(!lexer.getToken().getValue().toString().equals("-")) {
						throw new ComplexParserException("When writing only imaginary part, no symbols are allowed other than '-', which includes leading pluses!");
					}
					
					imaginarySign = lexer.getToken().getValue().toString();
					askForNextToken();
				}
				
				parseImaginaryPart();
				complexNumber = new Complex(real, (imaginarySign.equals("+") ? imaginary : -imaginary));
				break;
			}
			
			parseRealPart();
			askForNextToken();
			
			if(isTokenOfType(ComplexTokenType.EOL)) {
				complexNumber = new Complex(real, imaginary);
				break;
			}
			
			if(isTokenOfType(ComplexTokenType.SYMBOL)) {
				if(isSupportedSymbol(lexer.getToken().getValue().toString())) {
					imaginarySign = lexer.getToken().getValue().toString();
				} else {
					throw new ComplexParserException("A sign (+ or -) is supossed to come afer the real part!");
				}
			} else {
				throw new ComplexParserException("A sign (+ or -) is supossed to come afer the real part!");
			}
			
			askForNextToken();
			parseImaginaryPart();
			
			complexNumber = new Complex(real, (imaginarySign.equals("+") ? imaginary : -imaginary));
			break;
		}
	}
	
	/**
	 * Parses the real part of complex number.
	 */
	private void parseRealPart() {
		if(isTokenOfType(ComplexTokenType.NUMBER)) {
			real = Double.parseDouble(lexer.getToken().getValue().toString());
		} else {
			throw new ComplexParserException("The complex number must start with its real part!");
		}
	}

	/**
	 * Parses the imaginary part of complex number.
	 */
	private void parseImaginaryPart() {
		if(!isTokenOfType(ComplexTokenType.IMAGINARY_UNIT)) {
			throw new ComplexParserException("Imaginary unit is supossed to come after the sign of imaginary part!");
		}
		
		askForNextToken();
		
		if(isTokenOfType(ComplexTokenType.EOL)) {
			imaginary = 1;
			return;
		}
		
		if(isTokenOfType(ComplexTokenType.NUMBER)) {
			imaginary = Double.parseDouble(lexer.getToken().getValue().toString());
			
			if(imaginary < 0) {
				throw new ComplexParserException("The imaginary part sign is defined before the imaginary unit!");
			}
		} else{
			throw new ComplexParserException("The imaginary part is supossed to come after the imaginary unit!");
		}
		
		askForNextToken();
		
		if(!isTokenOfType(ComplexTokenType.EOL)) {
			throw new ComplexParserException("The complex number is supossed to end with its imaginary part!");
		}
	}
	
	/**
	 * Tests if the given <code>symbol</code> is supported against a
	 * string that contains supported symbols.
	 * <p>
	 * Here, the supported symbols are: '+' and '-'.
	 * 
	 * @param symbol - symbol to check for validity
	 * @return <code>true</code> if <code>symbol</code> is valid, <code>false</code> otherwise
	 */
	private boolean isSupportedSymbol(String symbol) {
		String supportedSymbols = "+-";
		if(supportedSymbols.contains(symbol)) return true;
		return false;
	}
	
	/**
	 * Helper method that calls the lexer method that generates its next token, but
	 * also handles the possible lexer exceptions thrown.
	 */
	private void askForNextToken() {
		try {
			lexer.nextToken();
		} catch(ComplexLexerException ex) {
			throw new ComplexParserException(ex.getMessage());
		}
	}

}
