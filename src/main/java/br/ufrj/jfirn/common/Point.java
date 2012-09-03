package br.ufrj.jfirn.common;

public class Point {

	public final double x, y;

	public Point(double x, double y) {
		this. x = x;
		this.y = y;
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
