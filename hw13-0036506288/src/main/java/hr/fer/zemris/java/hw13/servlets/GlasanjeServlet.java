package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet prepares the necessary data for a .jsp file which renders a
 * .html page on which the client can vote for a certain band.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="glasanje", urlPatterns={"/glasanje"})
public class GlasanjeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<Integer, BandEntry> bands = Utility.getBandsMap(req);
		req.setAttribute("bands", bands);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
}
