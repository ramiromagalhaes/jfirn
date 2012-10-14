package br.ufrj.jfirn.intelligent;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import br.ufrj.jfirn.common.BasicParticle;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.PointParticle;

public class HelperTest {
	private static final double TEN_TO_MINUS_3 = .001;

	@Test
	public void testDirectionSubtraction() {
		assertEquals(FastMath.PI / 3d,
				Helper.directionSubtraction(FastMath.PI / 3d, FastMath.PI * 2d),
				TEN_TO_MINUS_3);
		assertEquals(FastMath.PI / 3d,
				Helper.directionSubtraction(FastMath.PI * 7d / 3d, 0),
				TEN_TO_MINUS_3);
		assertEquals(FastMath.PI / 3d,
				Helper.directionSubtraction(0, FastMath.PI * 5d / 3d),
				TEN_TO_MINUS_3);
	}

	@Test
	public void testIntersection() {
		PointParticle p1 = new BasicParticle(0, 0, FastMath.PI / 4d, 0); //ignore speed
		PointParticle p2 = new BasicParticle(0, 0, FastMath.PI * 3d / 4d, 0);

		Point intersection = Helper.intersection(p1, p2);
		assertEquals(0d, intersection.x(), TEN_TO_MINUS_3);
		assertEquals(0d, intersection.y(), TEN_TO_MINUS_3);

		p1 = new BasicParticle(0, 3, 1.1071, 0);
		p2 = new BasicParticle(0, 7, -.4636, 0);

		intersection = Helper.intersection(p1, p2);
		assertEquals(1.6d, intersection.x(), TEN_TO_MINUS_3);
		assertEquals(6.2d, intersection.y(), TEN_TO_MINUS_3);

		p1 = new BasicParticle(0, 4, 0, 0);
		p2 = new BasicParticle(0, 0, FastMath.PI / 4d, 0);

		intersection = Helper.intersection(p1, p2);
		assertEquals(4d, intersection.x(), TEN_TO_MINUS_3);
		assertEquals(4d, intersection.y(), TEN_TO_MINUS_3);
	}

}
