package br.ufrj.jfirn.intelligent;

import org.apache.commons.math3.util.FastMath;

public class Helper {

	/**
	 * Subtracts 2 different directions
	 */
	public static double directionSubtraction(double d1, double d2) {
		final double d = d1 - d2;
		return FastMath.atan2(FastMath.sin(d), FastMath.cos(d));
	}

}
