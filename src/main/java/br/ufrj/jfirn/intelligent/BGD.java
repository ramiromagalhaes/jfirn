package br.ufrj.jfirn.intelligent;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;

/**
 * Bivariate Gaussian Distribution
 * 
 * As described by Bernt Arne Ødegaard in Financial Numerical Recipes in C++ 
 * 
 * @author Ciro Sobral
 *
 */

public class BGD {
	private static final NormalDistribution normal = new NormalDistribution(0, 1);
	private static final double A[] = {0.3253030, 0.4211071, 0.1334425, 0.006374323};
	private static final double B[] = {0.1337764, 0.6243247, 1.3425378, 2.2626645};

	private static double f(double x, double y, double aprime, double bprime, double rho) {
		return FastMath.exp(aprime * (2d * x - aprime) + bprime * (2d * y - bprime) + 2d * rho * (x - aprime) * (y - bprime));
	}

	/**
	 * Returns P(X < a, Y < b, c) where X, Y are gaussian random variables N(0, 1)
	 * of the bivariate normal distribution with correlation c in [-1, 1].
	 */
	public static double cdf(double a, double b, double c) {
		if((a <= 0) && (b <= 0) && (c <= 0)) {
			final double aprime = a/FastMath.sqrt(2d * (1d - c*c));
			final double bprime = b/FastMath.sqrt(2d * (1d - c*c));
			double sum = 0;
			for(int i = 0 ; i < A.length ; i++) {
				for (int j = 0; j < A.length; j++) {
					sum += A[i] * A[j] * f(B[i], B[j], aprime, bprime, c);
				}
			}

			sum *= FastMath.sqrt(1d - c * c) / FastMath.PI;

			return sum;
		} else if(a * b * c <= 0) {
			if ((a <= 0 ) && (b >= 0) && (c >= 0)) {
				return normal.cumulativeProbability(a) - cdf(a, -b, -c);  
			} else if ((a >= 0 ) && (b <= 0) && (c >= 0)) {
				return normal.cumulativeProbability(b) - cdf(-a, b, -c);  
			} else if ((a >= 0 ) && (b >= 0) && (c <= 0)) {
				return normal.cumulativeProbability(a) + normal.cumulativeProbability(b) - 1 + cdf(-a, -b, c);  
			}
		} else if(a * b * c >= 0) {
			final double denum = FastMath.sqrt(a * a - 2d * c * a * b + b * b);
			final double rho1 = ((c * a - b) * FastMath.signum(a)) / denum;
			final double rho2 = ((c * b - a) * FastMath.signum(b)) / denum;
			final double delta = (1d - FastMath.signum(a) * FastMath.signum(b)) / 4d;
			return cdf(a, 0, rho1) + cdf(b, 0, rho2) - delta;
		}

		throw new RuntimeException("Should never get here.");
	}
}
