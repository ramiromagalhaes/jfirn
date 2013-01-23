package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.common.Point;

public class RobotsUtils {

	public final static double SIZE_RADIUS = 5;

	/**
	 * Used to verify if the robot is in danger.
	 */
	public static boolean isInDangerRadius(Point myPosition, Point otherPosition) {
		return myPosition.distanceTo(otherPosition) <= RobotsUtils.SIZE_RADIUS * 2.5d;
	}

}
