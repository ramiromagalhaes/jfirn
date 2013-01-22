package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.intelligent.MobileObstacleStatistics;
import br.ufrj.jfirn.intelligent.evaluation.RobotBoundaries;

/**
 * A line in a bidimensional space.
 * 
 * @author <a href="mailto:cirosobral@gmail.com">Ciro Esteves Lima Sobral</a>
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class Line {
	private final Point p1, p2;

	/**
	 * We assume that nothing bigger than this number is of interest to this application.
	 */
	private static final double BIG_NUMBER = 999999;

	public Line(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public Line(Point start, double angle) {
		this(start, angle, Double.POSITIVE_INFINITY);
	}

	public Line(Point start, double angle, double length) {
		if (Double.isInfinite(length)) {
			length = BIG_NUMBER;
		}

		this.p1 = start;
		this.p2 = new Point(this.p1.x + FastMath.cos(angle) * length,
				            this.p1.y + FastMath.sin(angle) * length);
	}

	public double slope() {
		return (p2.y() - p1.y()) / (p2.x() - p1.x());
	}

	public double slopeAngle() {
		return FastMath.atan2(p2.y() - p1.y(), p2.x() - p1.x());
	}

	public double length() {
		return p1.distanceTo(p2);
	}

	public Point start() {
		return p1;
	}

	public Point end() {
		return p2;
	}

	/**
	 * Tests whether a line intersects another line in the same plane.
	 *  
	 * @param that to check against
	 * @return true if this line intersects another, false otherwise 
	 */
	public boolean intersects(Line that) {
		// Draws a bounding box for each line and returns false if there's not overlap.
		if (!intersectionFastTest(that)) {
			return false;
		} else {
			// Tests if the points of the testing line are above or below the other. If both are above or below the lines doesn't intersect.
			final double z1 = (that.p1.x - this.p1.x) * (this.p2.y - this.p1.y) - (that.p1.y - this.p1.y) * (this.p2.x - this.p1.x);
			final double z2 = (that.p2.x - this.p1.x) * (this.p2.y - this.p1.y) - (that.p2.y - this.p1.y) * (this.p2.x - this.p1.x);
			final int s1 = (int)FastMath.signum(z1);
			final int s2 = (int)FastMath.signum(z2);
			return (s1 == 0 || s2 == 0) || (s1 != s2);
		}
	}

	public Point intersection(Line that) {
		if (!this.intersects(that)) {
			return null;
		}

		final double denominator =
			(this.p1.x - this.p2.x) * (that.p1.y - that.p2.y) -
			(this.p1.y - this.p2.y) * (that.p1.x - that.p2.x);

		if (denominator == 0) {
			return null; //this means this and that lines are parallel
		}

		final double partial_1 = this.p1.x * this.p2.y - this.p1.y * this.p2.x;
		final double partial_2 = that.p1.x * that.p2.y - that.p1.y * that.p2.x;
		final double xNumerator = partial_1 * (that.p1.x - that.p2.x) - (this.p1.x - this.p2.x) * partial_2;
		final double yNumerator = partial_1 * (that.p1.y - that.p2.y) - (this.p1.y - this.p2.y) * partial_2;

		return new Point(xNumerator / denominator,
				         yNumerator / denominator);
	}



	/**
	 * From the statistical data of a mobile obstacle movement, get me the trajectories
	 * that represent the boundaries of its movement.
	 */
	public static Line[] fromStatistics(MobileObstacleStatistics stats) {
		//TODO Should I put this method somewhere else?
		final double mean = stats.directionMean();
		final double stdDeviation = FastMath.sqrt(stats.directionVariance());

		final Point[] points = RobotBoundaries.pointsFromVerticalAxis(stats.lastKnownPosition(), mean);

		return new Line[] {
			new Line(points[0], mean - stdDeviation),
			new Line(points[1], mean + stdDeviation)
		};
	}



	@Override
	public String toString() {
		//sample Trajectory 2 x + 10
		StringBuilder s = new StringBuilder()
			.append("Line [angle:")
			.append(this.slopeAngle())
			.append("; length: ");

		if (length() >= BIG_NUMBER/10) {
			s.append("BIG_NUMBER");
		} else {
			s.append(this.length());
		}

		s.append("; start: (")
			.append(this.p1.x)
			.append(", ")
			.append(this.p1.y)
			.append(")]");

		return s.toString();
	}



	/**
	 * This is a fast test that quickly verifies if the lines will NOT intersect. If it
	 * returns true, it means that further investigation is needed to see if they will
	 * intersect. If it returns false, we're sure that the lines will not intersect.
	 * @param that
	 * @return
	 */
	private boolean intersectionFastTest(final Line that) {
		return  FastMath.max(this.p1.x, this.p2.x) >= FastMath.min(that.p1.x, that.p2.x) &&
				FastMath.max(that.p1.x, that.p2.x) >= FastMath.min(this.p1.x, this.p2.x) &&
				FastMath.max(this.p1.y, this.p2.y) >= FastMath.min(that.p1.y, that.p2.y) &&
				FastMath.max(that.p1.y, that.p2.y) >= FastMath.min(this.p1.y, this.p2.y);
	}

}
