package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This is a GUI program that can take command line arguments.
 * <p>
 * The program expects exactly one command line argument, a
 * path to a file which describes one {@link BarChart} object.
 * <p>
 * The program reads the file, constructs a new {@link BarChart}
 * object and visualizes it using a {@link BarChartComponent}
 * instance.
 * 
 * @author Ivan Skorupan
 */
public class BarChartDemo extends JFrame {

	private static final long serialVersionUID = 5151434034409506855L;
	
	/**
	 * Path to file from which the bar chart model has been parsed.
	 */
	private Path fromFile;
	
	/**
	 * The bar chart model parsed from an input file.
	 */
	private BarChart model;
	
	/**
	 * Constructs a new {@link BarChartDemo} object.
	 * 
	 * @param fromFile - file from which <code>model</code> was parsed
	 * @param model - parsed bar chart model
	 * @throws NullPointerException if any of the arguments is <code>null</code>
	 */
	public BarChartDemo(Path fromFile, BarChart model) {
		this.fromFile = Objects.requireNonNull(fromFile);
		this.model = Objects.requireNonNull(model);
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Bar Chart Demo");
		setLocation(20, 20);
		setSize(800, 600);
		initGUI();
	}
	
	/**
	 * Initializes and places GUI components on the window.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		JLabel file = new JLabel(fromFile.toAbsolutePath().normalize().toString());
		file.setHorizontalAlignment(SwingConstants.CENTER);
		cp.add(file, BorderLayout.PAGE_START);
		
		BarChartComponent barChart = new BarChartComponent(model);
		cp.add(barChart, BorderLayout.CENTER);
	}
	
	/**
	 * Entry point of this program.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("The program expects exactly one command line argument; a path to an existing file!");
			return;
		}
		
		Path file;
		try {
			file = Paths.get(args[0]);
		} catch(InvalidPathException ex) {
			System.out.println("The given path is invalid!");
			return;
		}
		
		if(!Files.isRegularFile(file) || !Files.exists(file)) {
			System.out.println("Please provide a path to an existing file as the command line argument!");
			return;
		}
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(file);
		} catch (IOException e) {
			System.out.println("There was a problem reading from given file!");
		}
		
		if(lines.size() < 6) {
			System.out.println("The given file doesn't contain enough information!");
			return;
		}
		
		BarChart model;
		try {
			model = parseModel(lines);
		} catch(IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			return;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BarChartDemo demo = new BarChartDemo(file, model);
				demo.setVisible(true);
			}
		});
	}
	
	/**
	 * Parses and returns a model of {@link BarChart} from given
	 * <code>lines</code>.
	 * <p>
	 * If there is anything wrong with the input file, an
	 * exception is thrown because no parsable {@link BarChart}
	 * model exists.
	 * 
	 * @param lines - list of lines from the input file
	 * @return a new instance {@link BarChart} parsed from <code>lines</code>
	 * @throws IllegalArgumentException if the file format is wrong
	 */
	private static BarChart parseModel(List<String> lines) {
		String xLabel = lines.get(0);
		String yLabel = lines.get(1);
		
		List<String> points = Arrays.asList(lines.get(2).split(" "));
		List<XYValue> xyValues = new LinkedList<>();
		for(String point : points) {
			String[] coords = point.split(",");
			if(coords.length != 2) {
				throw new IllegalArgumentException("The input file has a wrong format!");
			}
			
			int x = 0, y = 0;
			try {
				x = Integer.parseInt(coords[0]);
				y = Integer.parseInt(coords[1]);
			} catch(NumberFormatException ex) {
				throw new IllegalArgumentException("Point (" + point + ") has a wrong format!");
			}
			
			xyValues.add(new XYValue(x, y));
		}
		
		int minY = 0, maxY = 0, diffY = 0;
		try {
			minY = Integer.parseInt(lines.get(3));
			maxY = Integer.parseInt(lines.get(4));
			diffY = Integer.parseInt(lines.get(5));
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("The y axis bounds have a wrong format!");
		}
		
		return new BarChart(xyValues, xLabel, yLabel, minY, maxY, diffY);
	}

}
