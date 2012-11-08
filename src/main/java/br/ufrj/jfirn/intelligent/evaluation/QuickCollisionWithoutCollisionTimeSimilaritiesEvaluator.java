package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.intelligent.Collision;

/**
 * Copy of {@link QuickCollisionEvaluator} without the time heuristics evaluation.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class QuickCollisionWithoutCollisionTimeSimilaritiesEvaluator extends QuickCollisionEvaluator {

	protected Collision evaluateCollision(Point myPosition, double myDirection, double mySpeed, Point otherPosition, double otherDirection, double otherSpeed, int id) {
		//TODO test performance?

		//here we forecast if a collision may happen
		final Point collisionPosition =
			intersection(myPosition, myDirection, mySpeed,
					otherPosition, otherDirection, otherSpeed);

		//if there is no intersection, then there is no collision
		if (collisionPosition == null) {
			return null;
		}

		//now, we calculate when it's going to happen...

		//but first, will it really happen?
		if ( //if any robot has passed the evaluated collision position, then there will be no collision.
			!isTheRightDirection(myPosition, myDirection, collisionPosition) ||
			!isTheRightDirection(otherPosition, otherDirection, collisionPosition) ) {
			return null;
		}

		//when each robot will reach the collision position?
		final double myTime = timeToReach(myPosition, mySpeed, collisionPosition);
		final double otherTime = timeToReach(otherPosition, otherSpeed, collisionPosition);

		//estimate the collision time with the average of times
		final double time = (myTime + otherTime) / 2d;

		return new Collision(id, collisionPosition, time);
	}



}
