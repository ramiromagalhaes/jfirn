package br.ufrj.jfirn.intelligent;

import br.ufrj.jfirn.common.BasicRobot;

public abstract class AbstractIntelligentRobot extends BasicRobot {

	public AbstractIntelligentRobot() {
		super();
	}

	public AbstractIntelligentRobot(double x, double y, double direction) {
		this.x(x);
		this.y(y);
		this.direction(direction);
	}

	@Override
	public BasicRobot speed(double speed) {
		if (speed > maximumSpeed()) {
			speed = maximumSpeed();
		}
		return super.speed(speed);
	}

	protected abstract double maximumSpeed();

}