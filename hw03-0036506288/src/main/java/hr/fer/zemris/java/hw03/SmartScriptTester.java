package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * This class serves as a testing class for the SmartScript lexer and parser.
 * It also features a static method that can be used to recover a document
 * from the given syntax tree.
 * <p>
 * The program expects exactly one command line argument and it should be a path
 * to the file that the user wants to parse.
 * 
 * @author Ivan Skorupan
 */
public class SmartScriptTester {

	/**
	 * Starting point when running the program.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("There should be exactly one command line argument!");
			System.exit(-1);
		}
		
		String filepath = args[0];
		
		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			System.out.println("There was an error reading the file!");
		}
		
		SmartScriptParser parser = null;
		
		try {
			parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch(Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}

		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody); // should write something like original content of docBody
	}
	
	/**
	 * Recovers the original document text from a given syntax tree.
	 * <p>
	 * It does not re-create the document exactly as it was since this is impossible
	 * because of certain empty characters being lost during parsing.
	 * <p>
	 * This method guarantees that when the recovered document string is parsed
	 * again, the resulting syntax tree will be identical to one of the original
	 * document.
	 * 
	 * @param document - the syntax tree to recover the document from
	 * @return a string which contains the recovered document text
	 */
	public static String createOriginalDocumentBody(DocumentNode document) {
		return document.toString();
	}
	
}
