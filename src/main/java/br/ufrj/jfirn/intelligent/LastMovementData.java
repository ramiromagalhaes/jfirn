package br.ufrj.jfirn.intelligent;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;

/**
 * Remembers last information read about an observed object.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class LastMovementData implements MobileObstacleDataLogger, MobileObstacleStatistics {

	private Point lastPosition;

	private double speed, direction;

	private final int observedObjectId;

	private int entriesAdded = 0;

	public LastMovementData(final int observedObjectId) {
		this.observedObjectId = observedObjectId;
	}

	@Override
	public int getObservedObjectId() {
		return observedObjectId;
	}

	@Override
	public void addEntry(Point position, double speed, double direction) {
		this.lastPosition = position;
		this.speed = speed;

		//Weird? See http://en.wikipedia.org/wiki/Directional_statistics#The_fundamental_difference_between_linear_and_circular_statistics
		//See also http://en.wikipedia.org/wiki/Atan2
		this.direction = FastMath.atan2(
			FastMath.sin(direction), FastMath.cos(direction)
		);

		entriesAdded = 1;
	}

	@Override
	public int entriesAdded() {
		return entriesAdded;
	}

	@Override
	public Point lastKnownPosition() {
		return lastPosition;
	}

	@Override
	public double xMean() {
		return lastPosition.x();
	}

	@Override
	public double xVariance() {
		return 0;
	}

	@Override
	public double yMean() {
		return lastPosition.y();
	}

	@Override
	public double yVariance() {
		return 0;
	}

	@Override
	public double speedMean() {
		return speed;
	}

	@Override
	public double speedVariance() {
		return 0;
	}

	@Override
	public double directionMean() {
		return direction;
	}

	@Override
	public void clear() {
		//does nothing. Meaningless.
	}

	@Override
	public double directionVariance() {
		return 0;
	}

	@Override
	public double xyCorrelation() {
		return 0;
	}

	@Override
	public double speedDirectionCorrelation() {
		return 0;
	}

	@Override
	public double xyCovariance() {
		return 0;
	}

	@Override
	public double speedDirectionCovariance() {
		return 0;
	}

}
