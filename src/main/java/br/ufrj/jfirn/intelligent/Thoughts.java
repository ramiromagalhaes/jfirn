package br.ufrj.jfirn.intelligent;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrj.jfirn.common.Point;

/**
 * This class represents what are an intelligent robot's thoughts and plans.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class Thoughts implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The position I think I'm at. It may be different from the real position
	 * in case the sensors get noisy data.
	 */
	private double x, y; //TODO maybe we should store position as a Point.

	/**
	 * The direction and speed I think I'm going.
	 */
	private double direction, speed;

	/**
	 * True when one should fear for his safety.
	 */
	private boolean endangered = false;

	/**
	 * Stored data about other robots and obstacles.
	 */
	private Map<Integer, MobileObstacleStatisticsLogger> stats = new HashMap<>();

	/**
	 * The target points of interest in the simulation area to where we want to go.
	 */
	private Deque<Point> targets = new ArrayDeque<>();

	/**
	 * Known collision forecast.
	 */
	private List<Collision> collisions = new ArrayList<>();

	/**
	 * Create a zeroed instance of Thoughts. I think I'm the center of
	 * the world.
	 */
	public Thoughts() {
		this.x = 0;
		this.y = 0;
		this.direction = 0;
		this.speed = 0;
	}

	/**
	 * Create an instance from an {@link IntelligentRobot} real data.
	 */
	public Thoughts(IntelligentRobot robot, Map<Integer, MobileObstacleStatisticsLogger> stats, Deque<Point> targets, List<Collision> collisions) {
		//TODO find a nice way to set or preserve this data
		this.x = robot.position().x();
		this.y = robot.position().y();
		this.direction = robot.direction();
		this.speed = robot.speed();

//		this.stats.putAll(stats);
//		this.targets.addAll(targets);
//		this.collisions.addAll(collisions);

		this.stats = stats;
		this.targets = targets;
		this.collisions = collisions;
	}

	public Map<Integer, MobileObstacleStatisticsLogger> knownObstacles() {
		return stats;
	}

	public Deque<Point> targets() {
		return targets;
	}

	public List<Collision> collisions() {
		return collisions;
	}

	public boolean endangered() {
		return endangered;
	}

	public void endangered(boolean e) {
		endangered = e;
	}

	public Point myPosition() {
		return new Point(x, y);
	}

	public double mySpeed() {
		return speed;
	}

	public double myDirection() {
		return direction;
	}

	public void myPosition(Point position) {
		this.x = position.x();
		this.y = position.y();
	}

	public void mySpeed(double speed) {
		this.speed = speed;
	}

	public void myDirection(double direction) {
		this.direction = direction;
	}

}
