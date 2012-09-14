package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;


public interface PointParticle {

	public static final double STOPPED = 0;
	public static final double RIGHT = 0,
	                           UP = FastMath.PI / 2d,
	    	                   LEFT = FastMath.PI,
	    	    	           DOWN = 3d * FastMath.PI / 2d;

	public double x();
	public double y();
	public Point position();
	public PointParticle x(double x);
	public PointParticle y(double y);

	public double speed();
	public PointParticle speed(double speed);

	/**
	 * Angle in radians. Zero points to the right and positive is clockwise.
	 */
	public double direction();
	public PointParticle direction(double direction);

	/**
	 * Same semantics as above, but angle is in degrees.
	 */
	public double directionDegrees();
	public PointParticle directionDegrees(double direction);

	/**
	 * Sets a new x and y considering the particle's current x, y, speed and direction.
	 */
	public void move(); //TODO reconsider what this method should do. Maybe only the simulator should set the particle's position.

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object o);

	@Override
	public String toString();

}
