package br.ufrj.jfirn.common;

import static org.junit.Assert.*;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class PointTest {

	@Test
	public void testDistanceTo() {
		assertEquals(FastMath.sqrt(2d), new Point(0,0).distanceTo(new Point(1,1)), 0d);
	}

	@Test
	public void testDirectionTo() {
		assertEquals(FastMath.PI/4d, new Point(0,0).directionTo(new Point(1,1)), 0d);
		assertEquals(FastMath.PI, new Point(0,0).directionTo(new Point(-1,0)), 0d);
		assertEquals(-3d*FastMath.PI/4d, new Point(0,0).directionTo(new Point(-1,-1)), 0d);
		assertEquals(-FastMath.PI/2d, new Point(0,0).directionTo(new Point(0,-1)), 0d);
	}

}
