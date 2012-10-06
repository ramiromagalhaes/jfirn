package br.ufrj.jfirn.common;

import static org.junit.Assert.*;

import org.junit.Test;

import br.ufrj.jfirn.common.BGD;

public class BGDTest {

	@Test
	public void test() {
		assertEquals(0.25d, BGD.cdf(0, 0, 0), 0.00001d);
	}

}
