package br.ufrj.jfirn.intelligent.evaluation;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;



/**
 * Helper class to calculate the points located at specific angles around from a robot's position.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class RobotBoundaries {

	/**
	 * Get 2 points that are {@link RobotBoundaries#RADIUS} units away from myPosition
	 * and located at -PI/2 an PI/2 from myDirection.
	 * 
	 * This is useful to know the limits of a {@link Robot}.
	 */
	public static Point[] pointsFromVerticalAxis(Point myPosition, double myDirection) {
		final double d1 = myDirection - FastMath.PI / 2d;
		final double d2 = myDirection + FastMath.PI / 2d;

		final Point[] points = new Point[] {
			new Point(
				myPosition.x() + FastMath.cos(d1) * RobotsConstants.SIZE_RADIUS,
				myPosition.y() + FastMath.sin(d1) * RobotsConstants.SIZE_RADIUS
			),
			new Point(
				myPosition.x() + FastMath.cos(d2) * RobotsConstants.SIZE_RADIUS,
				myPosition.y() + FastMath.sin(d2) * RobotsConstants.SIZE_RADIUS
			)
		};
		return points;
	}

}
