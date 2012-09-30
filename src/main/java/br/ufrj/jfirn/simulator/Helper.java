package br.ufrj.jfirn.simulator;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.PointParticle;

public class Helper {

	/**
	 * Subtracts 2 different directions
	 */
	public static double directionSubtraction(double d1, double d2) {
		final double d = d1 - d2;
		return FastMath.atan2( FastMath.sin(d), FastMath.cos(d) );
	}

	public static Point intersection(PointParticle p1, PointParticle p2) {
		return intersection(p1, p2.position(), p2.direction());
	}

	public static Point intersection(PointParticle p1, Point p2, double p2Direction) {
		//TODO test against infinite intersecting points?
		final double x = (p2.y() - p1.y()) / ( FastMath.tan(p1.direction()) - FastMath.tan(p2Direction) );

		return new Point(x, FastMath.tan(p1.direction()) * x + p1.y() );
	}

}
