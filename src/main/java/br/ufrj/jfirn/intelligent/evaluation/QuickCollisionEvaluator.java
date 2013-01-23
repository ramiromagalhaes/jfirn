package br.ufrj.jfirn.intelligent.evaluation;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Line;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.intelligent.Collision;
import br.ufrj.jfirn.intelligent.MobileObstacleStatistics;
import br.ufrj.jfirn.intelligent.Thoughts;

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
					new CollisionEvaluation(stats.getObstacleId(), Reason.NOT_ENOUGH_SAMPLES)
				);
				continue;
			}

			final Point moPosition = stats.lastKnownPosition();

			if (RobotsUtils.isInDangerRadius(myPosition, moPosition)) {
				thoughts.putCollisionEvaluation(
						new CollisionEvaluation(stats.getObstacleId(), Reason.TOO_CLOSE)
					);
					continue;
			}

			final Collision collision = evaluateCollision(
				myPosition, thoughts.myDirection(), thoughts.mySpeed(),
				moPosition, stats.directionMean(), stats.speedMean(),
				stats.getObstacleId()
			);

			if (collision == null) {
				thoughts.putCollisionEvaluation(
					new CollisionEvaluation(stats.getObstacleId(), Reason.NO_INTERSECTION)
				);
				continue;
			}

			//If this collision is too far in the future, forget it and go verify someone else.
			if (myPosition.distanceTo(collision.position) > 400d || collision.time > 20d) {
				thoughts.putCollisionEvaluation(
					new CollisionEvaluation(stats.getObstacleId(), Reason.TOO_FAR_AWAY)
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
		final Line myTrajectory = new Line(myPosition, myDirection, mySpeed * 100);
		final Line otherTrajectory = new Line(otherPosition, otherDirection, otherSpeed * 100);

		final Point intersection = myTrajectory.intersection(otherTrajectory);

		if (intersection == null) {
			return null;
		}

		//now, we calculate WHEN it's going to happen...

		//when each robot will reach the collision position?
		final double myTime = timeToReach(myPosition, mySpeed, intersection);
		final double otherTime = timeToReach(otherPosition, otherSpeed, intersection);

		//Heuristic: I'm considering there will be a collision if the time between robots to arrive at the collision position are almost the same.
		//TODO Evaluate and improve this 'if', maybe changing the time while considering objects direction, speed, size, etc.
		if (FastMath.abs(myTime - otherTime) > 6d) {
			return null;
		}

		//rough estimate of the collision time
		final double time = (myTime + otherTime) / 2d;

		return new Collision(id, intersection, time);
	}


	/**
	 * How much time a robot at position with speed would take to reach destination?
	 * Ignores the direction because I assume I'm going straight towards the destination.
	 * @see #isTheRightDirection(Point, double, Point)
	 */
	private double timeToReach(Point position, double speed, Point destination) {
		return position.distanceTo(destination) / speed;
	}

}
