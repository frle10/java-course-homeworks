package hr.fer.zemris.java.hw17.trazilica.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw17.trazilica.DocumentVector;
import hr.fer.zemris.java.hw17.trazilica.Environment;
import hr.fer.zemris.java.hw17.trazilica.QueryResult;
import hr.fer.zemris.java.hw17.trazilica.ShellStatus;
import hr.fer.zemris.java.hw17.trazilica.ShellUtil;
import hr.fer.zemris.java.hw17.trazilica.VocabularyBuilder;

/**
 * Command "query" expects a variable number of arguments but at least one.
 * <p>
 * The arguments should be key words for which to search for in the document
 * database.
 * 
 * @author Ivan Skorupan
 */
public class QueryShellCommand extends Command {
	
	/**
	 * Constructs a new {@link QueryShellCommand} object.
	 */
	public QueryShellCommand() {
		super("query");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length == 0) {
			env.writeln("The query is empty!");
			return ShellStatus.CONTINUE;
		}
		
		VocabularyBuilder vocabBuilder = ((VocabularyBuilder) env.getSharedData("vocabBuilder"));
		List<String> finalQuery = new ArrayList<>();
		DocumentVector queryTfVector = new DocumentVector(null, vocabBuilder.getVocabulary().size());
		for(int i = 0; i < args.length; i++) {
			String word = args[i];
			int wordIndex = vocabBuilder.getVocabulary().indexOf(word);
			
			if(wordIndex != -1) {
				finalQuery.add(word);
				queryTfVector.getComponents()[wordIndex]++;
			}
		}
		
		env.writeln("Query is: " + finalQuery);
		
		DocumentVector queryTfIdfVector = new DocumentVector(null, vocabBuilder.getVocabulary().size());
		DocumentVector idfVector = (DocumentVector) env.getSharedData("idfVector");
		
		for(int i = 0; i < queryTfIdfVector.getComponents().length; i++) {
			queryTfIdfVector.getComponents()[i] = queryTfVector.getComponents()[i] * idfVector.getComponents()[i];
		}
		
		List<DocumentVector> tfIdfVectors = (List<DocumentVector>) env.getSharedData("tfIdfVectors");
		List<QueryResult> results = new ArrayList<>();
		for(int i = 0; i < vocabBuilder.getNumberOfDocuments(); i++) {
			DocumentVector docTfIdfVector = tfIdfVectors.get(i);
			double similarity = (docTfIdfVector.dotProduct(queryTfIdfVector)) / (docTfIdfVector.norm() * queryTfIdfVector.norm());
			QueryResult result = new QueryResult(similarity, docTfIdfVector.getDocumentPath());
			results.add(result);
		}
		
		env.setSharedData("results", results);
		env.writeln("Najboljih 10 rezultata:");
		
		results.sort((r1, r2) -> Double.compare(r2.getSimilarity(), r1.getSimilarity()));
		ShellUtil.writeResults(env, results);
		
		return ShellStatus.CONTINUE;
	}
	
}
