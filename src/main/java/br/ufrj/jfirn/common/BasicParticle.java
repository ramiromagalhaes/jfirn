package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;

public class BasicParticle implements PointParticle {

	private double x, y, speed, direction; //direction is in radians
	private final int id;
	private static int particleIdGenerator = 1;

	{//this will be called prior to all constructors
		synchronized (BasicParticle.class) { //TODO verify if this will really synchronize something
			this.id = particleIdGenerator++;
		}
	}

	public BasicParticle() {
		x = y = direction = 0;
		speed = 1;
	}

	public BasicParticle(double x, double y, double direction, double speed) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.speed = speed;
	}

	@Override
	public double x() {
		return x;
	}

	@Override
	public double y() {
		return y;
	}

	@Override
	public Point position() {
		return new Point(x, y);
	}

	@Override
	public BasicParticle x(double x) {
		this.x = x;
		return this;
	}

	@Override
	public BasicParticle y(double y) {
		this.y = y;
		return this;
	}

	@Override
	public double speed() {
		return this.speed;
	}

	@Override
	public BasicParticle speed(double speed) {
		this.speed = speed;
		return this;
	}

	@Override
	public double direction() {
		return this.direction;
	}

	@Override
	public BasicParticle direction(double direction) {
		this.direction = direction;
		return this;
	}

	@Override
	public double directionDegrees() {
		return FastMath.toDegrees(direction);
	}

	@Override
	public PointParticle directionDegrees(double direction) {
		this.direction = FastMath.toRadians(direction);
		return this;
	}

	@Override
	public void move() {
		x += FastMath.cos(this.direction) * this.speed;
		y += FastMath.sin(this.direction) * this.speed;
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

		BasicParticle p = (BasicParticle)o;
		return p.id == this.id;
	}

	@Override
	public String toString() {
		//Sample: Particle 12 [x: 100, y: 1000, speed: 10, direction: 5]
		return new StringBuilder()
			.append( "Particle " )
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
