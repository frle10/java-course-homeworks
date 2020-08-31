package hr.fer.zemris.java.zi.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="grafika", urlPatterns={"/main"})
public class StartServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Path imagesPath = Paths.get(req.getServletContext().getRealPath("/WEB-INF/images"));
		
		if(!Files.isDirectory(imagesPath)) {
			Files.createDirectory(imagesPath);
		}
		
		MyVisitor myVisitor = new MyVisitor();
		Files.walkFileTree(imagesPath, myVisitor);
		
		List<String> imageNames = myVisitor.getPaths();
		imageNames.sort((s1, s2) -> Collator.getInstance(Locale.forLanguageTag("hr")).compare(s1, s2));
		req.setAttribute("images", imageNames);
		
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
	
}
