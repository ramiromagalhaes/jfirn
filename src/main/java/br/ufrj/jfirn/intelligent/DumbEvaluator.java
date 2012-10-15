package br.ufrj.jfirn.intelligent;

import java.util.Collection;
import java.util.Deque;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.PointParticle;
import br.ufrj.jfirn.intelligent.SimpleCollisionEvaluator.Collision;

public class DumbEvaluator implements Evaluator {
	private static final SimpleCollisionEvaluator collisionEvaluator = new SimpleCollisionEvaluator();

	public void evaluate(final PointParticle myself, final Collection<MovementStatistics> aboutObstacles, final Deque<Point> targets) {
		//do I foresee a collision?
		for (MovementStatistics stat : aboutObstacles) {
			final Collision collision = collisionEvaluator.eval(
					myself,
					stat.lastPosition(),
					stat.speedStats().getMean(),
					stat.directionStats().getMean(),
					stat.getObservedObjectId());

			if (collision == null) {
				continue;
			}

			if (myself.position().distanceTo(collision.position) <= 200 && collision.time <= 10d) {
				System.out.println(collision); //TODO for current debugging purposes only. Will vanish soon.
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
