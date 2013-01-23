package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.intelligent.MobileObstacleStatistics;
import br.ufrj.jfirn.intelligent.Thoughts;

public class ImmediateDangerEvaluator implements Evaluator {

	@Override
	public void evaluate(Thoughts thoughts, Instruction instruction, ChainOfEvaluations chain) {
		//Someone I see is endangering me?

		thoughts.endangered(false); //assume not...

		//...but check for it
		for (MobileObstacleStatistics stats : thoughts.allObstacleStatistics()) {
			if ( RobotsUtils.isInDangerRadius(thoughts.myPosition(), stats.lastKnownPosition()) ) {
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

}
