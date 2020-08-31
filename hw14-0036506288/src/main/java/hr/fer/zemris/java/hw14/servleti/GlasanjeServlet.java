package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.PollEntry;
import hr.fer.zemris.java.hw14.PollOptionsEntry;
import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * This servlet prepares the necessary data for a .jsp file which renders a
 * .html page on which the client can vote for a certain band.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="glasanje", urlPatterns={"/servleti/glasanje"})
public class GlasanjeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollID = Long.parseLong(req.getParameter("id"));
		List<PollOptionsEntry> pollOptions = DAOProvider.getDao().fetchPollOptionsByPollID(pollID);
		PollEntry poll = DAOProvider.getDao().fetchPollByID(pollID);
		
		req.setAttribute("pollOptions", pollOptions);
		req.setAttribute("poll", poll);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
	
}
