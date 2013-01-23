package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.common.Point;

public class RobotsConstants {

	public final static double SIZE_RADIUS = 5;

	/**
	 * Used to verify if the robot is in danger.
	 */
	public static boolean isInDangerRadius(Point myPosition, Point otherPosition) {
		return myPosition.distanceTo(otherPosition) <= RobotsConstants.SIZE_RADIUS * 2.5d;
	}

}
