package br.ufrj.jfirn.intelligent;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;

public class Trajectory {

	//Considering this as a linear equation we have y = alpha * x + beta
	private final double alpha;
	private final double beta;

	public Trajectory(double direction, Point origin) {
		this.alpha = FastMath.tan(direction);
		this.beta = origin.y() - alpha * origin.x();
	}

	public Point intersect(Trajectory other) {
		if (this.alpha == other.alpha) {
			return null; //no intersection or infinite intersecting points. In both cases, we return null.
		}

		//the intersection of those 2 linear equations is...
		final double x = (other.beta - this.beta) / (this.alpha - other.alpha);
		final double y = this.alpha * x + this.beta;

		return new Point(x, y);
	}

	public static Trajectory[] fromStatistics(MobileObstacleStatisticsLogger stats) {
		final Point lkp = stats.lastKnownPosition();
		final double mean = stats.directionMean();
		final double stdDeviation = FastMath.sqrt(stats.directionVariance());

		final Trajectory[] trajectory = new Trajectory[] {
			new Trajectory(mean - stdDeviation, lkp),
			new Trajectory(mean + stdDeviation, lkp)
		};

		return trajectory;
	}

}
