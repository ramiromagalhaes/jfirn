package br.ufrj.jfirn.intelligent.evaluation;

import java.util.LinkedList;
import java.util.List;

import br.ufrj.jfirn.intelligent.Thoughts;


/**
 * You may use this after evaluating the collisions probabilities.
 * @see CollisionProbabilityEvaluator
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class LowCollisionProbabilityRemoverEvaluator implements Evaluator {

	@Override
	public void evaluate(Thoughts thoughts, Instruction instruction, ChainOfEvaluations chain) {
		//collisions with small probability will be removed because they won't happen
		final List<Integer> collisionsToPurge = new LinkedList<>();

		for (CollisionEvaluation collisionEvaluation : thoughts.allColisionEvaluations()) {
			if (collisionEvaluation.willCollide() && collisionEvaluation.collision().probability == 0) {
				collisionsToPurge.add(collisionEvaluation.obstacleId());
			}
		}

		thoughts.removeObstacles(collisionsToPurge);
	}

}
