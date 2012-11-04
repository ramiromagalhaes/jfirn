package br.ufrj.jfirn.mobileObstacle;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.BasicRobot;

public class SquareRobot extends BasicRobot {

	private static final long serialVersionUID = 1L;

	private int step = 0;
	private int sideCount = 0;
	private int size;
	private int displacement;

	public SquareRobot() {
		super();
	}

	public SquareRobot(double x, double y, double direction, double speed) {
		super(x, y, direction, speed);
		this.size = 8;
		this.displacement = size / 2;
	}

	public SquareRobot(double x, double y, double direction, double speed,
			int size) {
		super(x, y, direction, speed);
		this.size = 2 * (size / 2); // Garante que size sempre será par
		this.displacement = size / 2;
	}

	@Override
	public void move() {
		if (sideCount == 3) {
			if (step++ % (size + displacement) == 0) {
				this.direction(this.direction() + FastMath.PI / 2d);
				sideCount = 0;
				step = 0;
			}
		} else {
			if (step++ % size == 0) {
				this.direction(this.direction() + FastMath.PI / 2d);
				sideCount++;
			}
		}

		super.move();
	}
}
