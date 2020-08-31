package hr.fer.zemris.java.hw17.trazilica;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A simple string tokenizer used to tokenize {@link SearchEngineShellEnvironment} commands.
 * <p>
 * It supports generation of path arguments that are enclosed in quotes and contain spaces
 * inside path to directory or file.
 * <p>
 * Note that path arguments enclosed in quotes will be returned as such, with quotes.
 * That means that in future generation of {@link Path} objects, the quotes will need to
 * be removed or else the path argument will throw an {@link InvalidPathException}.
 * 
 * @author Ivan Skorupan
 */
public class ShellTokenizer {
	
	/**
	 * Internal character array generated from passed input string.
	 */
	private char[] data;
	
	/**
	 * Current index of this tokenizer inside {@link #data} array.
	 */
	private int currentIndex;
	
	/**
	 * Last generated argument string.
	 */
	private String currentToken;
	
	/**
	 * Constructs a new {@link ShellTokenizer} object.
	 * <p>
	 * The tokenizer is initialized with passed <code>input</code>
	 * string which is being converted to a character array internally.
	 * 
	 * @param arguments - a string containing shell input that needs to be tokenized
	 * @throws NullPointerException if <code>input</code> is <code>null</code>
	 */
	public ShellTokenizer(String input) {
		Objects.requireNonNull(input);
		this.data = input.toCharArray();
		this.currentIndex = 0;
		this.currentToken = null;
	}
	
	/**
	 * Returns the last generated string.
	 * 
	 * @return last generated token
	 */
	public String getToken() {
		return currentToken;
	}
	
	/**
	 * Implements actual tokenization of commands and generates them by saving the
	 * current generated string to {@link #currentToken} field.
	 */
	public void nextToken() {
		skipEmptyContent();
		
		// check if we already tokenized the whole input string
		if(currentIndex >= data.length) {
			currentToken = null;
			return;
		}
		
		// check if we need to tokenize a path argument enclosed in quotes
		if(data[currentIndex] == '\"') {
			StringBuilder path = new StringBuilder();
			path.append(data[currentIndex++]);
			
			while(currentIndex < data.length && data[currentIndex] != '\"') {
				checkEscapeSequences();
				path.append(data[currentIndex++]);
			}
			
			path.append(data[currentIndex++]);
			currentToken = path.toString();
			currentIndex++;
			return;
		}
		
		// here we are tokenizing a command name or a normal argument that's not enclosed in quotes
		StringBuilder argument = new StringBuilder();
		while(currentIndex < data.length && data[currentIndex] != ' ') {
			argument.append(data[currentIndex++]);
		}
		
		currentToken = argument.toString();
		return;
	}
	
	/**
	 * Checks if the current character is a backslash and then checks if the character behind it
	 * forms a supported escape sequence.
	 * <p>
	 * If the escape sequence is supported, the backslash character is then ignored so the character
	 * behind it can be read without it.
	 */
	private void checkEscapeSequences() {
		if(data[currentIndex] == '\\') {
			if(data[currentIndex + 1] == '\"' || data[currentIndex + 1] == '\\') {
				currentIndex++;
			}
		}
	}

	/**
	 * Skips all empty content in the character array such as spaces and tabs.
	 */
	private void skipEmptyContent() {
		while(currentIndex < data.length && (data[currentIndex] == ' ' || data[currentIndex] == '\t')) {
			currentIndex++;
		}
	}
	
}
