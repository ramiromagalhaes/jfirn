package br.ufrj.jfirn.intelligent;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import br.ufrj.jfirn.common.BasicParticle;
import br.ufrj.jfirn.common.PointParticle;
import br.ufrj.jfirn.intelligent.SimpleCollisionEvaluator.Collision;

public class SimpleCollisionEvaluatorTest {
	private static final double TEN_TO_MINUS_3 = .001;

	@Test
	public void testEval() {
		final SimpleCollisionEvaluator evaluator = new SimpleCollisionEvaluator();

		PointParticle p1 = new BasicParticle(-10, -10, FastMath.PI / 4d, 5);
		PointParticle p2 = new BasicParticle(10, -10, FastMath.PI * 3d / 4d, 5);

		Collision collision = evaluator.eval(p1, p2);
		assertEquals(0d, collision.position.x(), TEN_TO_MINUS_3);
		assertEquals(0d, collision.position.y(), TEN_TO_MINUS_3);

		p1 = new BasicParticle(0, 3, 1.1071, 5);
		p2 = new BasicParticle(0, 7, -.4636, 5);

		collision = evaluator.eval(p1, p2);
		assertEquals(1.6d, collision.position.x(), TEN_TO_MINUS_3);
		assertEquals(6.2d, collision.position.y(), TEN_TO_MINUS_3);

		p1 = new BasicParticle(0, 4, 0, 5);
		p2 = new BasicParticle(0, 0, FastMath.PI / 4d, 5);

		collision = evaluator.eval(p1, p2);
		assertEquals(4d, collision.position.x(), TEN_TO_MINUS_3);
		assertEquals(4d, collision.position.y(), TEN_TO_MINUS_3);
	}

}
