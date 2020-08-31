package hr.fer.zemris.java.raytracer.model;

import static java.lang.Math.*;

import java.util.Objects;

/**
 * This class models a sphere in 3D space.
 * <p>
 * A sphere's center is positioned somewhere in 3D space, it is
 * an instance of {@link Point3D} and it has a certain radius.
 * <p>
 * Also, coefficients for each rgb color of the sphere's diffusion
 * and reflective component are defined, as well as the shininess factor n.
 * 
 * @author Ivan Skorupan
 */
public class Sphere extends GraphicalObject {
	
	private static final double DELTA = 1e-6;
	
	/**
	 * Center of this sphere.
	 */
	private Point3D center;
	
	/**
	 * This spehere's radius.
	 */
	private double radius;
	
	/**
	 * Coefficient for red color of diffuse component.
	 */
	private double kdr;
	
	/**
	 * Coefficient for green color of diffuse component.
	 */
	private double kdg;
	
	/**
	 * Coefficient for blue color of diffuse component.
	 */
	private double kdb;
	
	/**
	 * Coefficient for red color of reflective component.
	 */
	private double krr;
	
	/**
	 * Coefficient for green color of reflective component.
	 */
	private double krg;
	
	/**
	 * Coefficient for blue color of reflective component.
	 */
	private double krb;
	
	/**
	 * Shininess factor of the reflective component.
	 */
	private double krn;
	
	/**
	 * Constructs a new {@link Sphere} object,
	 * initialized with its center and radius along
	 * with diffuse and reflective component
	 * coefficients for each rgb color.
	 * 
	 * @param center - this sphere's center position
	 * @param radius - this sphere's radius
	 * @param kdr - coefficient for red color of diffuse component
	 * @param kdg - coefficient for green color of diffuse component
	 * @param kdb - coefficient for blue color of diffuse component
	 * @param krr - coefficient for red color of reflective component
	 * @param krg - coefficient for green color of reflective component
	 * @param krb - coefficient for blue color of reflective component
	 * @param krn - shininess factor of the reflective component
	 * @throws NullPointerException if <code>center</code> is <code>null</code>
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		this.center = Objects.requireNonNull(center);
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}
	
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Objects.requireNonNull(ray);
		Point3D fromEyeToCenter = ray.start.sub(center);
		
		// we're solving a quadratic formula where a = 1, so we need to define b and c
		double b =  2 * (ray.direction.scalarProduct(fromEyeToCenter));
		double c = fromEyeToCenter.scalarProduct(fromEyeToCenter) - radius * radius;
		
		double discriminant = b * b - 4 * c;
		// if there are no solutions to the equation, there are no intersections
		if(discriminant < 0) {
			return null;
		}
		
		double lambda1 = (-b + sqrt(discriminant)) / 2;
		double lambda2 = (-b - sqrt(discriminant)) / 2;
		
		final Point3D intersection;
		double distance = 0;
		boolean outer = true;
		
		// if there is one double solution and it is less than zero, the intersection is
		// behind the observer so it is not taken into account because it can't be seen
		// NOTE! Possibly watch out for double variable comparison precision.
		if(abs(lambda1 - lambda2) < DELTA && lambda1 <= 0) {
			intersection = null;
		} else if(abs(lambda1 - lambda2) < DELTA && lambda1 > 0) {
			// if the double solution is greater than zero, there is exactly one
			// intersection which we need to return
			distance = lambda1;
			intersection = ray.start.add(ray.direction.scalarMultiply(distance));
		} else if(lambda1 > 0 && lambda2 > 0) {
			// if there are two distinct solutions and they are both greater than 0,
			// we take the smaller one
			distance = min(lambda1, lambda2);
			intersection = ray.start.add(ray.direction.scalarMultiply(distance));
		} else if(lambda1 > 0 && lambda2 <= 0 || lambda1 <= 0 && lambda2 > 0) {
			// if exactly one of the solutions is less than zero, we take the other one
			distance = max(lambda1, lambda2);
			intersection = ray.start.add(ray.direction.scalarMultiply(distance));
			outer = false;
		} else {
			intersection = null;
		}
		
		return (intersection == null) ? null : new RayIntersection(intersection, distance, outer) {
			
			@Override
			public Point3D getNormal() {
				return intersection.sub(center).normalize();
			}
			
			@Override
			public double getKrr() {
				return krr;
			}
			
			@Override
			public double getKrn() {
				return krn;
			}
			
			@Override
			public double getKrg() {
				return krg;
			}
			
			@Override
			public double getKrb() {
				return krb;
			}
			
			@Override
			public double getKdr() {
				return kdr;
			}
			
			@Override
			public double getKdg() {
				return kdg;
			}
			
			@Override
			public double getKdb() {
				return kdb;
			}
		};
	}
	
}
