package br.ufrj.jfirn.intelligent;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import br.ufrj.jfirn.common.BasicRobot;
import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.intelligent.evaluation.SimpleCollisionEvaluator;
import br.ufrj.jfirn.intelligent.evaluation.SimpleCollisionEvaluator.Collision;

public class SimpleCollisionEvaluatorTest {
	private static final double TEN_TO_MINUS_3 = .001;

	@Test
	public void testEval() {
		final SimpleCollisionEvaluator evaluator = new SimpleCollisionEvaluator();

		Robot p1 = new BasicRobot(-10, -10, FastMath.PI / 4d, 5);
		Robot p2 = new BasicRobot(10, -10, FastMath.PI * 3d / 4d, 5);

		Collision collision = evaluator.eval(p1, p2);
		assertEquals(0d, collision.position.x(), TEN_TO_MINUS_3);
		assertEquals(0d, collision.position.y(), TEN_TO_MINUS_3);

		p1 = new BasicRobot(0, 3, 1.1071, 5);
		p2 = new BasicRobot(0, 7, -.4636, 5);

		collision = evaluator.eval(p1, p2);
		assertEquals(1.6d, collision.position.x(), TEN_TO_MINUS_3);
		assertEquals(6.2d, collision.position.y(), TEN_TO_MINUS_3);

		p1 = new BasicRobot(0, 4, 0, 5);
		p2 = new BasicRobot(0, 0, FastMath.PI / 4d, 5);

		collision = evaluator.eval(p1, p2);
		assertEquals(4d, collision.position.x(), TEN_TO_MINUS_3);
		assertEquals(4d, collision.position.y(), TEN_TO_MINUS_3);
	}

}
