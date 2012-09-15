package br.ufrj.jfirn.intelligent;

import java.util.Deque;
import java.util.Map;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.PointParticle;

/**
 * Classes implementing this interface should contain the algorithm used to decide
 * what the particle should do considering it's current situation.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public interface Evaluator {

	public void evaluate(final PointParticle p, final Map<PointParticle, MovementStatistics> aboutObstacles, final Deque<Point> targets);

}
