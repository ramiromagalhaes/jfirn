package br.ufrj.jfirn.intelligent.evaluation;

public class Interval extends org.apache.commons.math3.geometry.euclidean.oned.Interval {

	public Interval(double lower, double upper) {
		super(lower, upper);
	}

	/**
	 * Verifies if x is inside this Interval.
	 * 
	 * @return true if x is a value between this Interval lower and upper
	 *         bounds, false otherwise
	 */
	public boolean contains(double x) {
		return this.getUpper() >= x && this.getLower() <= x;
	}

	@Override
	public String toString() {
		return "Interval [" + this.getLower() + "," + " " + this.getUpper() + "]";
	}

}
