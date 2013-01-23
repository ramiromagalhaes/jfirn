package br.ufrj.jfirn.intelligent.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Line;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Polygon;
import br.ufrj.jfirn.intelligent.MobileObstacleStatistics;
import br.ufrj.jfirn.intelligent.Thoughts;

public class CollisionProbabilityEvaluator implements Evaluator {

	@Override
	public void evaluate(Thoughts thoughts, Instruction instruction, ChainOfEvaluations chain) {
		//The trajectory of the extremities of the IntelligentRobot, considering its movement direction
		final Line[] irLines = getIntelligentRobotTrajectory(thoughts);

		for (CollisionEvaluation collisionEvaluation : thoughts.allColisionEvaluations()) {
			if (!collisionEvaluation.hasCollision()) {
				continue;
			}

			final MobileObstacleStatistics stats =
				thoughts.obstacleStatistics(collisionEvaluation.obstacleId());

			final Line[] moLines = Line.fromStatistics(stats);

			//Intersections in the X, Y plane.
			if (collisionEvaluation.obstacleId() == 4) {
				System.out.println("!");
			}
			final Point[] intersections = evaluateIntersections(irLines, moLines);

			//Copy the intersections to the collision. We'll modify the intersections
			collisionEvaluation.collision().area = new Polygon(intersections);



			final Point moPosition = stats.lastKnownPosition();

			//Convert from XY coordinates to polar coordinates around the mobile obstacle.
			//Normalize the input, since BGD works only with 0 mean and 1 variance: (value - mean) / sqrt(var)
			for(int i = 0; i < intersections.length; i++) {
				final Point intersection = intersections[i];

				double direction = moPosition.directionTo(intersection); //first, calculate the direction...
				if (stats.directionVariance() != 0d) {//...then normalize it.
					direction = (direction - stats.directionMean()) / FastMath.sqrt(stats.directionVariance());
				} else {
					direction = moPosition.directionTo(intersection) - stats.directionMean();
				}

				double speed = moPosition.distanceTo(intersection) / collisionEvaluation.collision().time; //first, calculate the speed...
				if (stats.speedVariance() != 0d) { //...then normalize it.
					speed = (speed - stats.speedMean()) / FastMath.sqrt(stats.speedVariance());
				} else {
					speed = speed - stats.speedMean();
				}

				intersections[i] = new Point(direction, speed);
			}

			//calculate the collision probability
			collisionEvaluation.collision().probability =
				BGD.cdf(
					new Polygon(intersections),
					stats.speedDirectionCorrelation());

			collisionEvaluation.reason(Reason.PROBABILITY_EVALUATION);
		}

		chain.nextEvaluator(thoughts, instruction, chain);
	}



	private Point[] evaluateIntersections(final Line[] irTrajectories, final Line[] moTrajectories) {
		//TODO Make this evaluation generic.
		//This method will not work when the obstacle overlaps the intelligent robot. Take care while using it.
		final List<Point> intersections = new ArrayList<>();
		final boolean[][] intersected = new boolean[irTrajectories.length][moTrajectories.length];
		final int[] intersectionCount = new int[irTrajectories.length];

		for (int i = 0; i < irTrajectories.length; i++) {
			final Line lir = irTrajectories[i];

			for (int j = 0; j < moTrajectories.length; j++) {
				final Line lmo = moTrajectories[j];

				final Point intersection = lir.intersection(lmo);
				if (intersection != null) {
					intersections.add(intersection);
					intersected[i][j] = true;
					intersectionCount[i] += intersected[i][j] ? 1 : 0;
				}
			}
		}

		if (intersections.size() == 1) {
			System.out.println("teste");
		}

		final int mainTrajectoriesCount = intersectionCount[0] + intersectionCount[1];

		/*
		 * Those 'ifs' are used to evaluate the many forms the collision area may have.
		 * The first treats the case when the IR is crossing one of the moTrajectories,
		 * and one of the irTrajectories intercept one moTrajectory while the other
		 * intercept both.
		 * The second treats the case when the IR intercepts a single moTrajectory because
		 * it's "between" both moTrajectories.
		 * The third if consists on the IR crossing one of the moTrajectories, but only
		 * one intersection happens (the IR is going out of the collision area).
		 */
		if (intersectionCount[2] == 1 && mainTrajectoriesCount == 3) {
			if (intersectionCount[0] == 1) {
				intersections.add(irTrajectories[0].start());
			} else /* intersectionCount[1] == 1 */{
				intersections.add(irTrajectories[1].start());
			}

		} else if (intersectionCount[2] == 0 && intersectionCount[1] == 1 && intersectionCount[0] == 1) {
			intersections.add(irTrajectories[0].start());
			intersections.add(irTrajectories[1].start());

		} else if (intersectionCount[2] == 1 && mainTrajectoriesCount == 1) {
			if (intersectionCount[0] == 1) {
				intersections.add(irTrajectories[1].start());
			} else /* intersectionCount[1] == 1 */{
				intersections.add(irTrajectories[0].start());
			}
		}

		return intersections.toArray(new Point[intersections.size()]);
	}



	/**
	 * Calculates the trajectory of the extremities of the IntelligentRobot,
	 * considering its movement direction.
	 * 
	 * @param thoughts
	 * @return
	 */
	private Line[] getIntelligentRobotTrajectory(Thoughts thoughts) {
		final Point points[] = RobotBoundaries
			.pointsFromVerticalAxis(thoughts.myPosition(), thoughts.myDirection());
		return new Line[] {
			new Line(points[0], thoughts.myDirection()),
			new Line(points[1], thoughts.myDirection()),
			new Line(points[0], points[1])
		};
	}

}
