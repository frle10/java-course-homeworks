package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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

/**
 * This servlet creates and writes a bar chart showing voting results to the user.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="glasanjeGrafika", urlPatterns={"/glasanje-grafika"})
public class VotingGraphicsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<Integer, BandEntry> bands = Utility.getVotingResults(req);
		
		resp.setContentType("image/png");
		OutputStream outputStream = resp.getOutputStream();
		
		JFreeChart chart = getChart(bands);
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
	private JFreeChart getChart(Map<Integer, BandEntry> bands) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<Entry<Integer, BandEntry>> entryList = bands.entrySet().stream()
				.sorted((e1, e2) -> e2.getValue().getNumberOfVotes().compareTo(e1.getValue().getNumberOfVotes()))
				.collect(Collectors.toList());
		
		for(Entry<Integer, BandEntry> entry : entryList) {
			dataset.addValue(entry.getValue().getNumberOfVotes(), "Votes", entry.getValue().getBandName());
		}
		
		boolean legend = false;
		boolean tooltips = true;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createBarChart("Rezultati glasanja", "Bendovi", "Glasova",
				dataset, PlotOrientation.VERTICAL, legend, tooltips, urls);
		
		chart.getCategoryPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		chart.getCategoryPlot().getDomainAxis().setMaximumCategoryLabelLines(2);
		return chart;
	}
	
}
