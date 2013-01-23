package br.ufrj.jfirn.intelligent.evaluation;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Polygon;
import br.ufrj.jfirn.common.Triangle;

/**
 * Bivariate Gaussian Distribution utility functions.
 * 
 * @author Ciro Sobral 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class BGD {
	private static final NormalDistribution normal = new NormalDistribution(0, 1);
	private static final double A[] = {0.3253030, 0.4211071, 0.1334425, 0.006374323};
	private static final double B[] = {0.1337764, 0.6243247, 1.3425378, 2.2626645};

	private static final double squareHeight = FastMath.scalb(1, -4);

	/**
	 * As described by Bernt Arne Ødegaard in Financial Numerical Recipes in C++. 
	 * 
	 * Returns P(X < a, Y < b) where X, Y are gaussian random variables N(0, 1)
	 * of the bivariate normal distribution with correlation c in [-1, 1] between X and Y.
	 */
	public static double cdf(double a, double b, double c) {
		if (a == Double.NaN || b == Double.NaN || c == Double.NaN) {
			throw new IllegalArgumentException("Arguments must be a number.");
		}

		if (a == Double.NaN) {
			System.out.println("");
		}

		a = handleInfinity(a);
		b = handleInfinity(b);
		c = handleInfinity(c);

		if (a == Double.NaN) {
			System.out.println("");
		}

		if(a <= 0 && b <= 0 && c <= 0) {
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
		}

		                     //a or b may be too big and their multiplication may result in NaN.
		if(c * a * b <= 0) { //c is smaller (between [-1, 1]) and will help to avoid NaNs. So we multiply c first.
			if ((a <= 0 ) && (b >= 0) && (c >= 0)) {
				return normal.cumulativeProbability(a) - cdf(a, -b, -c);  
			} else if ((a >= 0 ) && (b <= 0) && (c >= 0)) {
				return normal.cumulativeProbability(b) - cdf(-a, b, -c);  
			} else if ((a >= 0 ) && (b >= 0) && (c <= 0)) {
				return normal.cumulativeProbability(a) + normal.cumulativeProbability(b) - 1 + cdf(-a, -b, c);  
			}
		} else if(c * a * b >= 0) {
			final double denum = FastMath.sqrt(a * a - 2d * c * a * b + b * b);
			final double rho1 = ((c * a - b) * FastMath.signum(a)) / denum;
			final double rho2 = ((c * b - a) * FastMath.signum(b)) / denum;
			final double delta = (1d - FastMath.signum(a) * FastMath.signum(b)) / 4d;
			return cdf(a, 0, rho1) + cdf(b, 0, rho2) - delta;
		}

		throw new RuntimeException("Should never get here. Values of [a; b ; c] = [" + a + "; " + b + "; " + c + "].");
	}

	//TODO write documentation
	public static double cdfOfRectangle(double higherX, double higherY, double length, double height, double c) {
		return BGD.cdf(higherX, higherY, c) - 
			BGD.cdf(higherX, higherY - height, c) -
			BGD.cdf(higherX - length, higherY, c) +
			BGD.cdf(higherX - length, higherY - height, c);
	}

	//TODO write documentation
	public static double cdfOfRectangle(Point higherPoint, double length, double height, double correlation) {
		return cdfOfRectangle(higherPoint.x(), higherPoint.y(), length, height, correlation);
	}

	/**
	 * Calculates an aproximate value for the BGD inside a certain triangle area.
	 */
	public static double cdf(Triangle triangle, double correlation) {
		/*triangle = new Triangle(
			applyLimitsToPoint(triangle.points[0]),
			applyLimitsToPoint(triangle.points[1]),
			applyLimitsToPoint(triangle.points[2])
		);*/

		double total = 0; //will return this
		boolean iterate = true;

		double currentY = triangle.getLowestY();
		while (iterate) {
			double increment = squareHeight;

			if (currentY + increment >= triangle.getHighestY()) {
				increment = triangle.getHighestY() - currentY;
				iterate = false;
			}

			currentY += increment;

			final double[] x = triangle.getX(currentY - increment/2d);

			total += BGD.cdfOfRectangle(x[1], currentY, x[1] - x[0], increment, correlation);
		}

		return total;
	}

	/**
	 * Calculates an aproximate value for the BGD inside a certain polygon area.
	 */
	public static double cdf(Polygon polygon, double correlation) {
		final Triangle[] triangles = polygon.toTriangles();

		double total = 0;
		for (final Triangle t : triangles) {
			total += cdf(t, correlation);
		}

		return total;
	}



	//== This class private parts. Be gentle.



	/**
	 * As described by Bernt Arne Ødegaard in Financial Numerical Recipes in C++.
	 */
	private static double f(double x, double y, double aprime, double bprime, double c) {
		return FastMath.exp(aprime * (2d * x - aprime) + bprime * (2d * y - bprime) + 2d * c * (x - aprime) * (y - bprime));
	}

	/**
	 * Since the algorithm to calculate the CDF of a bivariate gaussian
	 * distribution fails with infinity, we change infinity to the
	 * maximum value a Double can be. This allows the mentioned algorithm
	 * to provide the appropriate answer.
	 */
	private static double handleInfinity(double n) {
		if (Double.isInfinite(n)) {
			n = FastMath.copySign(Double.MAX_VALUE, n);
		}

		return n;
	}

	/**
	 * If the parameter's x or y values are bigger than a limit,
	 * create a new {@link Point} under this limit.
	 */
	private static Point applyLimitsToPoint(Point point) {
		//we'll avoid creating new instances of Point
		final double limit = 3d;
		boolean somethingChanged = false;
		double x = 0, y = 0;

		if (FastMath.abs(point.x()) > limit) {
			x = FastMath.copySign(limit, point.x());
			somethingChanged = true;
		}

		if (FastMath.abs(point.y()) > 3) {
			y = FastMath.copySign(limit, point.y());
			somethingChanged = true;
		}

		if (somethingChanged) {
			return new Point(x, y);
		} else {
			return point;
		}
	}

}
