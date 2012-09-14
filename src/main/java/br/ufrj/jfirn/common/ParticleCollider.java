package br.ufrj.jfirn.common;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.util.FastMath;



/**
 * Knows all particles and manages collisions.
 *
 */
public class ParticleCollider {

	private static final double COLLISION_DISTANCE = 10;
	private final Set<PointParticle> particles;

	public ParticleCollider(Set<PointParticle> particles) {
		this.particles = particles;
	}

	//TODO Consider: Maybe this collision stuff should be handled as an observer?
	public Set<PointParticle> checkForCollisions() {
		Set<PointParticle> collisions = new HashSet<>();

		//TODO this code is poor and I know it, but at least I wrote it really fast
		for (PointParticle p1 : particles) {
			for (PointParticle p2 : particles) {
				if ( ParticleCollider.distance(p1, p2) <= COLLISION_DISTANCE ) {
					collisions.add(p1);
					collisions.add(p2);
				}
			}
		}

		return collisions;
	}

	/**
	 * Returns the euclidean distance between p1 and p2.
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double distance(PointParticle p1, PointParticle p2) {
		return FastMath.hypot(
			p1.x() - p2.x(),
			p1.y() - p2.y()
		);
	}

	/**
	 * Returns the euclidean distance between p1 and p2.
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double distance(Point p1, Point p2) {
		return FastMath.hypot(
			p1.x - p2.x,
			p1.y - p2.y
		);
	}

	/**
	 * Returns the euclidean distance between p1 and p2.
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double distance(PointParticle p1, Point p2) {
		return FastMath.hypot(
			p1.x() - p2.x,
			p1.y() - p2.y
		);
	}

}
