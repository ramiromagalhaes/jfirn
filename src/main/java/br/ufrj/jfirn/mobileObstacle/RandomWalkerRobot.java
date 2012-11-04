package br.ufrj.jfirn.mobileObstacle;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.BasicRobot;

public class RandomWalkerRobot extends BasicRobot {

	private static final long serialVersionUID = 1L;

	public RandomWalkerRobot() {
		super();
	}

	public RandomWalkerRobot(double x, double y, double direction, double speed) {
		super(x, y, direction, speed);
	}

	@Override
	public void move() {
		//PI / 12 = 15 degrees
		this.direction( (FastMath.random() - 0.5) * FastMath.PI / 6d + this.direction() );
		super.move();
	}

}
