package br.ufrj.jfirn.intelligent;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.intelligent.evaluation.CollisionEvaluation;

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
	private Point position;

	/**
	 * The direction and speed I think I'm going.
	 */
	private double direction, speed;

	/**
	 * True when one should fear for his safety.
	 */
	private boolean endangered = false;

	/**
	 * The target points of interest in the simulation area to where we want to go.
	 */
	private Deque<Point> targets = new ArrayDeque<>();

	/**
	 * Obstacles statistics data.
	 */
	private Map<Integer, MovementStatistics> obstacleStatistics = new HashMap<>();

	/**
	 * For each known object (i.e. for each object present in the obstacleStatistics
	 * field), I have a collision evaluation.
	 */
	private Map<Integer, CollisionEvaluation> collisionEvaluations = new HashMap<>();



	/**
	 * Create an empty instance of Thoughts.
	 */
	public Thoughts() {
		this.position = new Point(0, 0);
		this.direction = 0;
		this.speed = 0;
	}

	/**
	 * Create an instance from an {@link IntelligentRobot} real data.
	 */
	public Thoughts(AbstractIntelligentRobot robot, Deque<Point> targets) {
		this.position = robot.position();
		this.direction = robot.direction();
		this.speed = robot.speed();

		this.targets = targets;
	}



	public Deque<Point> targets() {
		return targets;
	}

	public boolean endangered() {
		return endangered;
	}

	public void endangered(boolean e) {
		endangered = e;
	}

	public Point myPosition() {
		return position;
	}

	public double mySpeed() {
		return speed;
	}

	public double myDirection() {
		return direction;
	}

	public void myPosition(Point position) {
		this.position = position;
	}

	public void mySpeed(double speed) {
		this.speed = speed;
	}

	public void myDirection(double direction) {
		this.direction = direction;
	}



	/*
	 * End of simple methods. Begin more complex methods about the obstacle and collision database.
	 */



	/**
	 * If we have any movement statistics about an obstacle with a certain
	 * id, then we know it.
	 */
	public boolean isKnownObstacle(int objectId) {
		return obstacleStatistics.containsKey(objectId);
	}

	public Collection<Integer> knownObstacles() {
		return obstacleStatistics.keySet();
	}

	public MobileObstacleStatistics obstacleStatistics(int obstacleId) {
		return obstacleStatistics.get(obstacleId);
	}

	public Collision collision(int obstacleId) {
		return collisionEvaluations.get(obstacleId).collision();
	}

	public CollisionEvaluation collisionEvaluation(int obstacleId) {
		return collisionEvaluations.get(obstacleId);
	}

	//TODO rename to appendObstacleStatistics?
	public void addObstacleStatistics(int objectId, Point position, double direction, double speed) {
		if (!isKnownObstacle(objectId)) {
			this.obstacleStatistics.put(objectId, new MovementStatistics(objectId));
			this.collisionEvaluations.put(objectId, null);
		}
		this.obstacleStatistics.get(objectId).addEntry(position, speed, direction);
	}

	public void putCollisionEvaluation(CollisionEvaluation collisionEvaluation) {
		this.collisionEvaluations.put(collisionEvaluation.obstacleId(), collisionEvaluation);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<MobileObstacleStatistics> allObstacleStatistics() {
		return (Collection)this.obstacleStatistics.values();
	}

	public Collection<CollisionEvaluation> allColisionEvaluations() {
		return this.collisionEvaluations.values();
	}

	public void removeObstacle(int objectId) {
		if (isKnownObstacle(objectId)) {
			this.obstacleStatistics.remove(objectId);
			this.collisionEvaluations.remove(objectId);
		}
	}

	public void removeObstacles(List<Integer> obstacleIds) {
		obstacleStatistics.keySet().removeAll(obstacleIds);
		collisionEvaluations.keySet().removeAll(obstacleIds);
	}

	public void retainObstacles(List<Integer> obstacleIds) {
		obstacleStatistics.keySet().retainAll(obstacleIds);
		collisionEvaluations.keySet().retainAll(obstacleIds);
	}



}
