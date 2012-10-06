package br.ufrj.jfirn.intelligent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BGDTest {

	@Test
	public void test() {
		assertEquals(0.25d, BGD.cdf(0, 0, 0), 0.00001d);
	}

}
