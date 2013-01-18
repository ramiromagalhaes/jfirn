package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;

public class BasicRobot implements Robot {

	private static final long serialVersionUID = 1L;

	private double x, y, speed, direction; //direction is in radians and is in range ]-PI, PI]
	private final int id;
	private static int robotIdGenerator = 1;

	{//this will be called prior to all constructors
		synchronized (BasicRobot.class) { //TODO verify if this will really synchronize something
			this.id = robotIdGenerator++;
		}
	}

	public BasicRobot() {
		x = y = 0;
		direction = Robot.RIGHT;
		speed = 1;
	}

	public BasicRobot(double x, double y, double direction, double speed) {
		this.x = x;
		this.y = y;
		direction(direction);
		this.speed = speed;
	}

	@Override
	public final double x() {
		return x;
	}

	@Override
	public final double y() {
		return y;
	}

	@Override
	public final Point position() {
		return new Point(x, y);
	}

	@Override
	public final BasicRobot x(final double x) {
		this.x = x;
		return this;
	}

	@Override
	public final BasicRobot y(final double y) {
		this.y = y;
		return this;
	}

	@Override
	public final double speed() {
		return this.speed;
	}

	@Override
	public BasicRobot speed(final double speed) {
		this.speed = speed;
		return this;
	}

	@Override
	public final double direction() {
		return this.direction;
	}

	@Override
	public BasicRobot direction(final double direction) {
		if (direction > FastMath.PI) {
			this.direction = direction % (2d * FastMath.PI);
			this.direction -= this.direction > FastMath.PI ? 2d * FastMath.PI : 0;
		} else if (direction <= -FastMath.PI) {
			this.direction = direction % (2d * FastMath.PI);
			this.direction += this.direction <= -FastMath.PI ? 2d * FastMath.PI : 0;
		} else {
			this.direction = direction;
		}

		return this;
	}

	@Override
	public final double xSpeed() {
		return FastMath.cos(this.direction) * this.speed;
	}

	@Override
	public final double ySpeed() {
		return FastMath.sin(this.direction) * this.speed;
	}

	@Override
	public void move() {
		x += xSpeed();
		y += ySpeed();
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if ( !this.getClass().isAssignableFrom(o.getClass()) ) {
			return false;
		}

		BasicRobot p = (BasicRobot)o;
		return p.id == this.id;
	}

	@Override
	public String toString() {
		//Sample: Robot 12 [x: 100, y: 1000, speed: 10, direction: 5]
		return new StringBuilder()
			.append( "Robot " )
			.append( hashCode() )
			.append( " [x: " )
			.append( x )
			.append( ", y: " )
			.append( y )
			.append( ", speed: " )
			.append( speed )
			.append( ", direction: " )
			.append( direction )
			.append( ']' )
			.toString();
	}

}
