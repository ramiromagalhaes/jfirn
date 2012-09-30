package br.ufrj.jfirn.common;

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
	 * Adds a vector (i.e. an "arrow" with a certain size pointing to a a
	 * certain direction) to this point.
	 * 
	 * This method creates a new instance of Point with the results.
	 * 
	 * @param direction angle, in radians
	 * @param size the size of the movement 
	 * @return a new Point at this point plus the size pointed at a certain
	 * direction.
	 */
	public Point add(final double direction, double size) {
		//TODO WTF is that for?
		return new Point(
			this.x + FastMath.cos(direction) * size,
			this.y + FastMath.sin(direction) * size
		);
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

}
