package hr.fer.zemris.java.raytracer;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
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
 * This version of the program is not parallelized so it's a little
 * slower than {@link RayCasterParallel}.
 * 
 * @author Ivan Skorupan
 */
public class RayCaster {
	
	/**
	 * Entry point of this program.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(),
				new Point3D(10,0,0),
				new Point3D(0,0,0),
				new Point3D(0,0,10),
				20, 20);
	}

	/**
	 * Models objects that know how to produce or render a 3D scene
	 * using a ray tracing/ray casting algorithm.
	 * <p>
//	 * The producer here is <b>not</b> parallelized and is therefore a slower
	 * implementation than what it could be since color calculation
	 * for each pixel is independent of other pixels.
	 * 
	 * @return an instance of class that contains implementation of {@link IRayTracerProducer} interface
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			
			/**
			 * Maximum tolerated difference between two doubles for them to be considered equal.
			 */
			private static final double DELTA = 1e-6;
			
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

				Scene scene = RayTracerViewer.createPredefinedScene();

				short[] rgb = new short[3];
				int offset = 0;
				for(int y = 0; y < height; y++) {
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

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
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
				
				rgb[0] = 0;
				rgb[1] = 0;
				rgb[2] = 0;

				RayIntersection closest = findClosestIntersection(scene, ray);

				if(closest == null) {
					return;
				}

				/*
				 * This was used for black and white image.
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

		};
	}
	
}
