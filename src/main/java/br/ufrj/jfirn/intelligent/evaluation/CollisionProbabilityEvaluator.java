package br.ufrj.jfirn.intelligent.evaluation;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.intelligent.Collision;
import br.ufrj.jfirn.intelligent.MobileObstacleStatisticsLogger;
import br.ufrj.jfirn.intelligent.Thoughts;
import br.ufrj.jfirn.intelligent.Trajectory;

public class CollisionProbabilityEvaluator implements Evaluator {

	@Override
	public void evaluate(Thoughts thoughts, Instruction instruction, ChainOfEvaluations chain) {
		final Map<Integer, MobileObstacleStatisticsLogger> knownObstacles =
			thoughts.knownObstacles();

		//The trajectory of the extremities of the IntelligentRobot, considering its movement direction
		final Trajectory[] myTrajectory;
		{
			final Point points[] = RobotExtremePointsCalculator
				.makePoint(thoughts.myPosition(), thoughts.myDirection());
			myTrajectory = new Trajectory[] {
				new Trajectory(thoughts.myDirection(), points[0]),
				new Trajectory(thoughts.myDirection(), points[1]),
			};
		}

		for (Iterator<Collision> it = thoughts.collisions().iterator(); it.hasNext(); ) {
			final Collision collision = it.next();

			//We'll clean our collision database of collisions with objects that we do not monitor anymore
			//TODO consider moving this code to IntelligentRobot#onSight
			if (!knownObstacles.containsKey(collision.withObjectId)) {
				it.remove();
				continue;
			}

			final MobileObstacleStatisticsLogger stats =
				knownObstacles.get(collision.withObjectId);

			if (stats.entriesAdded() == 0) { //Not enough data. Ignore for now.
				continue;
			}

			final Trajectory[] trajectories =
				Trajectory.fromStatistics(stats);

			final Point[] intersections = new Point[] {
				myTrajectory[0].intersect(trajectories[0]),
				myTrajectory[1].intersect(trajectories[0]),
				myTrajectory[0].intersect(trajectories[1]),
				myTrajectory[1].intersect(trajectories[1])
			};

			final Point moPosition = stats.lastKnownPosition();

			//Convert from XY coordinates to a polar coordinates around the mobile obstacle.
			//Normalize the input, since BGD works only with 0 mean and 1 variance: (valor - mean) / sqrt(var)
			for(int i = 0; i < intersections.length; i++) {
				final double direction;
				final double speed;

				if (stats.directionVariance() != 0d) {
					direction = (moPosition.directionTo(intersections[i]) - stats.directionMean()) / FastMath.sqrt(stats.directionVariance());
				} else {
					direction = moPosition.directionTo(intersections[i]) - stats.directionMean();
				}

				if (stats.speedVariance() != 0d) {
					speed = ((moPosition.distanceTo(intersections[i]) / collision.time) - stats.speedMean()) / FastMath.sqrt(stats.speedVariance());
				} else {
					speed = (moPosition.distanceTo(intersections[i]) / collision.time) - stats.speedMean();
				}

				intersections[i] = new Point(direction, speed);
			}

			collision.probability =
				BGD.cdfOfHorizontalQuadrilaterals(
					intersections[0],
					intersections[1],
					intersections[2],
					intersections[3],
					stats.speedDirectionCorrelation());
		}

		chain.nextEvaluator(thoughts, instruction, chain);
	}

}
