package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;



public class SineParticle extends BasicParticle {

	private double step = 0;
	private double heading;
	private double frequency;

	public SineParticle() {
		super();
		this.heading = 0;
		this.frequency = 1;
	}

	public SineParticle(double x, double y, double direction, double speed) {
		super(x, y, direction, speed);
		this.heading = direction;
		this.frequency = 1;
	}

	public SineParticle(double x, double y, double direction, double speed, double frequency) {
			super(x, y, direction, speed);
			this.heading = direction;
			this.frequency = frequency;
 	}

	@Override
	public void move() {
		this.direction( heading + FastMath.acos(FastMath.sin(frequency * step)) );
		this.step += 0.1;
		super.move();
	}

}
