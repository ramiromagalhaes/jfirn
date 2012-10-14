package br.ufrj.jfirn.intelligent;

import java.util.Collection;
import java.util.Deque;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.PointParticle;

public class DumbEvaluator implements Evaluator {

	public void evaluate(final PointParticle myself, final Collection<MovementStatistics> aboutObstacles, final Deque<Point> targets) {
		//do I foresee a collision?
		for (MovementStatistics stat : aboutObstacles) {
			final Point collisionPosition =
				Helper.intersection(myself, stat.lastPosition(), stat.directionStats().getMean());

			if (collisionPosition == null) {
				continue;
			}

			final double myTime = Helper.timeToReach(myself, collisionPosition);
			final double hisTime = Helper.timeToReach(stat.lastPosition(), stat.speedStats().getMean(), collisionPosition);
			final double deltaTime = FastMath.abs(myTime - hisTime);
			final double time = (myTime + hisTime) / 2d;

			//TODO we can define a collision as something likely to happen if the time to collision of both particles are the same?
			if (deltaTime <= 4d //if the time to reach the collision position is almost the same...
					//&& time >= 0d //...and the time of the event is in the future (i.e. non negative)...
					//&& time <= 10 //...and will happen soon...
					) { //...then I'll care about it.
				final Collision collision = new Collision(stat.getObservedObjectId(), collisionPosition, time);
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

	public static class Collision {
		public final int withObjectId;
		public final Point position;
		public final double time;

		public Collision(int withObjectId, Point position, double time) {
			this.withObjectId = withObjectId;
			this.position = position;
			this.time = time;
		}

		public String toString() {
			return new StringBuilder()
				.append("Collision with ")
				.append(withObjectId)
				.append(" at ")
				.append(position)
				.append(" in ")
				.append(time)
				.append(" time units.")
				.toString();
		}
	}

}
