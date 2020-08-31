package hr.fer.zemris.java.zi.servlets;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class MyVisitor extends SimpleFileVisitor<Path> {
	
	private List<String> paths = new ArrayList<>();
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		if(file.getFileName().toString().substring(file.getFileName().toString().lastIndexOf(".") + 1).equals("jvd")) {
			paths.add(file.getFileName().toString());
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	public List<String> getPaths() {
		return paths;
	}
	
}
