package hr.fer.zemris.java.zi.servlets;

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

import hr.fer.zemris.java.hw17.jvdraw.Util;

@WebServlet(name="inforrr", urlPatterns={"/imageInfo"})
public class ImageInfoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getParameter("id");
		
		int lines = 0;
		int circles = 0;
		int fCircles = 0;
		int fTriangles = 0;
		
		Path file = Paths.get(req.getServletContext().getRealPath("/WEB-INF/images/" + fileName));
		List<String> fileLines = Files.readAllLines(file);
		
		for(String line : fileLines) {
			if(line.trim().startsWith(Util.LINE_IDENTIFIER)) {
				lines++;
			} else if(line.trim().startsWith(Util.CIRCLE_IDENTIFIER)) {
				circles++;
			} else if(line.trim().startsWith(Util.FILLED_CIRCLE_IDENTIFIER)) {
				fCircles++;
			} else if(line.trim().startsWith(Util.FILLED_TRIANGLE_IDENTIFIER)) {
				fTriangles++;
			}
		}
		
		req.getSession().setAttribute("fileName", fileName);
		req.getSession().setAttribute("lines", lines);
		req.getSession().setAttribute("circles", circles);
		req.getSession().setAttribute("fCircles", fCircles);
		req.getSession().setAttribute("fTriangles", fTriangles);

		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/temp"));
	}
	
}
