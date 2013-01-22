package br.ufrj.jfirn.intelligent;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import br.ufrj.jfirn.common.Line;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;

public class LineTest {

	private static final double ERROR = .001;

	@Test
	public void testIntersection1() {
		Line t1 = new Line(new Point(-10, -10), FastMath.PI / 4d);
		Line t2 = new Line(new Point(10, -10), FastMath.PI * 3d / 4d);
		Point intersection = t1.intersection(t2);
		assertEquals(0d, intersection.x(), ERROR);
		assertEquals(0d, intersection.y(), ERROR);
	}

	@Test
	public void testIntersection2() {
		Line t1 = new Line(new Point(0, 3), 1.1071);
		Line t2 = new Line(new Point(0, 7), -.4636);
		Point intersection = t1.intersection(t2);
		assertEquals(1.6d, intersection.x(), ERROR);
		assertEquals(6.2d, intersection.y(), ERROR);
	}

	@Test
	public void testIntersection3() {
		Line t1 = new Line(new Point(0, 4), 0);
		Line t2 = new Line(new Point(0, 0), FastMath.PI / 4d);
		Point intersection = t1.intersection(t2);
		assertEquals(4d, intersection.x(), ERROR);
		assertEquals(4d, intersection.y(), ERROR);
	}

	@Test
	public void testIntersection4() {
		Line t1 = new Line(new Point(300, 280), Robot.DOWN);
		Line t2 = new Line(new Point(200, 200), new Point(400, 100));
		Point intersection = t1.intersection(t2);
		assertEquals(300, intersection.x(), ERROR);
		assertEquals(150, intersection.y(), ERROR);
	}

}
