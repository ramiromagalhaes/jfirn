package br.ufrj.jfirn.intelligent;

import static org.junit.Assert.*;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import br.ufrj.jfirn.common.Point;

public class MovementStatisticsTest {

	@Test
	public void testDirectionMean() {
		MovementStatistics stats = new MovementStatistics(0);
		stats.addEntry(new Point(0, 0), 5, FastMath.PI);
		stats.addEntry(new Point(0, 0), 5, FastMath.PI);
		stats.addEntry(new Point(0, 0), 5, FastMath.PI);

		assertEquals(FastMath.PI, stats.directionMean(), 0);
		assertEquals(0, stats.directionVariance(), 0);
	}

	@Test
	public void testDirectionMean2() {
		MovementStatistics stats = new MovementStatistics(0);
		stats.addEntry(new Point(0, 0), 5, 2d * FastMath.PI);
		stats.addEntry(new Point(0, 0), 5, 0);
		stats.addEntry(new Point(0, 0), 5, FastMath.PI);

		assertEquals(0, stats.directionMean(), 0.000000000000001d);
	}

	@Test
	public void testDirectionMean3() {
		MovementStatistics stats = new MovementStatistics(0);
		stats.addEntry(new Point(0, 0), 5, FastMath.PI);
		stats.addEntry(new Point(0, 0), 5, -FastMath.PI);

		assertEquals(FastMath.PI, stats.directionMean(), 0);
	}

}
