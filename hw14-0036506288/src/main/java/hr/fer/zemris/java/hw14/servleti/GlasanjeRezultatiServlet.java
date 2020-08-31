package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.PollOptionsEntry;
import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * This servlet prepares necessary data for a .jsp file which must show the results
 * of voting. The data prepared are two maps, one containing all bands mapped to their
 * IDs and the values being {@link BandEntry}, and the other map containing the winners
 * of the voting. 
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="glasanjeRezultati", urlPatterns={"/servleti/glasanje-rezultati"})
public class GlasanjeRezultatiServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollID = Long.parseLong(req.getParameter("id"));
		List<PollOptionsEntry> pollOptions = DAOProvider.getDao().fetchPollOptionsByPollID(pollID);
		List<PollOptionsEntry> winners = new ArrayList<>();
		
		long maxVotes = 0;
		for(PollOptionsEntry entry : pollOptions) {
			if(entry.getVotesCount() > maxVotes) {
				maxVotes = entry.getVotesCount();
			}
		}
		
		for(PollOptionsEntry entry : pollOptions) {
			if(entry.getVotesCount() == maxVotes) {
				winners.add(entry);
			}
		}
		
		pollOptions.sort((poe1, poe2) -> Long.compare(poe2.getVotesCount(), poe1.getVotesCount()));
		req.setAttribute("pollOptions", pollOptions);
		req.setAttribute("winners", winners);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp?id=" + pollID).forward(req, resp);
	}
	
}
