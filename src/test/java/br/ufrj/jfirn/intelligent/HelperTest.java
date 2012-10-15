package br.ufrj.jfirn.intelligent;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

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

}
