package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;


//TODO still need lots of improvements
public class SineParticle extends BasicParticle {

	private double step = 0;

	public SineParticle() {
		super();
	}

	public SineParticle(double x, double y, double direction, double speed) {
		super(x, y, direction, speed);
	}

	@Override
	public void move() {
		double i = 0.08;

		if ( FastMath.sin(this.step) < 0) {
			i = -i;
		}

		this.direction( this.direction() + i );

		this.step += 0.08;

		// TODO Auto-generated method stub
		super.move();
	}

}
