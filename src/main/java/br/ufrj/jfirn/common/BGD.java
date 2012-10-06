package br.ufrj.jfirn.common;

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


	private static double f(double x, double y, double aprime, double bprime, double rho) {
		return FastMath.exp(aprime * (2 * x - aprime) + bprime * (2 * y - bprime) + 2 * rho * (x - aprime) * (y - bprime));
	}

	// Numerical approximation to the bivariate normal distribution	
	public static double cdf(double a, double b, double rho) {

		if((a <= 0) && (b <= 0) && (rho <= 0)) {
			double aprime = a/FastMath.sqrt(2 * (1 - rho*rho));
			double bprime = b/FastMath.sqrt(2 * (1 - rho*rho));
			double A[] = {0.3253030, 0.4211071, 0.1334425, 0.006374323};
			double B[] = {0.1337764, 0.6243247, 1.3425378, 2.2626645};
			double sum = 0;
			for(int i = 0 ; i < A.length ; i++)
				for (int j = 0; j < A.length; j++)
					sum += A[i] * A[j] * f(B[i], B[j], aprime, bprime, rho);

			sum *= FastMath.sqrt(1 - rho * rho) / FastMath.PI;

			return sum;
		} else if(a * b * rho <= 0) {
			if ((a <= 0 ) && (b >= 0) && (rho >= 0)) {
				return normal.cumulativeProbability(a) - cdf(a, -b, -rho);  
			} else if ((a >= 0 ) && (b <= 0) && (rho >= 0)) {
				return normal.cumulativeProbability(b) - cdf(-a, b, -rho);  
			} else if ((a >= 0 ) && (b >= 0) && (rho <= 0)) {
				return normal.cumulativeProbability(a) + normal.cumulativeProbability(b) - 1 + cdf(-a, -b, rho);  
			}
		} else if(a * b * rho >= 0) {
			double denum = FastMath.sqrt(a * a - 2 * rho * a * b + b * b);
			double rho1 = ((rho * a - b) * FastMath.signum(a)) / denum;
			double rho2 = ((rho * b - a) * FastMath.signum(b)) / denum;
			double delta = (1 - FastMath.signum(a) * FastMath.signum(b)) / 4d;
			return cdf(a, 0, rho1) + cdf(b, 0, rho2) - delta;
		}

		throw new RuntimeException("Should never get here.");
	}
}