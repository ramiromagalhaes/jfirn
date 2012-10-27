package br.ufrj.jfirn.common;

import java.util.Comparator;

import org.apache.commons.math3.util.FastMath;


/**
 * A point in a bidimensional space. This implementation is not mutable.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class Point {

	protected double x, y;

	public Point() {
		x = y = 0;
	}

	public Point(double x, double y) {
		this. x = x;
		this.y = y;
	}

	public final double x() {
		return x;
	}

	public final double y() {
		return y;
	}

	/**
	 * Returns the euclidean distance between this and p.
	 */
	public double distanceTo(final Point p) {
		return FastMath.hypot(
			this.x - p.x,
			this.y - p.y
		);
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("Point[")
			.append(x)
			.append(", ")
			.append(y)
			.append("]")
			.toString();
	}

	/**
	 * Sorts points in X order, smaller to bigger.
	 */
	public static class XComparator implements Comparator<Point> {
		public static final XComparator instance = new XComparator();
		@Override
		public int compare(Point p1, Point p2) {
			return (int)(p1.x - p2.x);
		}
	}

	/**
	 * Sorts points in Y order, smaller to bigger.
	 */
	public static class YComparator implements Comparator<Point> {
		public static final YComparator instance = new YComparator();
		@Override
		public int compare(Point p1, Point p2) {
			return (int)(p1.y - p2.y);
		}
	}

}
