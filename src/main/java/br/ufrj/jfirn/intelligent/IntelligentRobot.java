package br.ufrj.jfirn.intelligent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.ufrj.jfirn.common.BasicRobot;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.intelligent.evaluation.ChainOfEvaluationsImplementation;
import br.ufrj.jfirn.intelligent.evaluation.ThoughtProcesor;
import br.ufrj.jfirn.intelligent.evaluation.Evaluator;
import br.ufrj.jfirn.intelligent.sensors.RobotData;
import br.ufrj.jfirn.intelligent.sensors.Sight;
import br.ufrj.jfirn.intelligent.sensors.SightEvent;

public class IntelligentRobot extends BasicRobot implements Sight {

	private static final double DEFAULT_MAX_SPEED = 5;

	/**
	 * Holds an instance of the class that defines the thought process
	 * of this IntelligentRobot.
	 */
	private ThoughtProcesor evaluator = new ChainOfEvaluationsImplementation();

	/**
	 * What this robot is thinking.
	 */
	private Thoughts thoughts = new Thoughts();


	public IntelligentRobot(Point... targets) {
		super();
		this.thoughts.targets().addAll(
			Arrays.asList(targets)
		);
	}

	public IntelligentRobot(double x, double y, double direction, double speed, Point... targets) {
		super(x, y, direction, speed);
		this.thoughts.targets().addAll(
			Arrays.asList(targets)
		);
	}

	@Override
	public BasicRobot speed(double speed) {
		if (speed > DEFAULT_MAX_SPEED) {
			speed = DEFAULT_MAX_SPEED;
		}
		return super.speed(speed);
	}

	/**
	 * Handles {@link SightEvent}s. This method should do the minimum
	 * necessary work here. Movement decision logic should be put
	 * inside the {@link #move()} method.
	 */
	@Override
	public void onSight(SightEvent e) {
		final Map<Integer, MobileObstacleStatisticsLogger> knownObstacles =
			thoughts.knownObstacles();

		//remove data about objects I can't see anymore
		List<Integer> ids = new LinkedList<>();
		for (RobotData data : e.getMobileObstaclesSighted()) {
			ids.add(data.id);
		}
		knownObstacles.keySet().retainAll(ids);

		//store data about new objects I see
		for (RobotData data : e.getMobileObstaclesSighted()) {
			if ( !knownObstacles.containsKey(data.id) ) {
				knownObstacles.put(data.id, new MovementStatistics(data.id));
			}

			knownObstacles.get(data.id).addEntry(data.position, data.speed, data.direction);
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
		//first we update our know position.
		thoughts.myPosition(this.position());
		thoughts.myDirection(this.direction());
		thoughts.mySpeed(this.speed());

		//then the evaluator thinks about the robot current situation and sets its direction and speed
		this.evaluator.evaluate(thoughts).apply(this);
		
		super.move();
	}

}
