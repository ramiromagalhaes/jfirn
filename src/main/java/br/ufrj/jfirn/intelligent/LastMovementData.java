package br.ufrj.jfirn.intelligent;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;

/**
 * Remembers last information read about an observed object.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class LastMovementData implements ParticleDataLogger {

	/**
	 * The unique identifier of the observed particle.
	 */
	private final int observedObjectId;

	private Point lastPosition;

	private double x, y, speed, direction;

	public LastMovementData(final int observedObjectId) {
		this.observedObjectId = observedObjectId;
	}

	public int getObservedObjectId() {
		return observedObjectId;
	}

	@Override
	public void addEntry(Point position, double speed, double direction) {
		this.lastPosition = position;
		this.x = position.x();
		this.y = position.y();
		this.speed = speed;

		//Weird? See http://en.wikipedia.org/wiki/Directional_statistics#The_fundamental_difference_between_linear_and_circular_statistics
		//See also http://en.wikipedia.org/wiki/Atan2
		this.direction = FastMath.atan2(
			FastMath.sin(direction), FastMath.cos(direction)
		);
	}

	public Point lastPosition() {
		return lastPosition;
	}

	public double xStats() {
		return x;
	}

	public double yStats() {
		return y;
	}

	public double speedStats() {
		return speed;
	}

	public double directionStats() {
		return direction;
	}

}
