package br.ufrj.jfirn.intelligent;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.BasicParticle;
import br.ufrj.jfirn.common.ParticleCollider;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.PointParticle;

public class IntelligentParticle extends BasicParticle implements Sight {

	private static final double MAX_SPEED = 5;

	private Map<PointParticle, MovementStatistics> aboutObstacles = new HashMap<>();
	private Point target;

	public IntelligentParticle(Point target) {
		super();
		this.target = target;
	}

	public IntelligentParticle(double x, double y, double direction, double speed, Point target) {
		super(x, y, direction, speed);
		this.target = target;
	}

	/**
	 * Handles particle seen events.
	 * This method should do the minimum necessary work here. Movement decision
	 * logic should be put inside the {@link #move()} method.
	 */
	@Override
	public void onSight(SightEvent e) {
		//TODO yeah, I should definitely put some logging code everywhere...
		System.out.println(this + " sees: " + e.getParticlesSighted());

		for (PointParticle p : e.getParticlesSighted()) {
			//if another particle threatens the agent, stop; else cruise at maximum speed.
			if ( this.isEndangered(p) ) {
				this.speed(0);
				break;
			} else {
				this.speed(MAX_SPEED);
			}
		}


		//I'll only keep statistics about objects I see
		aboutObstacles.keySet().retainAll(e.getParticlesSighted());

		for (PointParticle p : e.getParticlesSighted()) {
			//store data about objects I see
			if ( !aboutObstacles.containsKey(p) ) {
				aboutObstacles.put(p, new MovementStatistics());
			}

			aboutObstacles.get(p).addEntry(p.x(), p.y(), p.speed(), p.direction());
		}
	}

	/**
	 * Moves the robot, but thinks about what move it should be.
	 */
	@Override
	public void move() {
		//what about all that collected data? I should use now!

		//standard move action: move towards target
		direction (
			FastMath.atan2(
				target.y - this.y(),
				target.x - this.x()
			)
		);

		super.move();
	}

	private final static double DANGER_RADIUS = 100;
	private boolean isEndangered(PointParticle p) {
		return DANGER_RADIUS <= ParticleCollider.distance(this, p);
	}

}
