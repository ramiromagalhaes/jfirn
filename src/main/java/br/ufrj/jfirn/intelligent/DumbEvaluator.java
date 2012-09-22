package br.ufrj.jfirn.intelligent;

import java.util.Deque;
import java.util.Map;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.PointParticle;

public class DumbEvaluator implements Evaluator {

	public void evaluate(final PointParticle p, final Map<PointParticle, MovementStatistics> aboutObstacles, final Deque<Point> targets) {
		final Point currentTarget = targets.peek();
		p.direction (
			FastMath.atan2(
				currentTarget.y() - p.y(),
				currentTarget.x() - p.x()
			)
		);
	}

}
