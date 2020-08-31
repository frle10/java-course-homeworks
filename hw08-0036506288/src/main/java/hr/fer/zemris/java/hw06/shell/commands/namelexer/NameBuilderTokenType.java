package hr.fer.zemris.java.hw06.shell.commands.namelexer;

/**
 * Enumeration of different token types the {@link NameBuilderToken}
 * can have.
 * 
 * @author Ivan Skorupan
 */
public enum NameBuilderTokenType {
	/** If the substring analyzed is plain text. **/
	TEXT,
	/** If the substring analyzed is substitution command {groupNumber}. **/
	GROUP,
	/** If the substring analyzed is substitution command {groupNumber,format}. **/
	EXTGROUP,
	/** End of line **/
	EOL;
}
