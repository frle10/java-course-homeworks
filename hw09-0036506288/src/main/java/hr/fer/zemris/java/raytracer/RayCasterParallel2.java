package hr.fer.zemris.java.raytracer;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerAnimator;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * This is a program that renders a predefined 3D scene containing two big
 * spheres and several small spheres using a ray-casting algorithm.
 * <p>
 * In this program, the 3D scene is animated and parallelized.
 * 
 * @author Ivan Skorupan
 */
public class RayCasterParallel2 {
	
	/**
	 * Entry point of this program.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), getIRayTracerAnimator(), 30, 30);
	}
	
	/**
	 * Returns an instance of a class that implements {@link IRayTracerAnimator}
	 * interface. Such objects are used for animating a 3D scene.
	 * 
	 * @return an instance of a class that contains an implementation of {@link IRayTracerAnimator}
	 * interface
	 */
	private static IRayTracerAnimator getIRayTracerAnimator() {
		return new IRayTracerAnimator() {
			
			/**
			 * Elapsed time.
			 */
			long time;
			
			@Override
			public void update(long deltaTime) {
				time += deltaTime;
			}
			
			@Override
			public Point3D getViewUp() { // fixed in time
				return new Point3D(0, 0, 10);
			}
			
			@Override
			public Point3D getView() { // fixed in time
				return new Point3D(-2, 0, -0.5);
			}
			
			@Override
			public long getTargetTimeFrameDuration() {
				return 150; // redraw scene each 150 milliseconds
			}
			
			@Override
			public Point3D getEye() { // changes in time
				double t = (double)time / 10000 * 2 * Math.PI;
				double t2 = (double)time / 5000 * 2 * Math.PI;
				double x = 50 * Math.cos(t);
				double y = 50 * Math.sin(t);
				double z = 30 * Math.sin(t2);
				return new Point3D(x, y, z);
			}
			
		};
	}
	
	/**
	 * Models objects that know how to produce or render a 3D scene
	 * using a ray tracing/ray casting algorithm.
	 * <p>
	 * The producer here <b>is</b> parallelized and is therefore a faster
	 * implementation than in {@link RayCaster}.
	 * 
	 * @return an instance of class that contains implementation of {@link IRayTracerProducer} interface
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
					double horizontal, double vertical, int width, int height,
					long requestNo, IRayTracerResultObserver observer, AtomicBoolean cancel) {

				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D normalizedOG = view.sub(eye).normalize();
				Point3D normalizedVUV = viewUp.normalize();
				double scalarOGAndVUV = normalizedOG.scalarProduct(normalizedVUV);

				Point3D yAxis = normalizedVUV.sub(normalizedOG.scalarMultiply(scalarOGAndVUV)).normalize();
				Point3D xAxis = normalizedOG.vectorProduct(yAxis).normalize();
				// Point3D zAxis = xAxis.vectorProduct(yAxis); --> this isn't used

				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
						.add(yAxis.scalarMultiply(vertical / 2));

				Scene scene = RayTracerViewer.createPredefinedScene2();

				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new ColorCalculationJob(horizontal, vertical, width, height, 0, height - 1,
						red, green, blue, xAxis, yAxis, screenCorner, eye, scene, cancel));

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}

		};
	}

	/**
	 * Models jobs that are implementations of {@link RecursiveAction}
	 * that know how to perform color calculation for a pixel based on
	 * Phong model.
	 * <p>
	 * These jobs know how to split themselves into smaller versions
	 * of themselves so that each one works on a maximum of
	 * {@link #THRESHOLD} number of picture rows. 
	 * 
	 * @author Ivan Skorupan
	 */
	public static class ColorCalculationJob extends RecursiveAction {

		private static final long serialVersionUID = -614402955545739063L;
		
		/**
		 * Maximum tolerated difference between two doubles for them to be considered equal.
		 */
		private static final double DELTA = 1e-6;
		
