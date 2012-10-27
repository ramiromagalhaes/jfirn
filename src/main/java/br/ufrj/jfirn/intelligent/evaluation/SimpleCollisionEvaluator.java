package br.ufrj.jfirn.intelligent.evaluation;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;

public class SimpleCollisionEvaluator {

	public Collision eval(Robot me, Robot other) {
		return eval(me, other.position(), other.speed(), other.direction(), other.hashCode());
	}

	public Collision eval(Robot me, Point otherPosition, double otherSpeed, double otherDirection, int id) {
		//TODO I fear this will perform poorly...

		//here we forecast if a collision may happen
		final Point collisionPosition = intersection(me, otherPosition, otherDirection);
		if (collisionPosition == null) { //if there is no intersection, then there is no collision
			return null;
		}

		//now, we calculate when it's going to happen...

		//but first, will it really happen?
		if ( //if any particle has passed the evaluated collision position, then there will be no collision.
			!isTheRightDirection(me.position(), me.direction(), collisionPosition) ||
			!isTheRightDirection(otherPosition, otherDirection, collisionPosition) ) {
			return null;
		}

		//when each particle will reach the collision position?
		final double meTime = timeToReach(me, collisionPosition);
		final double otherTime = timeToReach(otherPosition, otherSpeed, collisionPosition);

		//I'm considering there will be a collision if the time between particles to arrive at the collision position are almost the same.
		//TODO Improve this 'if', maybe considering objects direction, speed, size, etc.
		//TODO This approach may be an oversimplification but I think I'm kinda building a fuzzy method to evaluate the collision.
		if (FastMath.abs(meTime - otherTime) > 6d) {
			return null;
		}

		//estimate the collision time with the average of times
		final double time = (meTime + otherTime) / 2d;

		return new Collision(id, collisionPosition, time);
	}


	/**
	 * The particle paths intersect at some point?
	 */
	private Point intersection(Robot me, Point otherPosition, double otherDirection) {
		if (me.direction() == otherDirection) { //TODO verify: this does not seem to be the right way to check for intersections
			return null; //no intersection or infinite intersecting points. In both cases, we say there is no collision.
		}

		//intersection of 2 linear equations. The 'me' particle is y = alpha * x + beta
		final double alpha = FastMath.tan(me.direction());
		final double beta = me.y() - alpha * me.x();

		//The other particle is k = m * x + b
		final double m = FastMath.tan(otherDirection);
		final double b = otherPosition.y() - m * otherPosition.x();

		//the intersection is then...
		final double x = (b - beta) / (alpha - m);
		final double y = FastMath.tan(me.direction()) * x + beta;

		return new Point(x, y);
	}

	/**
	 * How much time p would take to reach destination?
	 */
	private double timeToReach(Robot p, Point destination) {
		return timeToReach(p.position(), p.speed(), destination);
	}

	/**
	 * How much time a particle at position with speed would take to reach destination?
	 * Ignores the direction because I assume I'm going straight towards the destination.
	 * @see #isTheRightDirection(Point, double, Point)
	 */
	private double timeToReach(Point position, double speed, Point destination) {
		return position.distanceTo(destination) / speed;
	}

	/**
	 * Is the particle going towards the collision or has it passed the destination already?
	 * true if it is going in the right direction.
	 */
	public static boolean isTheRightDirection(Point position, double direction, Point destination) {
		//TODO there might be a better/faster way to do this

		final double y = destination.y() - position.y();
		final double x = destination.x() - position.x();
		final double angle = FastMath.atan2(y, x);

		final double sinDirection = FastMath.sin(direction);
		final double sinAngle = FastMath.sin(angle);
		final double cosDirection = FastMath.cos(direction);
		final double cosAngle = FastMath.cos(angle);

		return FastMath.signum(sinDirection) == FastMath.signum(sinAngle)
			&& FastMath.signum(cosDirection) == FastMath.signum(cosAngle);
	}



	public static class Collision {
		//TODO still need to define the collision area and its probability
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
