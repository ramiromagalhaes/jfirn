package br.ufrj.jfirn.mobileObstacle;

import java.util.Random;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.BasicRobot;

public class RandomWalkerRobot extends BasicRobot {

	private static final long serialVersionUID = 1L;
	private final Random randomNumberGenerator;

	public RandomWalkerRobot() {
		super();
		this.randomNumberGenerator = new Random();
	}

	public RandomWalkerRobot(double x, double y, double direction, double speed) {
		super(x, y, direction, speed);
		this.randomNumberGenerator = new Random();
	}

	public RandomWalkerRobot(Random random) {
		super();
		this.randomNumberGenerator = random;
	}

	public RandomWalkerRobot(double x, double y, double direction, double speed, Random random) {
		super(x, y, direction, speed);
		this.randomNumberGenerator = random;
	}

	@Override
	public void move() {
		//PI / 12 = 15 degrees
		this.direction( (randomNumberGenerator.nextDouble() - 0.5) * FastMath.PI / 6d + this.direction() );
		super.move();
	}

}
