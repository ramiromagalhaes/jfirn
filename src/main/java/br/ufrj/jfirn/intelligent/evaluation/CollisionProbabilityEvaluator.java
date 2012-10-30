package br.ufrj.jfirn.intelligent.evaluation;

import java.util.Map;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.intelligent.Collision;
import br.ufrj.jfirn.intelligent.MobileObstacleStatisticsLogger;
import br.ufrj.jfirn.intelligent.Thoughts;
import br.ufrj.jfirn.intelligent.Trajectory;

public class CollisionProbabilityEvaluator implements Evaluator {
	private static final double RADIUS = 10;

	@Override
	public void evaluate(Thoughts thoughts, Instruction instruction, ChainOfEvaluations chain) {
		final Map<Integer, MobileObstacleStatisticsLogger> knownObstacles =
			thoughts.knownObstacles();

		final Trajectory[] myTrajectory;
		{
			final Point points[] = makePoint(thoughts.myPosition(), thoughts.myDirection());
			myTrajectory = new Trajectory[] {
				new Trajectory(thoughts.myDirection(), points[0]),
				new Trajectory(thoughts.myDirection(), points[1]),
			};
		}

		for (final Collision collision : thoughts.collisions()) {
			final MobileObstacleStatisticsLogger stats =
				knownObstacles.get(collision.withObjectId);
			final Trajectory[] trajectories =
				Trajectory.fromStatistics(stats);

			final Point[] intersections = new Point[] {
				myTrajectory[0].intersect(trajectories[0]),
				myTrajectory[1].intersect(trajectories[0]),
				myTrajectory[0].intersect(trajectories[1]),
				myTrajectory[1].intersect(trajectories[1])
			};

			//TODO convert points to polar coordinates
			intersections[0] = intersections[0];
			intersections[1] = intersections[0];
			intersections[2] = intersections[0];
			intersections[3] = intersections[0];

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

	private Point[] makePoint(Point myPosition, double myDirection) {
		final double d1 = myDirection - FastMath.PI / 2d;
		final Point[] points = new Point[] {
			new Point(
					myPosition.x() - FastMath.cos(d1) * RADIUS,
					myPosition.y() - FastMath.sin(d1) * RADIUS
			),
			new Point(
					myPosition.x() + FastMath.cos(d1) * RADIUS,
					myPosition.y() + FastMath.sin(d1) * RADIUS
			)
		};
		return points;
	}

}
