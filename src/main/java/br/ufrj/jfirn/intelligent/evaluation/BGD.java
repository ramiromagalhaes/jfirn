package br.ufrj.jfirn.intelligent.evaluation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;

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

	/**
	 * As described by Bernt Arne Ødegaard in Financial Numerical Recipes in C++. 
	 * 
	 * Returns P(X < a, Y < b, c) where X, Y are gaussian random variables N(0, 1)
	 * of the bivariate normal distribution with correlation between X and Y c in [-1, 1].
	 */
	public static double cdf(double a, double b, double c) {
		if (a == Double.NaN || b == Double.NaN || c == Double.NaN) {
			throw new IllegalArgumentException("Arguments must be a number.");
		}

		a = handleInfinity(a);
		b = handleInfinity(b);
		c = handleInfinity(c);

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
		}

		                     //a or b may be too big and their multiplication may result in NaN.
		if(c * a * b <= 0) { //c is smaller (between [-1, 1]) and will help not result NaNs. So we multiply c first.
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

		throw new RuntimeException("Should never get here.");
	}

	/**
	 * 
	 * @param higherX
	 * @param higherY
	 * @param length
	 * @param height
	 * @param c Correlation between distributions.
	 * @return
	 */
	public static double cdfOfRectangle(double higherX, double higherY, double length, double height, double c) {
		return BGD.cdf(higherX, higherY, c) - 
			BGD.cdf(higherX, higherY - height, c) -
			BGD.cdf(higherX - length, higherY, c) +
			BGD.cdf(higherX - length, higherY - height, c);
	}

	/**
	 * 
	 * @param higherPoint
	 * @param length
	 * @param height
	 * @param c Correlation between distributions.
	 * @return
	 */
	public static double cdfOfRectangle(Point higherPoint, double length, double height, double c) {
		return cdfOfRectangle(higherPoint.x(), higherPoint.y(), length, height, c);
	}


	private static final double squareHeight = FastMath.scalb(1, -4);

	/**
	 * Calculates an aproximate value for the BGD inside a certain quadrilateral area delimited
	 * by points a, b, c and d.
	 */
	public static double cdfOfConvexQuadrilaterals(Point a, Point b, Point c, Point d, double correlation) {
		//I need to know how are those points positioned, so I sort them.
		final Point[] points = arrangePointsAsConvexQuadrilateral(a, b, c, d);

		//The rainge of values this algorithm works with is [-3, 3].  
		applyLimitsToPoints(points); //TODO after all, this method makes any sense?

		final UnivariateFunction f1 = getFunction(points[1], points[0]);
		final UnivariateFunction f2 = getFunction(points[2], points[1]);
		final UnivariateFunction f3 = getFunction(points[3], points[2]);
		final UnivariateFunction f4 = getFunction(points[0], points[3]);

		//The Y dimension is limited by a horizontal line. The X dimension is limited by 2 functions.
		//These functions get the X value from the provided Y value.
		final UnivariateFunction rightmostFunction = getFunction(points[3], points[1]);
		final UnivariateFunction leftmostFunction = getFunction(points[2], points[0]);

		double total = 0; //will return this

		double currentY = points[3].y();
		while (currentY > points[0].y()) {
			final double rightmostX = rightmostFunction.value(currentY - squareHeight/2d);
			final double leftmostX = leftmostFunction.value(currentY - squareHeight/2d);
			final double xLength = rightmostX - leftmostX;

			total += BGD.cdfOfRectangle(rightmostX, currentY, xLength, squareHeight, correlation);
			currentY -= squareHeight;
		}

		return total;
	}



	//== This class private parts. Be gentle.



	/**
	 * As described by Bernt Arne Ødegaard in Financial Numerical Recipes in C++. 
	 */
	private static double f(double x, double y, double aprime, double bprime, double rho) {
		return FastMath.exp(aprime * (2d * x - aprime) + bprime * (2d * y - bprime) + 2d * rho * (x - aprime) * (y - bprime));
	}


	//== Private functions for #cdfOfHorizontalQuadrilaterals


	private static Point[] arrangePointsAsConvexQuadrilateral(Point a, Point b, Point c, Point d) {
		//TODO Not sure if it will perform well enough.
		final Point center = new Point(
			(a.x() + b.x() + c.x() + d.x()) / 4d,
			(a.y() + b.y() + c.y() + d.y()) / 4d
		);

		final Map<Double, Point> map = new HashMap<>(4);
		map.put(center.directionTo(a), a);
		map.put(center.directionTo(b), b);
		map.put(center.directionTo(c), c);
		map.put(center.directionTo(d), d);

		Double[] angles = new Double[4];
		angles = map.keySet().toArray(angles);
		Arrays.sort(angles);

		final LinkedList<Point> toReturn = new LinkedList<>();
		for(Double angle : angles) {
			toReturn.addLast(map.get(angle));
		}

		return toReturn.toArray(new Point[4]);
	}

	/**
	 * Create an UnivariateFunction that allows us to know the X position
	 * of a certain Y. Notice: this UnivariateFunction returns the X for
	 * a certain Y!
	 */
	private static UnivariateFunction getFunction(Point a, Point b) {
		double alpha = (a.y() - b.y()) / (a.x() - b.x());

		if (Double.isInfinite(alpha)) {
			return new PolynomialFunction(new double[]{a.x()});
		} else {
			double beta = a.y() - alpha * a.x();
			return new PolynomialFunction(new double[]{
				- beta / alpha, 1 / alpha
			});
		}
	}

	private static double handleInfinity(double n) {
		if (Double.isInfinite(n)) {
			n = FastMath.copySign(Double.MAX_VALUE, n);
		}

		return n;
	}

	private static void applyLimitsToPoints(Point[] points) {
		for(int i = 0; i < points.length; i++) {
			final double x = FastMath.abs(points[i].x()) > 3 ?
				FastMath.copySign(3, points[i].x()) : points[i].x();

			final double y = FastMath.abs(points[i].y()) > 3 ?
				FastMath.copySign(3, points[i].y()) : points[i].y();

			points[i] = new Point(x, y);
		}
	}



	private static class Quadrilateral {
		private final UnivariateFunction f1;
		private final UnivariateFunction f2;
		private final UnivariateFunction f3;
		private final UnivariateFunction f4;

		private Point[] points;

		private double[] ySteps;

		public Quadrilateral(Point a, Point b, Point c, Point d) {
			points = arrangePointsAsConvexQuadrilateral(a, b, c, d);


			f1 = getFunction(points[1], points[0]);
			f2 = getFunction(points[2], points[1]);
			f3 = getFunction(points[3], points[2]);
			f4 = getFunction(points[0], points[3]);
		}

		/**
		 * Get the left x and the right x.
		 */
		public double[] getX(double y) {
			final double rightmostX = f1.value(y - squareHeight/2d);
			final double leftmostX = f2.value(y - squareHeight/2d);

			return new double[2];
		}

		private Point[] arrangePointsAsConvexQuadrilateral(Point a, Point b, Point c, Point d) {
			//TODO Not sure if it will perform well enough.
			final Point center = new Point(
				(a.x() + b.x() + c.x() + d.x()) / 4d,
				(a.y() + b.y() + c.y() + d.y()) / 4d
			);

			final Map<Double, Point> map = new HashMap<>(4);
			map.put(center.directionTo(a), a);
			map.put(center.directionTo(b), b);
			map.put(center.directionTo(c), c);
			map.put(center.directionTo(d), d);

			Double[] angles = new Double[4];
			angles = map.keySet().toArray(angles);
			Arrays.sort(angles);

			final LinkedList<Point> toReturn = new LinkedList<>();
			for(Double angle : angles) {
				toReturn.addLast(map.get(angle));
			}

			return toReturn.toArray(new Point[4]);
		}

	}

}
