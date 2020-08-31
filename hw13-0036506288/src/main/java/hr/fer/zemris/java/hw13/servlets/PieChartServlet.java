package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;

/**
 * This servlet writes a Pie Chart as a PNG file to the response's output stream.
 * <p>
 * The pie chart is generated with some predefined data just to test the use
 * of JFreeChart library.
 * 
 * @author Ivan Skorupan
 */
public class PieChartServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		OutputStream outputStream = resp.getOutputStream();
		
		JFreeChart chart = getChart();
		int width = 800;
		int height = 400;
		ChartUtils.writeChartAsPNG(outputStream, chart, width, height);
	}

	/**
	 * This is a helper method which builds a pie chart using some predefined hard-coded data.
	 * 
	 * @return a 3D pie chart as a {@link JFreeChart} object.
	 */
	private JFreeChart getChart() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Windows", 69.34);
		dataset.setValue("Mac OS", 23.77);
		dataset.setValue("Linux", 4.16);
		dataset.setValue("Other", 2.73);
		
		boolean legend = true;
		boolean tooltips = true;
		boolean urls = false;
		
		JFreeChart chart = ChartFactory.createPieChart3D("Which operating system are you using?", dataset, legend, tooltips, urls);
		chart.getLegend().setBorder(1, 1, 1, 1);
		
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
		
		return chart;
	}
	
}
