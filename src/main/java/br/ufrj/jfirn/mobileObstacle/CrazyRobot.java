package br.ufrj.jfirn.mobileObstacle;

import java.util.Random;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.BasicRobot;

public class CrazyRobot extends BasicRobot {

	private static final long serialVersionUID = 1L;
	private final Random randomNumberGenerator;

	public CrazyRobot() {
		super();
		this.randomNumberGenerator = new Random();
	}

	public CrazyRobot(double x, double y, double direction, double speed) {
		super(x, y, direction, speed);
		this.randomNumberGenerator = new Random();
	}

	public CrazyRobot(Random random) {
		super();
		this.randomNumberGenerator = random;
	}

	public CrazyRobot(double x, double y, double direction, double speed, Random random) {
		super(x, y, direction, speed);
		this.randomNumberGenerator = random;
	}

	@Override
	public void move() {
		this.direction( randomNumberGenerator.nextDouble() * 2d * FastMath.PI );
		super.move();
	}

}