		/**
		 * Maximum number of rows a thread should get for it to start working
		 * on its part of the scene.
		 */
		private static final int THRESHOLD = 16;

		/**
		 * Width of the part of Euclidean plane we want to render.
		 */
		private double horizontal;
		
		/**
		 * Height of the part of Euclidean plane we want to render.
		 */
		private double vertical;
		
		/**
		 * GUI window width.
		 */
		private int width;
		
		/**
		 * GUI window height.
		 */
		private int height;
		
		/**
		 * Minimum y coordinate this job should work on.
		 */
		private int yMin;
		
		/**
		 * Maximum y coordinate this job should work on.
		 */
		private int yMax;
		
		/**
		 * Red color component for each pixel (width * height elements).
		 */
		private short[] red;
		
		/**
		 * Green color component for each pixel (width * height elements).
		 */
		private short[] green;
		
		/**
		 * Blue color component for each pixel (width * height elements).
		 */
		private short[] blue;
		
		/**
		 * Unit vector representing the direction of the x-axis.
		 */
		private Point3D xAxis;
		
		/**
		 * Unit vector representing the direction of y-axis.
		 */
		private Point3D yAxis;
		
		/**
		 * A point that represents the upper left corner of the window.
		 */
		private Point3D screenCorner;
		
		/**
		 * Position of the observer.
		 */
		private Point3D eye;
		
		/**
		 * The 3D scene we're working with.
		 */
		private Scene scene;
		
		/**
		 * Object used to cancel rendering of image.
		 */
		private AtomicBoolean cancel;
		
		/**
		 * Constructs a new {@link ColorCalculationJob} object and takes
		 * all parameters needed for the job to be able to perform
		 * its calculations.
		 * 
		 * @param horizontal - width of the part of Euclidean plane we want to render
		 * @param vertical - height of the part of Euclidean plane we want to render
		 * @param width - GUI window width
		 * @param height - GUI window height
		 * @param yMin - minimum y coordinate this job should work on
		 * @param yMax - maximum y coordinate this job should work on
		 * @param red - red color component for each pixel
		 * @param green - green color component for each pixel
		 * @param blue - blue color component for each pixel
		 * @param xAxis - unit vector representing the direction of x-axis
		 * @param yAxis - unit vector representing the direction of y-axis
		 * @param screenCorner - a point that represents the upper left corner of the window
		 * @param eye - position of the observer
		 * @param scene - the 3D scene we're working with
		 * @param cancel - object used to cancel rendering of image
		 * @throws NullPointerException if any of: <code>red, green, blue, xAxis, yAxis, screenCorner, eye, scene, cancel</code>
		 * arguments are <code>null</code>
		 */
		public ColorCalculationJob(double horizontal, double vertical, int width, int height, int yMin, int yMax, short[] red,
				short[] green, short[] blue, Point3D xAxis, Point3D yAxis, Point3D screenCorner, Point3D eye, Scene scene, AtomicBoolean cancel) {
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.red = Objects.requireNonNull(red);
			this.green = Objects.requireNonNull(green);
			this.blue = Objects.requireNonNull(blue);
			this.xAxis = Objects.requireNonNull(xAxis);
			this.yAxis = Objects.requireNonNull(yAxis);
			this.screenCorner = Objects.requireNonNull(screenCorner);
			this.eye = Objects.requireNonNull(eye);
			this.scene = Objects.requireNonNull(scene);
			this.cancel = Objects.requireNonNull(cancel);
		}

		@Override
		public void compute() {
			if(yMax - yMin + 1 <= THRESHOLD) {
				computeDirect();
				return;
			}

			invokeAll(
					new ColorCalculationJob(horizontal, vertical, width, height, yMin, yMin + (yMax - yMin) / 2,
							red, green, blue, xAxis, yAxis, screenCorner, eye, scene, cancel),
					new ColorCalculationJob(horizontal, vertical, width, height, yMin + (yMax - yMin) / 2 + 1, yMax,
							red, green, blue, xAxis, yAxis, screenCorner, eye, scene, cancel)
					);
		}
		
