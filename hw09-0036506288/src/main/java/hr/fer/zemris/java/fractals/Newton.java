package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;
import hr.fer.zemris.math.complex.parser.ComplexParser;
import hr.fer.zemris.math.complex.parser.ComplexParserException;

/**
 * This is a command line + GUI program that takes no command line
 * arguments, but asks the user to enter several roots of a
 * complex polynomial (at least 2) and then it initializes the
 * GUI where it draws the fractal using the Newton-Raphson iteration
 * method.
 * 
 * @author Ivan Skorupan
 */
public class Newton {
	
	/**
	 * String that, when entered by the user, signals the program that all roots were entered.
	 */
	private static final String DONE_COMMAND = "done";
	
	/**
	 * Entry point of this program.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		int rootNumber = 1;
		List<Complex> rootsList = new ArrayList<>();
		
		while(true) {
			System.out.print("Root " + rootNumber + "> ");
			String input = scanner.nextLine().trim();
			
			if(input.equals(DONE_COMMAND)) {
				if(rootNumber < 3) {
					System.out.println("You were supossed to enter at least 2 roots! Add more of them.");
					continue;
				}
				break;
			}
			
			ComplexParser parser = null;
			try {
				parser = new ComplexParser(input);
			} catch(ComplexParserException ex) {
				System.out.println(ex.getMessage());
				continue;
			}
			
			rootsList.add(parser.getComplexNumber());
			rootNumber++;
		}
		
		Complex[] roots = new Complex[rootsList.size()];
		for(int i = 0; i < roots.length; i++) {
			roots[i] = rootsList.get(i);
		}
		
		ComplexRootedPolynomial polynomial = new ComplexRootedPolynomial(Complex.ONE, roots);
		System.out.println("Image of fractal will appear shortly. Thank you.");
		scanner.close();
		FractalViewer.show(new NewtonFractalProducer(polynomial));
	}
	
	/**
	 * Models objects that know how to perform a Newton-Rhapson iteration method
	 * on given polynomial for a given set of pixels.
	 * 
	 * @author Ivan Skorupan
	 */
	public static class NewtonRhapsonCalculation implements Callable<Void> {
		
		/**
		 * Maximum difference between current iteration value and last
		 * iteration value for the polynomial to be considered convergent.
		 */
		private static final double CONVERGENCE_THRESHOLD = 1e-3;
		
		/**
		 * Maximum distance between any of the polynomial roots and
		 * approximated polynomial value for the value to be considered
		 * in calculating the index of closest root.
		 */
		private static final double ROOT_THRESHOLD = 2e-3;
		
		/**
		 * Minimum real part of complex plane for pixels to be mapped to.
		 */
		double reMin;
		
		/**
		 * Maximum real part of complex plane for pixels to be mapped to.
		 */
		double reMax;
		
		/**
		 * Minimum imaginary part of complex plane for pixels to be mapped to.
		 */
		double imMin;
		
		/**
		 * Maximum imaginary part of complex plane for pixels to be mapped to.
		 */
		double imMax;
		
		/**
		 * GUI window width.
		 */
		int width;
		
		/**
		 * GUI window height.
		 */
		int height;
		
		/**
		 * Minimum y coordinate this job should work on.
		 */
		int yMin;
		
		/**
		 * Maximum y coordinate this job should work on.
		 */
		int yMax;
		
		/**
		 * Maximum number of iterations to perform.
		 */
		int m;
		
		/**
		 * An array containing the resulting data for each pixel (width * height elements).
		 */
		short[] data;
		
		/**
		 * Object used to cancel rendering of image.
		 */
		AtomicBoolean cancel;
		
		/**
		 * Polynomial on which the Newton-Rhapson iterations are performed in rooted form.
		 */
		ComplexRootedPolynomial rootedPolynomial;
		
		/**
		 * Polynomial on which the Newton-Rhapson iterations are performed in standard form.
		 */
		ComplexPolynomial polynomial;
		
		/**
		 * Constructs a new {@link NewtonRhapsonCalculation} object and takes
		 * all parameters needed for the job to be able to perform the
		 * iterations.
		 * 
		 * @param reMin - minimum real part of complex plane for pixels to be mapped to
		 * @param reMax - maximum real part of complex plane for pixels to be mapped to
		 * @param imMin - minimum imaginary part of complex plane for pixels to be mapped to
		 * @param imMax - maximum imaginary part of complex plane for pixels to be mapped to
		 * @param width - GUI window width
		 * @param height - GUI window height
		 * @param yMin - minimum y coordinate this job should work on
		 * @param yMax - maximum y coordinate this job should work on
		 * @param m - maximum number of iterations to perform
		 * @param data - an array containing the resulting data for each pixel
		 * @param rootedPolynomial - polynomial on which the Newton-Rhapson iterations are performed in rooted form
		 * @throws NullPointerException if any of: <code>data, cancel, rootedPolynomial</code>
		 * arguments are <code>null</code>
		 */
		public NewtonRhapsonCalculation(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				int m, short[] data, ComplexRootedPolynomial rootedPolynomial) {
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = Objects.requireNonNull(data);
			this.cancel = Objects.requireNonNull(cancel);
			this.rootedPolynomial = Objects.requireNonNull(rootedPolynomial);
			this.polynomial = rootedPolynomial.toComplexPolynom();
		}
		
