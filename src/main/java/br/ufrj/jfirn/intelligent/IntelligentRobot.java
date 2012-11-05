package br.ufrj.jfirn.intelligent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.intelligent.evaluation.ChainOfEvaluationsImplementation;
import br.ufrj.jfirn.intelligent.evaluation.Instruction;
import br.ufrj.jfirn.intelligent.evaluation.ThoughtProcesor;
import br.ufrj.jfirn.intelligent.sensors.Eye;
import br.ufrj.jfirn.intelligent.sensors.PositioningSystem;
import br.ufrj.jfirn.intelligent.sensors.Sight;
import br.ufrj.jfirn.intelligent.sensors.SightData;

public class IntelligentRobot extends AbstractIntelligentRobot implements Sight, PositioningSystem {

	private static final long serialVersionUID = 1L;

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

	@Override
	protected double maximumSpeed() {
		return 5d;
	}

	/**
	 * Makes the robot think about what it should do, then move.
	 * @see Thoughts
	 * @see ThoughtProcesor
	 * @see Instruction
	 */
	@Override
	public void move() {
		//first the evaluator thinks about the robot current situation and sets its direction and speed
		this.evaluator.evaluate(thoughts).apply(this);
		
		super.move();
	}

	public Thoughts getThoughts() {
		return thoughts;
	}

	/**
	 * Handles {@link SightEvent}s. This method simply stores the data
	 * collected from this robot's {@link Eye}.
	 */
	@Override
	public void onSight(Set<SightData> sighted) {
		//remove data about objects I can't see anymore
		List<Integer> ids = new LinkedList<>();
		for (SightData data : sighted) {
			ids.add(data.id);
		}

		final Map<Integer, MobileObstacleStatisticsLogger> knownObstacles =
				thoughts.knownObstacles();

		knownObstacles.keySet().retainAll(ids);

		//store data about new objects I see
		for (SightData data : sighted) {
			if ( !knownObstacles.containsKey(data.id) ) {
				knownObstacles.put(data.id, new MovementStatistics(data.id));
			}

			knownObstacles.get(data.id).addEntry(data.position, data.speed, data.direction);
		}
	}

	@Override
	public void onPositioningData(Point position, double direction, double speed) {
		thoughts.myPosition(position);
		thoughts.myDirection(direction);
		thoughts.mySpeed(speed);
	}

}
