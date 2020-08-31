package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;

/**
 * This implementation of {@link FileVisitor} prints the given directory tree
 * to the shell using the given {@link Environment}.
 * 
 * @author Ivan Skorupan
 */
public class TreeFileVisitor implements FileVisitor<Path> {
	
	/**
	 * Shell environment to use for communication with the shell and printing the tree.
	 */
	private Environment env;
	
	/**
	 * Current directory level at which the visitor is.
	 */
	private int level;
	
	/**
	 * Constructs a new {@link TreeFileVisitor} object.
	 * <p>
	 * The constructor takes one argument, the shell environment which
	 * will be used for interaction with specific shell.
	 * 
	 * @param env - shell environment used for communication
	 * @throws NullPointerException if <code>env</code> is <code>null</code>
	 */
	public TreeFileVisitor(Environment env) {
		this.env = Objects.requireNonNull(env);
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		env.writeln("|" + " ".repeat(2 * level) + dir.getFileName());
		level++;
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		env.writeln("|" + " ".repeat(2 * level) + file.getFileName());
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		level--;
		return FileVisitResult.CONTINUE;
	}
	
}
