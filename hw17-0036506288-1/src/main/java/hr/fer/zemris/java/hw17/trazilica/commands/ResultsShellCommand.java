package hr.fer.zemris.java.hw17.trazilica.commands;

import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw17.trazilica.Environment;
import hr.fer.zemris.java.hw17.trazilica.QueryResult;
import hr.fer.zemris.java.hw17.trazilica.ShellStatus;
import hr.fer.zemris.java.hw17.trazilica.ShellUtil;

/**
 * Command "results" expects no arguments.
 * <p>
 * The command prints the results of latest successful query run.
 * <p>
 * If no successful queries were run since shell startup, an appropriate
 * message is printed.
 * 
 * @author Ivan Skorupan
 */
public class ResultsShellCommand extends Command {
	
	/**
	 * Constructs a new {@link ResultsShellCommand} object.
	 */
	public ResultsShellCommand() {
		super("results");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env);
		Objects.requireNonNull(arguments);
		String[] args = CommandUtil.getSplitCommandInput(arguments);
		
		if(args.length != 0) {
			env.writeln("Too many arguments for results command!");
			return ShellStatus.CONTINUE;
		}
		
		List<QueryResult> results = (List<QueryResult>) env.getSharedData("results");
		
		if(results == null) {
			env.writeln("No successful queries were run since shell startup.");
			return ShellStatus.CONTINUE;
		}
		
		ShellUtil.writeResults(env, results);
		return ShellStatus.CONTINUE;
	}

}
