package br.ufrj.jfirn.intelligent.evaluation;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;

public class RobotExtremePointsCalculator {
	private static final double RADIUS = 10;

	/**
	 * Get 2 points that are 10 units away from myPosition and located at
	 * -PI/2 an PI/2 from myDirection.
	 * 
	 * This is useful to know the limits of a {@link Robot}.
	 */
	//TODO gimme a decent name!
	public static Point[] makePoint(Point myPosition, double myDirection) {
		final double d1 = myDirection - FastMath.PI / 2d;
		final Point[] points = new Point[] {
			new Point(
					myPosition.x() - FastMath.cos(d1) * RADIUS,
					myPosition.y() - FastMath.sin(d1) * RADIUS
			),
			new Point(
					myPosition.x() + FastMath.cos(d1) * RADIUS,
					myPosition.y() + FastMath.sin(d1) * RADIUS
			)
		};
		return points;
	}


}
