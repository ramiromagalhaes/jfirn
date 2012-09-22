package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;

public class CrazyParticle extends BasicParticle {

	public CrazyParticle() {
		super();
	}

	public CrazyParticle(double x, double y, double direction, double speed) {
		super(x, y, direction, speed);
	}

	@Override
	public void move() {
		//PI / 12 = 15 degrees
		this.direction( FastMath.random() * 2d * FastMath.PI );
		super.move();
	}

}
