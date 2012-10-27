package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;


public interface Robot {

	public static final double STOPPED = 0;
	public static final double RIGHT = 0,
	                           UP = FastMath.PI / 2d,
	    	                   LEFT = FastMath.PI,
	    	    	           DOWN = 3d * FastMath.PI / 2d;

	public double x();
	public double y();
	public Point position();
	public Robot x(double x);
	public Robot y(double y);

	public double speed();
	public Robot speed(double speed);

	/**
	 * Angle in radians. Zero points to the right and positive is clockwise.
	 */
	public double direction();
	public Robot direction(double direction);

	/**
	 * Same semantics as above, but angle is in degrees.
	 */
	public double directionDegrees();
	public Robot directionDegrees(double direction);

	/**
	 * Considering the direction this particle is facing, return this particle
	 * speed in respect to the X axis (horizontal).
	 */
	public double xSpeed();

	/**
	 * Considering the direction this particle is facing, return this particle
	 * speed in respect to the Y axis (vertical).
	 */
	public double ySpeed();

	/**
	 * Sets a new x and y considering the particle's current x, y, speed and direction.
	 */
	public void move(); //TODO reconsider what this method should do. Maybe only the simulator
	                    //should set the particle's position and the particle should only think
	                    //about its next actions. The method signature below could be used then.
	                    //public Status think(final double x, final double y); 

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object o);

	@Override
	public String toString();

}
