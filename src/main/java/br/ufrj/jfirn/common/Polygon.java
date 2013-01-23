package br.ufrj.jfirn.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Polygon {
	private List<Point> polygon;

	public Polygon(Point[] points) {
		polygon = ConvexHull.execute(Arrays.asList(points));
	}

	public Triangle[] toTriangles() {
		if (polygon.size() == 3) {
			return new Triangle[] {
				new Triangle(polygon.get(0), polygon.get(1), polygon.get(2))
			};

		} else if (polygon.size() == 4) {
			return new Triangle[] {
				new Triangle(polygon.get(0), polygon.get(1), polygon.get(2)),
				new Triangle(polygon.get(0), polygon.get(3), polygon.get(2))
			};

		} else if (polygon.size() == 5) {
			return new Triangle[] {
				new Triangle(polygon.get(0), polygon.get(1), polygon.get(4)),
				new Triangle(polygon.get(1), polygon.get(3), polygon.get(4)),
				new Triangle(polygon.get(1), polygon.get(2), polygon.get(3))
			};
		}

		throw new IllegalArgumentException("Works only with polygins with 3, 4 or 5 vertices. This one has " + polygon.size() + ".");
	}





	private static class ConvexHull {

		public static List<Point> execute(List<Point> points) {
			List<Point> xSorted = new ArrayList<>(points);

			Collections.sort(xSorted, new XCompare());

			int n = xSorted.size();

			Point[] lUpper = new Point[n];

			lUpper[0] = xSorted.get(0);
			lUpper[1] = xSorted.get(1);

			int lUpperSize = 2;

			for (int i = 2; i < n; i++) {
				lUpper[lUpperSize] = xSorted.get(i);
				lUpperSize++;

				while (lUpperSize > 2
						&& !rightTurn(lUpper[lUpperSize - 3],
								lUpper[lUpperSize - 2], lUpper[lUpperSize - 1])) {
					// Remove the middle point of the three last
					lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
					lUpperSize--;
				}
			}

			Point[] lLower = new Point[n];

			lLower[0] = xSorted.get(n - 1);
			lLower[1] = xSorted.get(n - 2);

			int lLowerSize = 2;

			for (int i = n - 3; i >= 0; i--) {
				lLower[lLowerSize] = xSorted.get(i);
				lLowerSize++;

				while (lLowerSize > 2
						&& !rightTurn(lLower[lLowerSize - 3],
								lLower[lLowerSize - 2], lLower[lLowerSize - 1])) {
					// Remove the middle point of the three last
					lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
					lLowerSize--;
				}
			}

			ArrayList<Point> result = new ArrayList<Point>();

			for (int i = 0; i < lUpperSize; i++) {
				result.add(lUpper[i]);
			}

			for (int i = 1; i < lLowerSize - 1; i++) {
				result.add(lLower[i]);
			}

			return result;
		}

		private static boolean rightTurn(Point a, Point b, Point c) {
			return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x) > 0;
		}

		private static class XCompare implements Comparator<Point> {
			@Override
			public int compare(Point o1, Point o2) {
				return (int) (o1.x - o2.x);
			}
		}
	}
}
