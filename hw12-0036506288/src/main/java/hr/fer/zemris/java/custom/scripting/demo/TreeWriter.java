package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * This is a demo program that aims to show the use of Visitor Design
 * Pattern in a problem we already solved in homework 3.
 * <p>
 * The program expects exactly one argument and it should be a file name
 * of a smart script. After taking the argument, the file is opened,
 * parsed into a tree and then its approximate original form is reproduced
 * and written on the standard output.
 * 
 * @author Ivan Skorupan
 */
public class TreeWriter {

	/**
	 * Starting point of this demo program.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Expected exactly one argument: a smart script file path!");
			return;
		}
		
		Path script = null;
		try {
			script = Paths.get(args[0]);
		} catch(InvalidPathException ex) {
			System.out.println("The given path is invalid!");
			return;
		}
		
		String docBody = null;
		try {
			docBody = Files.readString(script);
		} catch (IOException e) {
			System.out.println("There was a problem while reading the script from the disk!");
			return;
		}
		
		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
		// by the time the previous line completes its job, the document should have been written
		// on the standard output
	}
	
	/**
	 * An implementation of a node visitor that prints out a parsed
	 * smart script document tree to the standard output when given
	 * the document node.
	 * 
	 * @author Ivan Skorupan
	 */
	private static class WriterVisitor implements INodeVisitor {
		
		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.toString());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print(node.toString());
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.print(node.toString());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}
		
	}
	
}
