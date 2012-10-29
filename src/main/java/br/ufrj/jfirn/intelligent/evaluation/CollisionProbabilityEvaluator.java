package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.intelligent.Collision;
import br.ufrj.jfirn.intelligent.Thoughts;

public class CollisionProbabilityEvaluator implements Evaluator {

	@Override
	public void evaluate(Thoughts thoughts, Instruction instruction, ChainOfEvaluations chain) {
		for (Collision collision : thoughts.collisions()) {
			//TODO implement
		}

		chain.nextEvaluator(thoughts, instruction, chain);
	}

}
