package br.ufrj.jfirn.intelligent.evaluation;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;

public class RobotsUtils {

	public final static double SIZE_RADIUS = 5;

	/**
	 * Used to verify if the robot is in danger.
	 */
	public static boolean isInDangerRadius(Point myPosition, Point otherPosition) {
		return myPosition.distanceTo(otherPosition) <= RobotsUtils.SIZE_RADIUS * 2.5d;
	}

	/**
	 * Get 2 points that are {@link RobotBoundaries#RADIUS} units away from myPosition
	 * and located at -PI/2 an PI/2 from myDirection.
	 * 
	 * This is useful to know the limits of a {@link Robot}.
	 */
	public static Point[] pointsInVerticalAxisBoundaries(Point myPosition, double myDirection) {
		final double d1 = myDirection - FastMath.PI / 2d;
		final double d2 = myDirection + FastMath.PI / 2d;
	
		final Point[] points = new Point[] {
			new Point(
				myPosition.x() + FastMath.cos(d1) * SIZE_RADIUS,
				myPosition.y() + FastMath.sin(d1) * SIZE_RADIUS
			),
			new Point(
				myPosition.x() + FastMath.cos(d2) * SIZE_RADIUS,
				myPosition.y() + FastMath.sin(d2) * SIZE_RADIUS
			)
		};
		return points;
	}

}
