package br.ufrj.jfirn.intelligent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.intelligent.evaluation.ChainOfEvaluationsImplementation;
import br.ufrj.jfirn.intelligent.evaluation.Instruction;
import br.ufrj.jfirn.intelligent.evaluation.ThoughtProcesor;
import br.ufrj.jfirn.intelligent.sensors.Eye;
import br.ufrj.jfirn.intelligent.sensors.RobotData;
import br.ufrj.jfirn.intelligent.sensors.Sight;
import br.ufrj.jfirn.intelligent.sensors.SightEvent;

public class IntelligentRobot extends AbstractIntelligentRobot implements Sight {

	/**
	 * Holds an instance of the class that defines the thought process
	 * of this IntelligentRobot.
	 */
	private ThoughtProcesor evaluator = new ChainOfEvaluationsImplementation();

	/**
	 * What this robot thinks about itself and the environment.
	 */
	private Thoughts thoughts = new Thoughts();


	public IntelligentRobot(Point... targets) {
		super();
		this.thoughts.targets().addAll(
			Arrays.asList(targets)
		);
	}

	public IntelligentRobot(double x, double y, double direction, Point... targets) {
		super(x, y, direction);
		this.thoughts.targets().addAll(
			Arrays.asList(targets)
		);
	}

	/**
	 * Makes the robot think about what it should do, set its speed and direction, and move.
	 * @see Thoughts
	 * @see ThoughtProcesor
	 * @see Instruction
	 */
	@Override
	public void move() {
		//first we update what we know about ourself.
		thoughts.myPosition(this.position());
		thoughts.myDirection(this.direction());
		thoughts.mySpeed(this.speed());

		//then the evaluator thinks about the robot current situation and sets its direction and speed
		this.evaluator.evaluate(thoughts).apply(this);
		
		super.move();
	}

	/**
	 * Handles {@link SightEvent}s. This method simply stores the data
	 * collected from this robot's {@link Eye}.
	 */
	@Override
	public void onSight(SightEvent e) {
		//remove data about objects I can't see anymore
		List<Integer> ids = new LinkedList<>();
		for (RobotData data : e.getMobileObstaclesSighted()) {
			ids.add(data.id);
		}

		final Map<Integer, MobileObstacleStatisticsLogger> knownObstacles =
				thoughts.knownObstacles();

		knownObstacles.keySet().retainAll(ids);

		//store data about new objects I see
		for (RobotData data : e.getMobileObstaclesSighted()) {
			if ( !knownObstacles.containsKey(data.id) ) {
				knownObstacles.put(data.id, new MovementStatistics(data.id));
			}

			knownObstacles.get(data.id).addEntry(data.position, data.speed, data.direction);
		}
	}

	@Override
	protected double maximumSpeed() {
		return 5d;
	}

}
