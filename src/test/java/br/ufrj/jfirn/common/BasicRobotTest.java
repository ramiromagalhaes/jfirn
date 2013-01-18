package br.ufrj.jfirn.common;

import static org.junit.Assert.*;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class BasicRobotTest {

	@Test
	public void testDirection1() {
		BasicRobot r = new BasicRobot();
		r.direction(5 * FastMath.PI);
		assertEquals(FastMath.PI, r.direction(), 0);
	}

	@Test
	public void testDirection2() {
		BasicRobot r = new BasicRobot();
		r.direction(FastMath.PI);
		assertEquals(FastMath.PI, r.direction(), 0);
	}

	@Test
	public void testDirection3() {
		BasicRobot r = new BasicRobot();
		r.direction(FastMath.PI / 2d);
		assertEquals(FastMath.PI / 2d, r.direction(), 0);
	}

	@Test
	public void testDirection4() {
		BasicRobot r = new BasicRobot();
		r.direction(-5d * FastMath.PI);
		assertEquals(FastMath.PI, r.direction(), 0);
	}

	@Test
	public void testDirection5() {
		BasicRobot r = new BasicRobot();
		r.direction(-FastMath.PI);
		assertEquals(FastMath.PI, r.direction(), 0);
	}

	@Test
	public void testDirection6() {
		BasicRobot r = new BasicRobot();
		r.direction(-FastMath.PI / 2d);
		assertEquals(-FastMath.PI / 2d, r.direction(), 0);
	}

}
