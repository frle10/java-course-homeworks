package hr.fer.zemris.java.hw17.trazilica;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import hr.fer.zemris.java.hw17.trazilica.commands.ShellCommand;

/**
 * This is the main class for this search engine.
 * <p>
 * It contains the <code>main</code> method and shell execution
 * starts here, as well as preparations before actual interaction
 * with the user starts (calculating the vocabulary and TF vectors).
 * 
 * @author Ivan Skorupan
 */
public class Konzola {
	
	/**
	 * Starting point of this program.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Expected one argument - path to directory with test documents.");
			return;
		}
		
		Path docDir = null;
		try {
			docDir = Paths.get(args[0]);
		} catch(InvalidPathException ex) {
			System.out.println("The argument provided is not a valid path!");
			return;
		}
		
		if(!Files.isDirectory(docDir)) {
			System.out.println("The argument must be a path to an existing directory!");
			return;
		}
		
		Scanner scanner = new Scanner(System.in);
		Environment env = new SearchEngineShellEnvironment(scanner);
		ShellStatus status = ShellStatus.CONTINUE;
		
		VocabularyBuilder vocabBuilder = null;
		try {
			vocabBuilder = new VocabularyBuilder();
		} catch (IOException e) {
			env.writeln("There was an IO error while building the vocabulary!");
			return;
		}
		
		try {
			Files.walkFileTree(docDir, vocabBuilder);
		} catch (IOException e) {
			env.writeln("There was an IO error while building the vocabulary!");
			return;
		}
		
		DocumentVectorBuilder vectorBuilder = new DocumentVectorBuilder(vocabBuilder);
		try {
			Files.walkFileTree(docDir, vectorBuilder);
		} catch(IOException e) {
			env.writeln("There was an IO error while building necessary vectors!");
			return;
		}
		
		vectorBuilder.calculateIdfVector();
		vectorBuilder.calculateTfIdfVectors();
		env.setSharedData("vocabBuilder", vocabBuilder);
		env.setSharedData("idfVector", vectorBuilder.getIdfVector());
		env.setSharedData("tfIdfVectors", vectorBuilder.getTfIdfVectors());
		
		env.writeln("");
		env.writeln("Veličina riječnika je " + vocabBuilder.getVocabulary().size() + " riječi.");
		env.writeln("");
		
		do {
			env.write("Enter command > ");
			String line = env.readLine();
			String commandName = ShellUtil.extractCommandName(line);
			String arguments = ShellUtil.extractArguments(line);
			ShellCommand command = env.commands().get(commandName);
			
			if(command != null) {
				status = command.executeCommand(env, arguments);
			} else {
				env.writeln("Nepoznata naredba.");
			}
			
			env.writeln("");
		} while(status != ShellStatus.TERMINATE);
		
		scanner.close();
	}
	
}
