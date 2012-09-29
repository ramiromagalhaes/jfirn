package br.ufrj.jfirn.intelligent;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.util.FastMath;

/**
 * Collects data about an observed object.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class MovementStatistics {

	private double xLast, yLast;

	private SummaryStatistics
		x = new SummaryStatistics(),
		y = new SummaryStatistics(),
		speed = new SummaryStatistics(),
		direction = new SummaryStatistics();

	// TODO implement confidence intervals?

	public void addEntry(double x, double y, double speed, double direction) {
		this.xLast = x;
		this.yLast = y;
		this.x.addValue(x);
		this.y.addValue(y);
		this.speed.addValue(speed);

		//Weird? See http://en.wikipedia.org/wiki/Directional_statistics#The_fundamental_difference_between_linear_and_circular_statistics
		this.direction.addValue(
			FastMath.atan2(
				FastMath.sin(direction),
				FastMath.cos(direction)
			)
		);
	}

	public double x() {
		return xLast;
	}

	public double xMean() {
		return x.getMean();
	}

	public double xVariance() {
		return x.getVariance();
	}

	public double y() {
		return yLast;
	}

	public double yMean() {
		return y.getMean();
	}

	public double yVariance() {
		return y.getVariance();
	}

	public double speedMean() {
		return speed.getMean();
	}

	public double speedVariance() {
		return speed.getVariance();
	}

	public double directionMean() {
		return direction.getMean();
	}

	public double directionVariance() {
		return direction.getVariance();
	}

}
