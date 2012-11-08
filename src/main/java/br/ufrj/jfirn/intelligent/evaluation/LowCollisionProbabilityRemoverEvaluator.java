package br.ufrj.jfirn.intelligent.evaluation;

import java.util.LinkedList;
import java.util.List;

import br.ufrj.jfirn.intelligent.Collision;
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

		for (Collision collision : thoughts.allColisions()) {
			if (collision.probability == 0) {
				collisionsToPurge.add(collision.withObjectId);
			}
		}

		for (Integer id : collisionsToPurge) {
			thoughts.removeCollision(id);
		}
	}

}
