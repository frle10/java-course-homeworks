package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet processes a vote request sent by the client. The processing is
 * simply done by updating the appropriate number of votes entry in the
 * appropriate file and then further action is delegated to a .jsp file.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="glasanjeGlasaj", urlPatterns={"/glasanje-glasaj"})
public class GlasanjeGlasajServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Path file = Paths.get(fileName);
		
		if(!Files.exists(file)) {
			Files.createFile(file);
		}
		
		Integer voteId = Integer.parseInt(req.getParameter("id"));
		
		List<String> lines = Files.readAllLines(file);
		boolean lineExists = false;
		for(int i = 0; i < lines.size(); i++) {
			String currentLine = lines.get(i);
			
			if(currentLine.startsWith(voteId.toString())) {
				String[] currentLineTokens = currentLine.split("\\t");
				int oldValue = Integer.parseInt(currentLineTokens[1]);
				String newLine = voteId.toString() + "\t" + (oldValue + 1);
				lines.set(i, newLine);
				lineExists = true;
			}
		}
		
		if(!lineExists) {
			lines.add(voteId.toString() + "\t" + 1);
		}
		
		lines.sort((s1, s2) -> s1.compareTo(s2));
		Files.write(file, lines);
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
	
}
