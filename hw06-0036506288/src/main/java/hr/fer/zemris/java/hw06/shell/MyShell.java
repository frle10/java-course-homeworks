package hr.fer.zemris.java.hw06.shell;

import java.util.Scanner;

/**
 * A simulation program that provides a space for running a desired
 * shell {@link Environment}.
 * <p>
 * The program runs in a loop which starts by prompting the user for
 * input and running the given command if possible. Afterwards,
 * the process is repeated until the user enters the command to exit
 * the shell.
 * 
 * @author Ivan Skorupan
 */
public class MyShell {
	
	/**
	 * Shell's entry point.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Environment env = new MyShellEnvironment(scanner);
		ShellStatus status = ShellStatus.CONTINUE;
		
		env.writeln("Welcome to MyShell v 1.0");
		do {
			env.write(env.getPromptSymbol() + " ");
			String lines = ShellUtil.readLineOrLines(env);
			String commandName = ShellUtil.extractCommandName(lines);
			String arguments = ShellUtil.extractArguments(lines);
			ShellCommand command = env.commands().get(commandName);
			
			if(command != null) {
				try {
					status = command.executeCommand(env, arguments);
				} catch(ShellIOException ex) {
					env.writeln(ex.getMessage());
					status = ShellStatus.TERMINATE;
				}
			} else {
				env.writeln("Command " + commandName + " is not supported!");
			}
			
			env.writeln("");
		} while(status != ShellStatus.TERMINATE);
		
		scanner.close();
	}
	
}
