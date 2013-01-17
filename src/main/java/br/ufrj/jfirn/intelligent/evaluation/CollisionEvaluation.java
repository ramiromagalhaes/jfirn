package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.intelligent.Collision;



public class CollisionEvaluation {

	/**
	 * The id of the object this evaluation is about.
	 */
	private final int aboutObstacleId;

	/**
	 * May be null if there is no collision forecast.
	 */
	private final Collision collision;

	/**
	 * Explains why we reached such conclusion about the collision.
	 */
	private Reason reason;

	public CollisionEvaluation(final Collision collision) {
		this.aboutObstacleId = collision.withObjectId;
		this.collision = collision;
		this.reason = Reason.NO_REASON;
	}

	public CollisionEvaluation(final int aboutObstacleId, final Reason reason) {
		this.aboutObstacleId = aboutObstacleId;
		this.collision = null;
		reason(reason);
	}

	public CollisionEvaluation(final Collision collision, final Reason reason) {
		if (collision == null) {
			throw new IllegalArgumentException("Collision cannot be null.");
		}

		this.aboutObstacleId = collision.withObjectId;
		this.collision = collision;
		this.reason(reason);
	}

	public boolean hasCollision() {
		return this.collision != null;
	}

	public Collision collision() {
		return collision;
	}

	public int obstacleId() {
		return this.aboutObstacleId;
	}

	public Reason reason() {
		return reason;
	}

	@Override
	public int hashCode() {
		return aboutObstacleId;
	}

	public void reason(final Reason reason) {
		if (reason == null) {
			this.reason = Reason.NO_REASON;
		} else {
			this.reason = reason;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CollisionEvaluation)) {
			return false;
		}

		CollisionEvaluation c = (CollisionEvaluation) obj;

		return c.aboutObstacleId == this.aboutObstacleId;
	}

}
