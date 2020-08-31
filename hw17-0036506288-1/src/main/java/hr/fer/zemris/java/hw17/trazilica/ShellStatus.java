package hr.fer.zemris.java.hw17.trazilica;

/**
 * Models the status of {@link Konzola}.
 * <p>
 * If status is {@link #CONTINUE}, the shell keeps accepting
 * new commands.
 * <p>
 * If status is {@link #TERMINATE}, the shell should stop
 * accepting new commands and exits.
 * 
 * @author Ivan Skorupan
 */
public enum ShellStatus {
	/** Keep accepting new commands (working mode). **/
	CONTINUE,
	/** Stop accepting new commands and exit the shell. **/
	TERMINATE;
}
