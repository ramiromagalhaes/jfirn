package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;

/**
 * Univariate Gaussian Distriburion
 * 
 * As described by Bernt Arne Ødegaard in Financial Numerical Recipes in C++
 * 
 * @author Ciro Sobral
 *
 */

public class UGD {
	public static double pdf(double z) {
		return (1d / FastMath.sqrt(2.0 * FastMath.PI) * FastMath.exp(-0.5 * z * z));
	}

	public static double cdf(double z) {
		if (z > 6) return 1;
		if (z < -6) return 0;

		double c[] = {
				0.31938153,
				-0.356563782,
				1.781477937,
				-1.821255978,
				1.330274429};
		double p = 0.2316419;
		double c2 = 0.3989423;
		
		double a = FastMath.abs(z);
		double t = 1d / (1 + a * p);
		double b = c2 * FastMath.exp((-z) * (z / 2));
		double n = ((((c[4] * t + c[3]) * t + c[2]) * t + c[1]) * t + c[0]) * t;
		n = 1d - b * n;
		if(z < 0) n = 1 - n;
		return n;
	}
}
