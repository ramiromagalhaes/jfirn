package br.ufrj.jfirn.intelligent;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufrj.jfirn.common.BasicRobot;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.intelligent.evaluation.DumbEvaluator;
import br.ufrj.jfirn.intelligent.evaluation.Evaluator;

public class IntelligentAgent extends BasicRobot implements Sight {

	private static final double DEFAULT_MAX_SPEED = 5;
	private static final Logger logger = LoggerFactory.getLogger(IntelligentAgent.class);

	/**
	 * True when someone is way too close to me and I should fear for my safety.
	 */
	private boolean endangered;

	/**
	 * Stored data about other particles and obstacles.
	 */
	private Map<Robot, MovementStatistics> aboutObstacles = new HashMap<>();

	/**
	 * The target points in the simulation area to where I should move.
	 */
	private Deque<Point> targets = new ArrayDeque<>();

	/**
	 * Decides what this particle should do in terms of speed and direction.
	 */
	private Evaluator evaluator = new DumbEvaluator();



	public IntelligentAgent(Point... targets) {
		super();
		this.targets.addAll(
			Arrays.asList(targets)
		);
	}

	public IntelligentAgent(Evaluator evaluator, Point... targets) {
		super();
		this.evaluator = evaluator;
		this.targets.addAll(
			Arrays.asList(targets)
		);
	}

	public IntelligentAgent(double x, double y, double direction, double speed, Point... targets) {
		super(x, y, direction, speed);
		this.targets.addAll(
			Arrays.asList(targets)
		);
	}

	public IntelligentAgent(double x, double y, double direction, double speed, Evaluator evaluator, Point... targets) {
		this(x, y, direction, speed, targets);
		this.evaluator = evaluator;
	}

	/**
	 * Handles {@link SightEvent}s. This method should do the minimum
	 * necessary work here. Movement decision logic should be put
	 * inside the {@link #move()} method.
	 */
	@Override
	public void onSight(SightEvent e) {
		if (logger.isDebugEnabled()) {
			logger.debug(this + " sees: " + e.getParticlesSighted());
		}

		//Someone I see is endangering me?
		this.endangered = false; //assume not...
		for (Robot p : e.getParticlesSighted()) { //...but check for it
			if ( this.isInDangerRadius(p) ) {
				this.endangered = true;
				if (logger.isDebugEnabled()) {
					logger.debug(this + " is endangered by " + p);
				}
				break;
			}
		}

		//I'll only keep statistics about objects I see
		aboutObstacles.keySet().retainAll(e.getParticlesSighted());

		for (Robot p : e.getParticlesSighted()) {
			if ( !aboutObstacles.containsKey(p) ) { //store data about new objects I see
				aboutObstacles.put(p, new MovementStatistics(p.hashCode()));
			}

			aboutObstacles.get(p).addEntry(p.position(), p.speed(), p.direction());
		}
	}

	/**
	 * Moves the robot, but thinks about what move it should be.
	 * 
	 * The following steps are executed:
	 * <ol>
	 * <li>Checks if this particle is way too close to someone. If it is, stop.</li>
	 * <li>Checks if this particle has anywhere to go. If it doesn't, stop.</li>
	 * <li>Checks if this particle arrived somewhere it was supposed to go. Gets the next place.</li>
	 * <li>Thinks about how it is going to get there and sets the course via an implementation of {@link Evaluator}.</li>
	 * </ol>
	 */
	@Override
	public void move() {
		//First things first: am I in danger?
		if (endangered) {
			this.speed(STOPPED);
		} else {
			this.speed(DEFAULT_MAX_SPEED);
		}

		//Do I have any additional targets to go after?
		if (targets.isEmpty()) {
			this.speed(STOPPED);
			//TODO Should I fire some event to the simulation saying I'm done?
			return;
		}

		final Point currentTarget = targets.peek(); //This is where should I move to now.

		//Did I arrive somewhere I wanted to?
		if (this.isInReachRadius(currentTarget)) {
			logger.debug("Arrived at target " + currentTarget + ". My current position is " + this.position() + ".");
			targets.pop();
		}

		//the evaluator thinks about the robot current situation and sets its direction and speed
		this.evaluator.evaluate(this, aboutObstacles.values(), targets);

		super.move();
	}

	/**
	 * Used to verify if the particle is in danger.
	 */
	private final static double DANGER_RADIUS = 10;
	private boolean isInDangerRadius(Robot p) {
		return this.position().distanceTo(p.position()) <= DANGER_RADIUS;
	}

	/**
	 * Used to verify if I'm close enough to a target.
	 */
	private final static double REACH_RADIUS = 10;
	private boolean isInReachRadius(Point p) {
		return this.position().distanceTo(p) <= REACH_RADIUS;
	}

}
