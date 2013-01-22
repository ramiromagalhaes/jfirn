package br.ufrj.jfirn.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

import br.ufrj.jfirn.intelligent.evaluation.Interval;


public class Triangle {
	public final Point[] points;

	/**
	 * This map defines what functions I should use to get valid x values for a given y inside the
	 * quadrilateral.
	 */
	private final Map<Interval, UnivariateFunction[]> intervalFunction = new HashMap<>(2);

	public Triangle(final Point a, final Point b, final Point c) {
		points = new Point[3];
		points[0] = a;
		points[1] = b;
		points[2] = c;

		Arrays.sort(points, Point.YThenXComparator.instance);

		//Now we define which function to use in each limit.
		//This series of 'ifs' simply set what functions work as limits of the triangle.
		//There are 3 possibilities of interest: all points have different y positions;
		//2 points have the same y, and they are the lowest point of the triangle;
		//2 points have the same y, and they are the highest point of the triangle.
		if (points[0].y != points[1].y && points[1].y != points[2].y && points[0].y != points[2].y ) {
			intervalFunction.put(new Interval(points[0].y, points[1].y),
					new UnivariateFunction[] {
						getFunction(points[0], points[1]),
						getFunction(points[0], points[2])
					});
			intervalFunction.put(new Interval(points[1].y, points[2].y),
					new UnivariateFunction[] {
						getFunction(points[1], points[2]),
						getFunction(points[0], points[2])
					});

		} else if (points[0].y == points[1].y) {
			intervalFunction.put(new Interval(points[0].y, points[2].y),
					new UnivariateFunction[] {
						getFunction(points[0], points[2]),
						getFunction(points[1], points[2])
					});

		} else if (points[1].y == points[2].y) {
			intervalFunction.put(new Interval(points[0].y, points[2].y),
					new UnivariateFunction[] {
						getFunction(points[0], points[1]),
						getFunction(points[0], points[2])
					});

		} else {
			throw new RuntimeException("Should NEVER get here");
		}
	}

	public double getLowestY() {
		return points[0].y;
	}

	public double getHighestY() {
		return points[2].y;
	}

	/**
	 * Get the left and the right x.
	 */
	public double[] getX(double y) {
		for(Interval interval : intervalFunction.keySet()) {
			if (interval.contains(y)) {
				final UnivariateFunction line[] = intervalFunction.get(interval);

				final double[] values = new double[] {
					line[0].value(y), line[1].value(y)
				};

				Arrays.sort(values);
				return values;
			}
		}

		return null; //input not in Triangle
	}

	/**
	 * Create an UnivariateFunction that allows us to know the X position
	 * of a certain Y. Notice: this UnivariateFunction returns the X for
	 * a certain Y!
	 */
	private UnivariateFunction getFunction(Point a, Point b) {
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

}
