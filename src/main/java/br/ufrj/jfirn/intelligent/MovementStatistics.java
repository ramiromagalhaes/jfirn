package br.ufrj.jfirn.intelligent;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import br.ufrj.jfirn.common.Point;

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
		direction = new SummaryStatistics();

	public MovementStatistics(final int observedObjectId) {
		this.observedObjectId = observedObjectId;
	}

	@Override
	public int getObservedObjectId() {
		return observedObjectId;
	}

	@Override
	public void addEntry(Point position, double speed, double direction) {
		this.lastPosition = position;
		this.x.addValue(position.x());
		this.y.addValue(position.y());

		this.speed.addValue(speed);

		//Weird? See http://en.wikipedia.org/wiki/Directional_statistics#The_fundamental_difference_between_linear_and_circular_statistics
		//See also http://en.wikipedia.org/wiki/Atan2
		//this.direction.addValue(FastMath.atan2(FastMath.sin(direction), FastMath.cos(direction)));
		this.direction.addValue(direction);
	}

	@Override
	public int entriesAdded() {
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
		return direction.getMean();
	}

	@Override
	public double directionVariance() {
		return direction.getVariance();
	}

	@Override
	public void clear() {
		x.clear();
		y.clear();
		speed.clear();
		direction.clear();
	}

	@Override
	public double xyCorrelation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double xyCovariance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double speedDirectionCorrelation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double speedDirectionCovariance() {
		// TODO Auto-generated method stub
		return 0;
	}

}
