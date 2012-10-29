package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.intelligent.Thoughts;

public interface ThoughtProcesor {

	/**
	 * Evaluate my thoughts and give me instructions about what I should do next.
	 */
	Instruction evaluate(Thoughts thoughts);

}