		/**
		 * This is the actual computation implementation of Newtwon-Rhapson method that each
		 * thread will execute.
		 * <p>
		 * The method is implemented so that every thread gets its own custom area of the screen
		 * to work on so that the image is properly rendered, and fast.
		 */
		private void computeDirect() {
			short[] rgb = new short[3];
			int offset = yMin * width;
			for(int y = yMin; y <= yMax; y++) {
				for(int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(horizontal * x / (width - 1)))
							.sub(yAxis.scalarMultiply(vertical * y / (height - 1)));
					Ray ray = Ray.fromPoints(eye, screenPoint);

					tracer(scene, ray, rgb);

					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

					offset++;
				}
			}
		}
		
		/**
		 * This method contains the core implementation of ray casting algorithm used for
		 * determining the color of a pixel. The algorithm also uses a simple Phong model
		 * for determining diffuse and reflective component of pixel color.
		 * 
		 * @param scene - the 3D scene we're working with
		 * @param ray - ray that's defined by eye position and screen point
		 * @param rgb - an array that will contain the final rgb component for this pixel
		 * @throws NullPointerException if <code>scene</code> or <code>ray</code> is <code>null</code>
		 */
		private void tracer(Scene scene, Ray ray, short[] rgb) {
			Objects.requireNonNull(scene);
			Objects.requireNonNull(ray);
			Objects.requireNonNull(rgb);
			
			rgb[0] = 0;
			rgb[1] = 0;
			rgb[2] = 0;

			RayIntersection closest = findClosestIntersection(scene, ray);

			if(closest == null) {
				return;
			}

			/*
			 * This was used for black-white image.
			 * rgb[0] = 255;
			 * rgb[1] = 255;
			 * rgb[2] = 255;
			 */

			// set the ambient component --> rgb(15, 15, 15)
			rgb[0] = 15;
			rgb[1] = 15;
			rgb[2] = 15;

			List<LightSource> lightSources = scene.getLights();
			for(LightSource lightSource : lightSources) {
				Ray lightToObject = Ray.fromPoints(lightSource.getPoint(), closest.getPoint());
				RayIntersection closestLitObject = findClosestIntersection(scene, lightToObject);

				if(closestLitObject != null) {
					double distanceToClosestLitObject = closestLitObject.getDistance();
					double distanceToObject = lightSource.getPoint().sub(closest.getPoint()).norm();

					if(distanceToClosestLitObject + DELTA < distanceToObject) {
						continue;
					}
				} else continue;

				rgb[0] += calculateDiffuseComponent(lightSource.getR(), closestLitObject.getKdr(), closestLitObject, lightToObject)
						+ calculateReflectiveComponent(lightSource.getR(), closestLitObject.getKrr(), closestLitObject.getKrn(), closestLitObject, lightToObject, ray);

				rgb[1] += calculateDiffuseComponent(lightSource.getG(), closestLitObject.getKdg(), closestLitObject, lightToObject)
						+ calculateReflectiveComponent(lightSource.getG(), closestLitObject.getKrg(), closestLitObject.getKrn(), closestLitObject, lightToObject, ray);

				rgb[2] += calculateDiffuseComponent(lightSource.getB(), closestLitObject.getKdb(), closestLitObject, lightToObject)
						+ calculateReflectiveComponent(lightSource.getB(), closestLitObject.getKrb(), closestLitObject.getKrn(), closestLitObject, lightToObject, ray);
			}
		}
		
