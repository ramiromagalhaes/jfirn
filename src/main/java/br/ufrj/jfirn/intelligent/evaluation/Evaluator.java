package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.intelligent.Thoughts;

/**
 * Classes implementing this interface should contain the algorithm used to decide
 * what the particle should do considering it's current situation.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public interface Evaluator {

	/**
	 * After completing your evaluation, if you want another evaluator to do its job,
	 * invoke {@link ChainOfEvaluations#nextEvaluator(Thoughts, ChainOfEvaluations)}
	 */
	public void evaluate(Thoughts thoughts, ChainOfEvaluations chain);

}
