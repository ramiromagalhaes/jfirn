package br.ufrj.jfirn.intelligent;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.geometry.Point;

/**
 * Collects data about an observed object.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class MovementStatistics implements MobileObstacleDataLogger, MobileObstacleStatistics {

	private final int observedObjectId;

	private Point lastPosition;

	//visit this page: http://commons.apache.org/math/userguide/stat.html
	private final SummaryStatistics
		x = new SummaryStatistics(),
		y = new SummaryStatistics(),
		speed = new SummaryStatistics(),

		directionTimesSpeed = new SummaryStatistics(),
		//TODO consider simplifying all that so only one of the three attributes bellow are necessary.
		//Beware: lazy cheap work done bellow.
		directionSin = new SummaryStatistics(),
		directionCos = new SummaryStatistics();

	public MovementStatistics(final int observedObjectId) {
		this.observedObjectId = observedObjectId;
	}

	@Override
	public int getObstacleId() {
		return observedObjectId;
	}

	@Override
	public void addEntry(Point position, double speed, double direction) {
		this.lastPosition = position;
		this.x.addValue(position.x());
		this.y.addValue(position.y());

		this.speed.addValue(speed);

		//Found all the code below Weird? See http://en.wikipedia.org/wiki/Directional_statistics#The_fundamental_difference_between_linear_and_circular_statistics
		//See also http://en.wikipedia.org/wiki/Atan2
		final double sin = FastMath.sin(direction);
		final double cos = FastMath.cos(direction);
		this.directionSin.addValue(sin);
		this.directionCos.addValue(cos);

		directionTimesSpeed.addValue(speed * FastMath.atan2(sin, cos));
	}

	@Override
	public int samplesCount() {
		return (int)x.getN();
	}

	@Override
	public Point lastKnownPosition() {
		return lastPosition;
	}

	@Override
	public double xMean() {
		return x.getMean();
	}

	@Override
	public double xVariance() {
		return x.getVariance();
	}

	@Override
	public double yMean() {
		return y.getMean();
	}

	@Override
	public double yVariance() {
		return y.getVariance();
	}

	@Override
	public double speedMean() {
		return speed.getMean();
	}

	@Override
	public double speedVariance() {
		return speed.getVariance();
	}

	@Override
	public double directionMean() {
		return FastMath.atan2(
			directionSin.getMean(),
			directionCos.getMean()
		);
	}

	@Override
	public double directionVariance() {
		return FastMath.pow(
			FastMath.atan2(
				directionSin.getVariance(),
				directionCos.getVariance()
			), 2d);
	}

	@Override
	public void clear() {
		x.clear();
		y.clear();
		speed.clear();
		directionSin.clear();
		directionCos.clear();
		directionTimesSpeed.clear();
	}

	@Override
	public double xyCorrelation() {
		return xyCovariance() / (x.getStandardDeviation() * y.getStandardDeviation());
	}

	@Override
	public double xyCovariance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double speedDirectionCorrelation() {
		final double means = speed.getStandardDeviation() *
			FastMath.atan2(
				directionSin.getStandardDeviation(),
				directionCos.getStandardDeviation()
			);

		return means != 0 ? speedDirectionCovariance() / means : 0;
	}

	@Override
	public double speedDirectionCovariance() {
		return directionTimesSpeed.getMean() - speed.getMean() *
			FastMath.atan2(
				directionSin.getStandardDeviation(),
				directionCos.getStandardDeviation()
			);
	}

}
