package br.ufrj.jfirn.mobileObstacle;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.BasicRobot;

public class CrazyRobot extends BasicRobot {

	public CrazyRobot() {
		super();
	}

	public CrazyRobot(double x, double y, double direction, double speed) {
		super(x, y, direction, speed);
	}

	@Override
	public void move() {
		//PI / 12 = 15 degrees
		this.direction( FastMath.random() * 2d * FastMath.PI );
		super.move();
	}

}
