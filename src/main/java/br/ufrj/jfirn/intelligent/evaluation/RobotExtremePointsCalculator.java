package br.ufrj.jfirn.intelligent.evaluation;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;

public class RobotExtremePointsCalculator {
	private static final double RADIUS = 10;

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
