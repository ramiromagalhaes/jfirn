package br.ufrj.jfirn.intelligent;

import java.util.Collection;
import java.util.Deque;

import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;

public class DumbEvaluator implements Evaluator {
	private static final Logger logger = LoggerFactory.getLogger(DumbEvaluator.class);

	private static final SimpleCollisionEvaluator collisionEvaluator = new SimpleCollisionEvaluator();

	public void evaluate(final Robot myself, final Collection<MovementStatistics> aboutObstacles, final Deque<Point> targets) {
		for (MovementStatistics stat : aboutObstacles) { //evaluate everyone I see.
			//Check if I'll collide with someone.
			final Collision collision = collisionEvaluator.eval(
					myself,
					stat.lastKnownPosition(),
					stat.speedMean(),
					stat.directionMean(),
					stat.getObservedObjectId());

			if (collision == null) { //No collision. Verify someone else.
				continue;
			}

			//If this collision is too far in the future, forget about it. Verify someone else.
			if (myself.position().distanceTo(collision.position) > 200d || collision.time > 10d) {
				continue;
			}

			if (logger.isDebugEnabled()) {
				logger.debug(collision.toString());
			}
		}


		final Point currentTarget = targets.peek();
		if (currentTarget != null) {
			myself.direction (
				FastMath.atan2(
					currentTarget.y() - myself.y(),
					currentTarget.x() - myself.x()
				)
			);
		}
	}

}
