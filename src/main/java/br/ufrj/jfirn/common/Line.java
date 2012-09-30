package br.ufrj.jfirn.common;

import org.apache.commons.math3.util.FastMath;

/**
 * A line in a bidimensional space.
 * 
 * @author <a href="mailto:cirosobral@gmail.com">Ciro Esteves Lima Sobral</a>
 *
 */
public class Line {
	protected Point p1, p2;

	public Line() {
		p1 = p2 = new Point();
	}

	public Line(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public Line(double x1, double y1, double x2, double y2) {
		this.p1 = new Point(x1, y1);
		this.p2 = new Point(x2, y2);
	}

	/**
	 * Tests whether a line intersects another line in the same plane.
	 *  
	 * @param line to check against
	 * @return true if this line intersects another, false otherwise 
	 */
	public boolean intersects(Line line) {
		// Draws a bounding box for each line and returns false if there's not overlap.
		if (! (FastMath.max(p1.x, p2.x) >= FastMath.min(line.p1.x, line.p2.x) &&
				FastMath.max(line.p1.x, line.p2.x) >= FastMath.min(p1.x, p2.x) &&
				FastMath.max(p1.y, p2.y) >= FastMath.min(line.p1.y, line.p2.y) &&
				FastMath.max(line.p1.y, line.p2.y) >= FastMath.min(p1.y, p2.y))) {
			return false;
		} else {

		// Tests if the points of the testing line are above or below the other. If both are above or below the lines doesn't intersect.
		double z1 = ((line.p1.x - p1.x) * (p2.y - p1.y)) - ((line.p1.y - p1.y) * (p2.x - p1.x));
		double z2 = ((line.p2.x - p1.x) * (p2.y - p1.y)) - ((line.p2.y - p1.y) * (p2.x - p1.x));
		double s1 = FastMath.signum(z1);
		double s2 = FastMath.signum(z2);

		if ((s1 == 0 || s2 == 0) || (s1 != s2))
			return true;
		else
			return false;
		}
	}
}
