package br.ufrj.jfirn.intelligent;

import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;

/**
 * Collects data about an observed object.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class MovementStatistics {

	/**
	 * The unique identifier of the observed particle.
	 */
	private final int observedObjectId;

	private Point lastPosition;

	private SummaryStatistics
		x = new SummaryStatistics(),
		y = new SummaryStatistics(),
		speed = new SummaryStatistics(),
		direction = new SummaryStatistics();

	public MovementStatistics(final int observedObjectId) {
		this.observedObjectId = observedObjectId;
	}

	public int getObservedObjectId() {
		return observedObjectId;
	}

	public void addEntry(Point position, double speed, double direction) {
		this.lastPosition = position;
		this.x.addValue(position.x());
		this.y.addValue(position.y());
		this.speed.addValue(speed);

		//Weird? See http://en.wikipedia.org/wiki/Directional_statistics#The_fundamental_difference_between_linear_and_circular_statistics
		//See also http://en.wikipedia.org/wiki/Atan2
		this.direction.addValue(
			FastMath.atan2(
				FastMath.sin(direction) , FastMath.cos(direction)
			)
		);
	}

	public Point lastPosition() {
		return lastPosition;
	}

	public StatisticalSummary xStats() {
		return x;
	}

	public StatisticalSummary yStats() {
		return y;
	}

	public StatisticalSummary speedStats() {
		return speed;
	}

	public StatisticalSummary directionStats() {
		return direction;
	}

}
