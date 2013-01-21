package br.ufrj.jfirn.intelligent.evaluation;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.intelligent.Collision;
import br.ufrj.jfirn.intelligent.MobileObstacleStatistics;
import br.ufrj.jfirn.intelligent.Thoughts;
import br.ufrj.jfirn.intelligent.Trajectory;

/**
 * Evaluates if the IntelligentRobot and the other robots known to this robot may
 * collide.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 */
public class QuickCollisionEvaluator implements Evaluator {

	@Override
	public void evaluate(Thoughts thoughts, Instruction instruction, ChainOfEvaluations chain) {
		//think and evaluate and change your thoughts and decide what to do next...
		final Point myPosition = thoughts.myPosition();

		for (MobileObstacleStatistics stats : thoughts.allObstacleStatistics()) { //evaluate everyone I see.
			if (stats.samplesCount() <= 3) {
				thoughts.putCollisionEvaluation(
					new CollisionEvaluation(stats.getObservedObjectId(), Reason.NOT_ENOUGH_SAMPLES)
				);
				continue;
			}

			Collision collision = evaluateCollision(
				myPosition,
				thoughts.myDirection(),
				thoughts.mySpeed(),
				stats.lastKnownPosition(),
				stats.directionMean(),
				stats.speedMean(),
				stats.getObservedObjectId()
			);

			if (collision == null) {
				thoughts.putCollisionEvaluation(
					new CollisionEvaluation(stats.getObservedObjectId(), Reason.NO_INTERSECTION)
				);
				continue;
			}

			//If this collision is too far in the future, forget it and go verify someone else.
			if (myPosition.distanceTo(collision.position) > 400d || collision.time > 20d) {
				thoughts.putCollisionEvaluation(
					new CollisionEvaluation(stats.getObservedObjectId(), Reason.TOO_FAR_AWAY)
				);
				continue;
			}

			//We may need to evaluate a new collision
			thoughts.putCollisionEvaluation(
				new CollisionEvaluation(
					collision, Reason.PARTIAL_EVALUATION
				)
			);
		}

		chain.nextEvaluator(thoughts, instruction, chain); //keep thinking
	}


	protected Collision evaluateCollision(Point myPosition, double myDirection, double mySpeed, Point otherPosition, double otherDirection, double otherSpeed, int id) {
		//TODO test and improve performance?

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

		//Heuristic: I'm considering there will be a collision if the time between robots to arrive at the collision position are almost the same.
		//TODO Evaluate and improve this 'if', maybe changing the time while considering objects direction, speed, size, etc.
		if (FastMath.abs(myTime - otherTime) > 6d) {
			return null;
		}

		//rough estimate of the collision time
		final double time = (myTime + otherTime) / 2d;

		return new Collision(id, collisionPosition, time);
	}


	/**
	 * The robot paths intersect at some point?
	 */
	protected Point intersection(Point myPosition, double myDirection, double mySpeed, Point otherPosition, double otherDirection, double otherSpeed) {
		if (mySpeed == Robot.STOPPED && otherSpeed == Robot.STOPPED) {
			return null;
		} else if (mySpeed == Robot.STOPPED) {
			final Trajectory otherTrajectory = new Trajectory(otherDirection, otherPosition);
			return otherTrajectory.intersect(myPosition) ? myPosition : null;
		} else if (otherSpeed == Robot.STOPPED) {
			final Trajectory myTrajectory = new Trajectory(myDirection, myPosition);
			return myTrajectory.intersect(otherPosition) ? otherPosition : null;
		}

		final Trajectory myTrajectory = new Trajectory(myDirection, myPosition);
		final Trajectory otherTrajectory = new Trajectory(otherDirection, otherPosition);
		return myTrajectory.intersect(otherTrajectory);
	}



	/**
	 * How much time a robot at position with speed would take to reach destination?
	 * Ignores the direction because I assume I'm going straight towards the destination.
	 * @see #isTheRightDirection(Point, double, Point)
	 */
	protected double timeToReach(Point position, double speed, Point destination) {
		return position.distanceTo(destination) / speed;
	}

	/**
	 * Is the robot going towards the collision or has it passed the destination already?
	 * true if it is going in the right direction.
	 */
	protected static boolean isTheRightDirection(Point position, double direction, Point destination) {
		//TODO there might be a better/faster way to do this

		double angle = FastMath.atan2(
			destination.y() - position.y(),
			destination.x() - position.x()
		);

		final double sinDirection = FastMath.sin(direction);
		final double sinAngle = FastMath.sin(angle);
		final double cosDirection = FastMath.cos(direction);
		final double cosAngle = FastMath.cos(angle);

		return FastMath.signum(sinDirection) == FastMath.signum(sinAngle)
			&& FastMath.signum(cosDirection) == FastMath.signum(cosAngle);
	}

}
