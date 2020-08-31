package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.PollEntry;
import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * This servlet uses DAO to fetch all polls available to this web application and then
 * delegates the HTML rendering work to an appropriate .jsp file.
 * <p>
 * The idea of this servlet is to prepare all necessary data to show the user all available
 * polls which can be accessed by the client computer.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="polls", urlPatterns={"/servleti/index.html"})
public class PollsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<PollEntry> polls = DAOProvider.getDao().fetchAllPolls();
		req.setAttribute("polls", polls);
		req.getRequestDispatcher("/WEB-INF/pages/polls.jsp").forward(req, resp);
	}
	
}
