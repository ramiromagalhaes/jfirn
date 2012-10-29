package br.ufrj.jfirn.simulator;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufrj.jfirn.common.BasicRobot;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.intelligent.IntelligentRobot;
import br.ufrj.jfirn.intelligent.sensors.Eye;
import br.ufrj.jfirn.intelligent.sensors.RobotData;
import br.ufrj.jfirn.intelligent.sensors.SightEvent;
import br.ufrj.jfirn.mobileObstacle.CrazyRobot;
import br.ufrj.jfirn.mobileObstacle.RandomWalkerRobot;
import br.ufrj.jfirn.mobileObstacle.SineRobot;
import br.ufrj.jfirn.mobileObstacle.SquareRobot;
import br.ufrj.jfirn.simulator.renderer.SimpleSwingRenderer;
import br.ufrj.jfirn.simulator.renderer.SimpleTimedSwingRenderer;
import br.ufrj.jfirn.simulator.renderer.SimulationRenderer;
import br.ufrj.jfirn.simulator.renderer.WriterRenderer;


/**
 * Engine to run online simulations.
 * 
 */
public class Engine {

	private static final Logger logger = LoggerFactory.getLogger(Engine.class);

	private final int iterations = 200;
	private final Set<Robot> robots = new HashSet<>();
	private final Set<Eye> eyes = new HashSet<>();
	private final List<SimulationRenderer> renderers = new ArrayList<>();

	public Engine() {
		this.renderers.add( new SimpleSwingRenderer() );
		this.renderers.add( new SimpleTimedSwingRenderer() );
		this.renderers.add( new WriterRenderer(new OutputStreamWriter(System.out)) );
	}

	public Engine(SimulationRenderer renderer) {
		this.renderers.add( renderer );
	}

	public void simulate() {
		init();

		for (int i = 0; i < this.iterations; i++) {
			sense();
			move();
			render();
		}

		for (SimulationRenderer renderer : renderers) {
			renderer.done();
		}
	}

	private void init() {
		//nothing to do yet...
	}

	private void sense() {
		//NOTE: sensing and particle collision should both be solved efficiently with a collision detection algorithm
		for(Eye eye : eyes) {
			final Set<RobotData> seenRobots = new HashSet<>();
			for(Robot p : robots) {
				if ( eye.sees(p) ) {
					seenRobots.add(
						new RobotData(p.hashCode(), p.position(), p.speed(), p.direction())
					);
				}
			}
			if (!seenRobots.isEmpty()) {
				eye.onSight(new SightEvent() {
					@Override
					public Set<RobotData> getMobileObstaclesSighted() {
						return seenRobots;
					}
				});
			}
		}
	}

	private void move() {
		for (Robot particle : this.robots) {
			particle.move();
		}
	}

	/**
	 * Render stuff (if a renderer exists).
	 */
	private void render() {
		if (!renderers.isEmpty()) {
			for (Robot particle : this.robots) {
				for (SimulationRenderer renderer : renderers) {
					renderer.draw(particle);
				}
			}
			for (SimulationRenderer renderer : renderers) {
				renderer.nextTick();
			}
		}
	}

	public static void main(String[] args) {
		final Engine e = new Engine();

		IntelligentRobot p = new IntelligentRobot (200, 200, 0, 5,
			new Point(400, 100), new Point(200, 400), new Point(300, 450), new Point(200, 200) //targets
		);
		e.eyes.add(new Eye(200, p));
		e.robots.add( p );

		e.robots.add( new BasicRobot       (185, 175, 0, 5) );
		e.robots.add( new RandomWalkerRobot(300, 300, 0, 5) );
		e.robots.add( new SineRobot        (535, 100, Robot.UP, 5) );
		e.robots.add( new CrazyRobot       (250, 375, 0, 5) );
		e.robots.add( new SquareRobot      (300, 425, Robot.UP, 5) );

		e.simulate();
		logger.debug(p.toString());
	}
}
