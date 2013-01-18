package br.ufrj.jfirn.intelligent;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.intelligent.Trajectory;

public class TrajectoryTest {
	private static final double TEN_TO_MINUS_3 = .001;

	@Test
	public void testEval() {
		Trajectory t1 = new Trajectory(FastMath.PI / 4d, new Point(-10, -10));
		Trajectory t2 = new Trajectory(FastMath.PI * 3d / 4d, new Point(10, -10));
		Point intersection = t1.intersect(t2);
		assertEquals(0d, intersection.x(), TEN_TO_MINUS_3);
		assertEquals(0d, intersection.y(), TEN_TO_MINUS_3);

		t1 = new Trajectory(1.1071, new Point(0, 3));
		t2 = new Trajectory(-.4636, new Point(0, 7));
		intersection = t1.intersect(t2);
		assertEquals(1.6d, intersection.x(), TEN_TO_MINUS_3);
		assertEquals(6.2d, intersection.y(), TEN_TO_MINUS_3);

		t1 = new Trajectory(0, new Point(0, 4));
		t2 = new Trajectory(FastMath.PI / 4d, new Point(0, 0));
		intersection = t1.intersect(t2);
		assertEquals(4d, intersection.x(), TEN_TO_MINUS_3);
		assertEquals(4d, intersection.y(), TEN_TO_MINUS_3);
	}

}
