package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;

public class RandomWalkerParticle extends BasicParticle {

	public RandomWalkerParticle() {
		super();
	}

	public RandomWalkerParticle(double x, double y, double direction, double speed) {
		super(x, y, direction, speed);
	}

	@Override
	public void move() {
		//PI / 12 = 15 degrees
		this.direction( (FastMath.random() - 0.5) * FastMath.PI / 6d + this.direction() );
		super.move();
	}

}
