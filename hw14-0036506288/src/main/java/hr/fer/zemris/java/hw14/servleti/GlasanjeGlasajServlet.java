package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.PollOptionsEntry;
import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * This servlet processes a vote request sent by the client. The processing is
 * simply done by updating the appropriate number of votes entry in the
 * appropriate file and then further action is delegated to a .jsp file.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="glasanjeGlasaj", urlPatterns={"/servleti/glasanje-glasaj"})
public class GlasanjeGlasajServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollID = Long.parseLong(req.getParameter("id"));
		long optionID = Long.parseLong(req.getParameter("optionID"));
		PollOptionsEntry pollOption = DAOProvider.getDao().fetchPollOptionByID(optionID);
		long votesCount = pollOption.getVotesCount();
		DAOProvider.getDao().setVotesCountForOption(optionID, votesCount + 1);
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/glasanje-rezultati?id=" + pollID));
	}
	
}
