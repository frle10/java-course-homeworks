package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import hr.fer.zemris.java.hw14.PollOptionsEntry;
import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * This servlet creates and writes a bar chart showing voting results to the user.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="glasanjeGrafika", urlPatterns={"/servleti/glasanje-grafika"})
public class VotingGraphicsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long pollID = Long.parseLong(req.getParameter("id"));
		List<PollOptionsEntry> pollOptions = DAOProvider.getDao().fetchPollOptionsByPollID(pollID);
		pollOptions.sort((poe1, poe2) -> Long.compare(poe2.getVotesCount(), poe1.getVotesCount()));
		
		resp.setContentType("image/png");
		OutputStream outputStream = resp.getOutputStream();
		
		JFreeChart chart = getChart(pollOptions);
		int width = 640;
		int height = 480;
		ChartUtils.writeChartAsPNG(outputStream, chart, width, height);
	}
	
	/**
	 * Helper method which generates the necessary chart and fills it with data and
	 * also sets some design properties of the chart.
	 * 
	 * @return the bar chart as a {@link JFreeChart} object
	 */
	private JFreeChart getChart(List<PollOptionsEntry> pollOptions) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for(PollOptionsEntry entry : pollOptions) {
			dataset.addValue(entry.getVotesCount(), "Votes", entry.getOptionTitle());
		}
		
		boolean legend = false;
		boolean tooltips = true;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createBarChart("Rezultati glasanja", "Opcije", "Glasova",
				dataset, PlotOrientation.VERTICAL, legend, tooltips, urls);
		
		chart.getCategoryPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		chart.getCategoryPlot().getDomainAxis().setMaximumCategoryLabelLines(2);
		return chart;
	}
	
}
