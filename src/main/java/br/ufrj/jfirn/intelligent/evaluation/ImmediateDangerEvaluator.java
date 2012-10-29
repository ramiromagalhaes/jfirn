package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.intelligent.MobileObstacleStatisticsLogger;
import br.ufrj.jfirn.intelligent.Thoughts;

public class ImmediateDangerEvaluator implements Evaluator {

	@Override
	public void evaluate(Thoughts thoughts, Instruction instruction, ChainOfEvaluations chain) {
		//Someone I see is endangering me?

		thoughts.endangered(false); //assume not...

		//...but check for it
		for (MobileObstacleStatisticsLogger stats : thoughts.knownObstacles().values()) {
			if ( this.isInDangerRadius(thoughts.myPosition(), stats.lastKnownPosition()) ) {
				thoughts.endangered(true);
				break;
			}
		}

		if (thoughts.endangered()) {
			instruction.newSpeed = Robot.STOPPED;
		} else {
			instruction.newSpeed = Robot.MAX_SPEED;
		}

		chain.nextEvaluator(thoughts, instruction, chain);
	}

	/**
	 * Used to verify if the particle is in danger.
	 */
	private final static double DANGER_RADIUS = 10;
	private boolean isInDangerRadius(Point myPosition, Point otherPosition) {
		return myPosition.distanceTo(otherPosition) <= DANGER_RADIUS;
	}

}
