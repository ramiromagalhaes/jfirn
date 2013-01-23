package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.intelligent.MobileObstacleStatistics;
import br.ufrj.jfirn.intelligent.evaluation.RobotBoundaries;

/**
 * A line in a bidimensional space.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class Line {
	private final Point start, end;

	/**
	 * We assume that nothing bigger than this number is of interest to this application.
	 */
	private static final double BIG_NUMBER = 999999;

	public Line(Point p1, Point p2) {
		this.start = p1;
		this.end = p2;
	}

	public Line(Point start, double angle) {
		this(start, angle, Double.POSITIVE_INFINITY);
	}

	public Line(Point start, double angle, double length) {
		if (Double.isInfinite(length)) {
			length = BIG_NUMBER;
		}

		this.start = start;
		this.end = new Point(this.start.x + FastMath.cos(angle) * length,
				            this.start.y + FastMath.sin(angle) * length);
	}

	public double slope() {
		return (end.y() - start.y()) / (end.x() - start.x());
	}

	public double slopeAngle() {
		return FastMath.atan2(end.y() - start.y(), end.x() - start.x());
	}

	public double length() {
		return start.distanceTo(end);
	}

	public Point start() {
		return start;
	}

	public Point end() {
		return end;
	}

	public boolean intersects(Line that) {
		//As seen at http://www.bryceboe.com/2006/10/23/line-segment-intersection-algorithm/
		final Point a = this.start;
		final Point b = this.end;
		final Point c = that.start;
		final Point d = that.end;

		return counterclockwise(a, c, d) != counterclockwise(b, c, d) &&
			   counterclockwise(a, b, c) != counterclockwise(a, b, d);
	}

	public Point intersection(Line that) {
		if (!this.intersects(that)) {
			return null;
		}

		final double denominator =
			(this.start.x - this.end.x) * (that.start.y - that.end.y) -
			(this.start.y - this.end.y) * (that.start.x - that.end.x);

		if (denominator == 0) {
			return null; //this means this and that lines are parallel
		}

		final double partial_1 = this.start.x * this.end.y - this.start.y * this.end.x;
		final double partial_2 = that.start.x * that.end.y - that.start.y * that.end.x;
		final double xNumerator = partial_1 * (that.start.x - that.end.x) - (this.start.x - this.end.x) * partial_2;
		final double yNumerator = partial_1 * (that.start.y - that.end.y) - (this.start.y - this.end.y) * partial_2;

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
			new Line(points[1], mean + stdDeviation),
			new Line(points[0], points[1])
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
			.append(this.start.x)
			.append(", ")
			.append(this.start.y)
			.append(")]");

		return s.toString();
	}



	//As seen at http://www.bryceboe.com/2006/10/23/line-segment-intersection-algorithm/
	private boolean counterclockwise(final Point a, final Point b, final Point c) {
		return (c.y - a.y) * (b.x - a.x) > (b.y - a.y) * (c.x - a.x);
	}

}
