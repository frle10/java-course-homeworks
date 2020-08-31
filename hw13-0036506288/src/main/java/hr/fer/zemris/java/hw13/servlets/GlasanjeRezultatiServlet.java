package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet prepares necessary data for a .jsp file which must show the results
 * of voting. The data prepared are two maps, one containing all bands mapped to their
 * IDs and the values being {@link BandEntry}, and the other map containing the winners
 * of the voting. 
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="glasanjeRezultati", urlPatterns={"/glasanje-rezultati"})
public class GlasanjeRezultatiServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<Integer, BandEntry> bands = Utility.getVotingResults(req);
		Map<Integer, BandEntry> winners = new TreeMap<>();
		
		int maxVotes = 0;
		for(Entry<Integer, BandEntry> entry : bands.entrySet()) {
			int votes = entry.getValue().getNumberOfVotes();
			if(votes > maxVotes) {
				maxVotes = votes;
			}
		}
		
		for(Entry<Integer, BandEntry> entry : bands.entrySet()) {
			if(entry.getValue().getNumberOfVotes() == maxVotes) {
				winners.put(entry.getKey(), entry.getValue());
			}
		}
		
		req.setAttribute("bands", bands);
		req.setAttribute("winners", winners);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
	
}
