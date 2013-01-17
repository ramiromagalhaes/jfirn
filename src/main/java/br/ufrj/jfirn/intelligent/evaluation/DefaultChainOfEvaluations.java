package br.ufrj.jfirn.intelligent.evaluation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import br.ufrj.jfirn.intelligent.Thoughts;

public class DefaultChainOfEvaluations implements ChainOfEvaluations, ThoughtProcesor {

	private final List<Evaluator> evaluators = new LinkedList<>();
	private Iterator<Evaluator> iterator;

	public DefaultChainOfEvaluations() {
		evaluators.add(new ImmediateDangerEvaluator());
		evaluators.add(new NextTargetEvaluator());
		evaluators.add(new QuickCollisionEvaluator()); //You can try replacing this line with the one bellow
		                                               //evaluators.add(new QuickCollisionWithoutCollisionTimeSimilaritiesEvaluator());
        //evaluators.add(new QuickCollisionWithoutCollisionTimeSimilaritiesEvaluator());
		evaluators.add(new CollisionProbabilityEvaluator());
	}

	/**
	 * 
	 * @see br.ufrj.jfirn.intelligent.evaluation.ThoughtProcesor#evaluate(br.ufrj.jfirn.intelligent.Thoughts)
	 */
	@Override
	public Instruction evaluate(Thoughts thoughts) {
		iterator = evaluators.iterator();
		Instruction instruction = new Instruction();
		nextEvaluator(thoughts, instruction, this);
		return instruction;
	}

	@Override
	public void nextEvaluator(Thoughts thoughts, Instruction instruction, ChainOfEvaluations chain) {
		if (iterator.hasNext()) {
			iterator.next().evaluate(thoughts, instruction, chain);
		}
	}

}