		/**
		 * Calculates the diffuse component of current pixel using the Phong model.
		 * 
		 * @param colorIntensity - light intensity of the light source for a certain color
		 * @param kd - diffuse coefficient for a certain color
		 * @param closestLitObject - a point on an object closest to the light source where the
		 * light ray and ray from observer's eye intersect
		 * @param lightToObject - a ray that's defined by the light source's position and intersection with the object closest to it
		 * @return diffuse component of current pixel
		 * @throws NullPointerException if <code>closestLitObject</code> or <code>lightToObject</code> is <code>null</code>
		 */
		private int calculateDiffuseComponent(int colorIntensity, double kd, RayIntersection closestLitObject, Ray lightToObject) {
			Objects.requireNonNull(closestLitObject);
			Objects.requireNonNull(lightToObject);
			
			Point3D normal = closestLitObject.getNormal();
			Point3D objectToLight = lightToObject.start.sub(closestLitObject.getPoint()).normalize();

			double scalarProduct = normal.scalarProduct(objectToLight);
			double Id = colorIntensity * kd * scalarProduct;

			return (Id < 0) ? 0 : (int) Math.round(Id);
		}
		
		/**
		 * Calculates the diffuse component of current pixel using the Phong model.
		 * 
		 * @param colorIntensity - light intensity of the light source for a certain color
		 * @param kr - reflective coefficient for a certain color
		 * @param krn - the shininess factor
		 * @param closestLitObject - a point on an object closest to the light source where the
		 * light ray and ray from observer's eye intersect
		 * @param lightToObject - a ray that's defined by the light source's position and intersection with the object closest to it
		 * @param eyeToPixel
		 * @return reflective component of current pixel
		 * @throws NullPointerException if <code>closestLitObject</code>, <code>lightToObject</code> or <code>eyeToPixel</code>
		 * is <code>null</code>
		 */
		private int calculateReflectiveComponent(int colorIntensity, double kr, double krn, RayIntersection closestLitObject, Ray lightToObject, Ray eyeToPixel) {
			Objects.requireNonNull(closestLitObject);
			Objects.requireNonNull(lightToObject);
			Objects.requireNonNull(eyeToPixel);
			
			Point3D normal = closestLitObject.getNormal();
			Point3D objectToLight = lightToObject.start.sub(closestLitObject.getPoint()).normalize();

			Point3D reflected = normal.scalarMultiply(2).scalarMultiply(normal.scalarProduct(objectToLight)).sub(objectToLight).normalize();

			Point3D intersectionToEye = eyeToPixel.start.sub(closestLitObject.getPoint()).normalize();

			double Is = colorIntensity * kr * pow(intersectionToEye.scalarProduct(reflected), krn);

			return (int) Math.round(Is);
		}
		
		/**
		 * Calculates intersections of <code>ray</code> and every object in
		 * the <code>scene</code> and then returns the intersection that's
		 * closest to the starting point of <code>ray</code>.
		 * <p>
		 * If <code>ray</code> does not intersect with any object in the scene
		 * or if all intersections are behind the observer defined by <code>ray</code>
		 * (which means they are not considered since they cannot be seen in the scene),
		 * <code>null</code> is returned.
		 * 
		 * @param scene - 3D scene we're working with
		 * @param ray - a ray that defines the position of the observer and its looking direction
		 * @return closest intersection of <code>ray</code> with any object in the <code>scene</code> or
		 * <code>null</code> if no such intersection exists
		 * @throws NullPointerException if any of the arguments are <code>null</code>
		 */
		private RayIntersection findClosestIntersection(Scene scene, Ray ray) {
			Objects.requireNonNull(scene);
			Objects.requireNonNull(ray);
			
			List<GraphicalObject> graphicalObjects = scene.getObjects();
			List<RayIntersection> intersections = new ArrayList<>();

			for(GraphicalObject graphicalObject : graphicalObjects) {
				RayIntersection intersection = graphicalObject.findClosestRayIntersection(ray);
				if(intersection != null) {
					intersections.add(intersection);
				}
			}

			if(intersections.isEmpty()) return null;

			RayIntersection closest = intersections.get(0);
			double closestDistance = closest.getDistance();
			for(RayIntersection intersection : intersections) {
				if(intersection.getDistance() + DELTA < closestDistance) {
					closest = intersection;
					closestDistance = intersection.getDistance();
				}
			}

			return closest;
		}

	}

}
