package br.ufrj.jfirn.common;

import java.io.Serializable;

import org.apache.commons.math3.util.FastMath;


public interface Robot extends Serializable {

	public static final double STOPPED = 0;
	public static final double MAX_SPEED = Double.POSITIVE_INFINITY;

	public static final double RIGHT = 0,
	                           UP = FastMath.PI / 2d,
	    	                   LEFT = FastMath.PI,
	    	    	           DOWN = 3d * FastMath.PI / 2d;

	/**
	 * The real x position of this Robot in the world. Notice
	 * that the real position may be different from the one
	 * that this Robot thinks it is.
	 */
	public double x();
	/**
	 * The real y position of this Robot in the world. Notice
	 * that the real position may be different from the one
	 * that this Robot thinks it is.
	 */
	public double y();
	/**
	 * The real position of this Robot in the world. Notice
	 * that the real position may be different from the one
	 * that this Robot thinks it is.
	 */
	public Point position();
	/**
	 * Sets the robot real x position.
	 */
	public Robot x(double x);
	/**
	 * Sets the robot real y position.
	 */
	public Robot y(double y);

	/**
	 * The real robot speed. It may be different from the one the robot
	 * thinks it is.
	 * @see Robot#STOPPED
	 */
	public double speed();
	/**
	 * Sets the real robot speed.
	 */
	public Robot speed(double speed);

	/**
	 * Return the real direction the robot is facing. It may be different
	 * from the one the robot thinks it is going.
	 * Angle in radians. Zero points to the right and positive is counterclockwise.
	 * @see Robot#RIGHT
	 * @see Robot#UP
	 * @see Robot#LEFT
	 * @see Robot#DOWN
	 */
	public double direction();
	public Robot direction(double direction);

	/**
	 * Same semantics as above, but angle is in degrees.
	 */
	public double directionDegrees();
	public Robot directionDegrees(double direction);

	/**
	 * Considering the direction this robot is facing, return this robot
	 * speed in respect to the X axis (horizontal).
	 */
	public double xSpeed();

	/**
	 * Considering the direction this robot is facing, return this robot
	 * speed in respect to the Y axis (vertical).
	 */
	public double ySpeed();

	/**
	 * Sets a new x and y considering the robot's current x, y, speed and direction.
	 */
	public void move(); //TODO reconsider what this method should do. Maybe only the simulator
	                    //should set the robot's position and the robot should only think
	                    //about its next actions. The method signature below could be used then.
	                    //public Status think(final double x, final double y); 

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object o);

	@Override
	public String toString();

}