		@Override
		public Void call() {
			int offset = yMin * width;
			for(int i = yMin; i <= yMax; i++) {
				for(int j = 0; j < width; j++) {
					Complex c = mapToComplexPlain(j, i, 0, width - 1, 0, height - 1, reMin, reMax, imMin, imMax);
					Complex zn = c;
					Complex znOld = null;
					double module = 0;
					int iter = 0;
					
					do {
						znOld = zn;
						zn = zn.sub(polynomial.apply(zn).divide(polynomial.derive().apply(zn)));
						module = znOld.sub(zn).module();
						iter++;
					} while(module > CONVERGENCE_THRESHOLD && iter < m);
					
					int index = rootedPolynomial.indexOfClosestRootFor(zn, ROOT_THRESHOLD);
					data[offset++] = (short) (index + 1);
				}
			}
			
			return null;
		}
		
		/**
		 * Maps the given pixel defined by its coordinates on the screen to the area
		 * of complex plane defined by <code>reMin, reMax, imMin, imMax</code>.
		 * 
		 * @param x - x coordinate of current pixel
		 * @param y - y coordinate of current pixel
		 * @param xMin
		 * @param xMax
		 * @param yMin
		 * @param yMax
		 * @param reMin - lowest x-axis value of the complex plane
		 * @param reMax - highest x-axis value of the complex plane
		 * @param imMin - lowest y-axis value of the complex plane
		 * @param imMax - highest y-axis value of the complex plane
		 * @return a complex number mapped to the defined complex plane area from the given pixel
		 */
		private Complex mapToComplexPlain(int x, int y, int xMin, int xMax, int yMin, int yMax, double reMin,
				double reMax, double imMin, double imMax) {
			double real = (x * (reMax - reMin) / (xMax - xMin)) + reMin;
			double imaginary = imMax - (y * (imMax - imMin) / (yMax - yMin));
			
			return new Complex(real, imaginary);
		}
		
	}
	
	/**
	 * This class is an implementation of {@link IFractalProducer} that
	 * produces fractals using the Newton-Raphson iteration method.
	 * 
	 * @author Ivan Skorupan
	 */
	public static class NewtonFractalProducer implements IFractalProducer {
		
		/**
		 * Maximum number of iterations for Newton-Rhapson calculation.
		 */
		private static final int MAX_ITER = 4096;
		
		/**
		 * Polynomial used for fractal generation.
		 */
		private ComplexRootedPolynomial polynomial;

		/**
		 * Constructs a new {@link NewtonFractalProducer} object
		 * initialized with a polynomial to use for fractal
		 * generation.
		 * 
		 * @param polynomial - polynomial to use for fractal generation
		 * @throws NullPointerException if <code>polynomial</code> is <code>null</code>
		 */
		public NewtonFractalProducer(ComplexRootedPolynomial polynomial) {
			this.polynomial = Objects.requireNonNull(polynomial);
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer) {
			short[] data = new short[width * height];
			
			final int numberOfTracks = height / (8 * Runtime.getRuntime().availableProcessors());
			int numberOfRowsPerTrack = height / numberOfTracks;
			
			ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new DaemonicThreadFactory());
			List<Future<Void>> results = new ArrayList<>();
			
			for(int i = 0; i < numberOfTracks; i++) {
				int minY = i * numberOfRowsPerTrack;
				int maxY = (i + 1) * numberOfRowsPerTrack - 1;
				
				if(i == numberOfTracks - 1) {
					maxY = height - 1;
				}
				
				NewtonRhapsonCalculation job = new NewtonRhapsonCalculation(reMin, reMax, imMin, imMax, width, height, minY, maxY, MAX_ITER, data, polynomial);
				results.add(pool.submit(job));
			}
			
			for(Future<Void> posao : results) {
				try {
					posao.get();
				} catch (InterruptedException | ExecutionException e) {
					
				}
			}
			
			pool.shutdown();
			observer.acceptResult(data, (short) (polynomial.toComplexPolynom().order() + 1), requestNo);
		}
		
	}
	
	/**
	 * This class is an implementation of {@link ThreadFactory} which
	 * produces threads with daemonic flag set to <code>true</code>.
	 * 
	 * @author Ivan Skorupan
	 */
	public static class DaemonicThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable r) {
			Objects.requireNonNull(r);
			Thread thread = new Thread(r);
			thread.setDaemon(true);
			return thread;
		}
		
	}
	
}
