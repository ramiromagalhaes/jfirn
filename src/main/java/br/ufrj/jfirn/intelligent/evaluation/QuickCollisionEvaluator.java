package br.ufrj.jfirn.intelligent.evaluation;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.geometry.Line;
import br.ufrj.jfirn.common.geometry.Point;
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
		final Point irPosition = thoughts.myPosition();

		for (MobileObstacleStatistics stats : thoughts.allObstacleStatistics()) { //evaluate everyone I see.
			if (stats.samplesCount() <= 3) {
				thoughts.putCollisionEvaluation(
					new CollisionEvaluation(stats.getObstacleId(), Reason.NOT_ENOUGH_SAMPLES)
				);
				continue;
			}

			final Point moPosition = stats.lastKnownPosition();

			if (RobotsUtils.isInDangerRadius(irPosition, moPosition)) {
				thoughts.putCollisionEvaluation(
					new CollisionEvaluation(stats.getObstacleId(), Reason.TOO_CLOSE)
				);
				continue;
			}

			//do the mean trajectories intersect?
			final Line myTrajectory = new Line(irPosition, thoughts.myDirection());
			final Line otherTrajectory = new Line(moPosition, stats.directionMean());
			final Point intersection = myTrajectory.intersection(otherTrajectory);
			if (intersection == null) {
				thoughts.putCollisionEvaluation(
					new CollisionEvaluation(stats.getObstacleId(), Reason.NO_INTERSECTION)
				);
				continue;
			}

			//when each robot will reach the collision position?
			final double myTime = timeToReach(irPosition, thoughts.mySpeed(), intersection);
			final double otherTime = timeToReach(moPosition, stats.speedMean(), intersection);
			//Heuristic: I'm considering there will be a collision if the time between robots to arrive at the collision position are almost the same.
			if (FastMath.abs(myTime - otherTime) > 6d) {
				thoughts.putCollisionEvaluation(
					new CollisionEvaluation(stats.getObstacleId(), Reason.DIFFERENT_TIME)
				);
				continue;
			}


			//rough estimate of the collision time
			final double time = (myTime + otherTime) / 2d;
			final Collision collision = new Collision(stats.getObstacleId(), intersection, time);

			//If this collision is too far in the future, forget it and go verify someone else.
			if (irPosition.distanceTo(collision.position) > 400d || collision.time > 20d) {
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

	/**
	 * How much time a robot at position with speed would take to reach destination?
	 * Ignores the direction because I assume I'm going straight towards the destination.
	 * @see #isTheRightDirection(Point, double, Point)
	 */
	private double timeToReach(Point position, double speed, Point destination) {
		return position.distanceTo(destination) / speed;
	}

}
