package br.ufrj.jfirn.intelligent.evaluation;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Polygon;

public class BGDTest {
	//took this 0.116516235668598d result from Matlab
	private final double FROM_MATLAB = 0.116516235668598d;

	@Test
	public void testCdf() {
		assertEquals(0.25d, BGD.cdf(0, 0, 0), 0.00001d);

		assertEquals(0.5d, BGD.cdf(0, Double.MAX_VALUE, 0), 0.00001d);
		assertEquals(0.5d, BGD.cdf(Double.MAX_VALUE, 0, 0), 0.00001d);

		assertEquals(1d, BGD.cdf(Double.MAX_VALUE, Double.MAX_VALUE, 0), 0.00001d);

		assertEquals(0d, BGD.cdf(-Double.MAX_VALUE, -Double.MAX_VALUE, 0), 0.00001d);

		assertEquals(0.99730201d, BGD.cdf(3, 3, 0), 0.00001d);
	}

	@Test
	public void testCdfPolygon() {
		final double l = FastMath.scalb(1, -4);

		final Point a = new Point(0, 0);
		final Point b = new Point(l, 0);
		final Point c = new Point(2d * l, 2d * l);
		final Point d = new Point(3d * l, 2d * l);

		final double expected =
			BGD.cdfOfRectangle(2.5d * l, 2d * l, l, l, 0) +
			BGD.cdfOfRectangle(1.5d * l,      l, l, l, 0);

		Polygon p = new Polygon(new Point[] {a, b, c, d});

		assertEquals(expected, BGD.cdf(p, 0), 0.00001d);
	}

	@Test
	public void testCdfPolygon2() {
		Polygon p = new Polygon(new Point[] {
			new Point(0d, 0d),
			new Point(1d, 0d),
			new Point(0d, 1d),
			new Point(1d, 1d),
		});

		assertEquals(FROM_MATLAB, BGD.cdf(p, 0), 0.00001d);
	}

	@Test
	public void testCdfPolygon3() {
		final double l = FastMath.scalb(1, -4);

		Polygon p = new Polygon(new Point[] {
			new Point(0, 0),
			new Point(l, 0),
			new Point(2d * l, 2d * l),
			new Point(3d * l, 2d * l)
		});

		final double expected =
			BGD.cdfOfRectangle(2.5d * l, 2d * l, l, l, 0) +
			BGD.cdfOfRectangle(1.5d * l,      l, l, l, 0);

		assertEquals(expected, BGD.cdf(p, 0), 0.00001d);
	}

}
