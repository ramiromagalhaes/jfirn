package br.ufrj.jfirn.intelligent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BGDTest {

	@Test
	public void testSimple() {
		assertEquals(0.25d, BGD.cdf(0, 0, 0), 0.00001d);

		assertEquals(0.5d, BGD.cdf(0, Double.MAX_VALUE, 0), 0.00001d);
		assertEquals(0.5d, BGD.cdf(Double.MAX_VALUE, 0, 0), 0.00001d);

		assertEquals(1d, BGD.cdf(Double.MAX_VALUE, Double.MAX_VALUE, 0), 0.00001d);

		assertEquals(0d, BGD.cdf(-Double.MAX_VALUE, -Double.MAX_VALUE, 0), 0.00001d);

		//Normalização: (valor - media) / sqrt(var)
		
	}

}
