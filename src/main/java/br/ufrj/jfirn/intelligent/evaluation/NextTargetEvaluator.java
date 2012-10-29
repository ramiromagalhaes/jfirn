package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.intelligent.Thoughts;

public class NextTargetEvaluator implements Evaluator {

	@Override
	public void evaluate(Thoughts thoughts, Instruction instruction,
			ChainOfEvaluations chain) {
		//Do I have any additional targets to go after?
		if (thoughts.targets().isEmpty()) {
			instruction.newSpeed = Robot.STOPPED;
			//TODO Should I fire some event to the simulation saying I'm done?
			return;
		}

		final Point currentTarget = thoughts.targets().peek(); //This is where should I move to now.

		//Did I arrive somewhere I wanted to?
		if (isInReachRadius(thoughts.myPosition(), currentTarget)) {
			//TODO Think about how I'm gonna log this.
			//logger.debug("Arrived at target " + currentTarget + ". My current position is " + this.position() + ".");
			thoughts.targets().pop();
		}

		if (currentTarget != null) {
			instruction.newDirection =
				currentTarget.directionTo(thoughts.myPosition());
		}

		chain.nextEvaluator(thoughts, instruction, chain);
	}

	/**
	 * Used to verify if I'm close enough to a target.
	 */
	private final static double REACH_RADIUS = 10;
	private boolean isInReachRadius(Point myPosition, Point otherPosition) {
		return myPosition.distanceTo(otherPosition) <= REACH_RADIUS;
	}

}
