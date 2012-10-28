package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.intelligent.Thoughts;

public interface ChainOfEvaluations {

	public void nextEvaluator(Thoughts thoughts, Instruction instruction, ChainOfEvaluations chain);

}
