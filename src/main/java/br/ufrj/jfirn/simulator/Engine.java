package br.ufrj.jfirn.simulator;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufrj.jfirn.common.BasicRobot;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.intelligent.IntelligentRobot;
import br.ufrj.jfirn.intelligent.sensors.Eye;
import br.ufrj.jfirn.intelligent.sensors.SightData;
import br.ufrj.jfirn.mobileObstacle.CrazyRobot;
import br.ufrj.jfirn.mobileObstacle.RandomWalkerRobot;
import br.ufrj.jfirn.mobileObstacle.SineRobot;
import br.ufrj.jfirn.mobileObstacle.SquareRobot;
import br.ufrj.jfirn.simulator.renderer.SimpleTimedSwingRenderer;
import br.ufrj.jfirn.simulator.renderer.SimulationRenderer;
import br.ufrj.jfirn.simulator.renderer.WriterRenderer;


/**
 * Engine to run online simulations.
 * 
 */
public class Engine {

	private final int iterations = 200;
	private final Set<Robot> robots = new HashSet<>();
	private final Set<Eye> eyes = new HashSet<>();
	private final List<SimulationRenderer> renderers = new ArrayList<>();

	public Engine() {
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
		//NOTE: sensing and robot collision should both be solved efficiently with a collision detection algorithm
		for(Eye eye : eyes) {
			final Set<SightData> seenRobots = new HashSet<>();
			for(Robot p : robots) {
				if ( eye.sees(p) ) {
					seenRobots.add(
						new SightData(p.hashCode(), p.position(), p.speed(), p.direction())
					);
				}
			}
			if (!seenRobots.isEmpty()) {
				eye.onSight(seenRobots);
			}
		}
	}

	private void move() {
		for (Robot robot : this.robots) {
			robot.move();
		}
	}

	/**
	 * Render stuff (if a renderer exists).
	 */
	private void render() {
		if (!renderers.isEmpty()) {
			for (Robot robot : this.robots) {
				for (SimulationRenderer renderer : renderers) {
					renderer.draw(robot);
				}
			}
			for (SimulationRenderer renderer : renderers) {
				renderer.nextTick();
			}
		}
	}

	public static void main(String[] args) {
		final Engine e = new Engine();

		{ //TODO need to improve the IntelligentRobot registration process.
			final IntelligentRobot p = new IntelligentRobot (200, 200, 0,
				new Point(400, 100),
				new Point(200, 400),
				new Point(300, 450),
				new Point(200, 200)
			);
			e.eyes.add(new Eye(400, p));
			e.robots.add( p );
		}

		e.robots.add( new BasicRobot       (185, 175, 0, 5) );
		e.robots.add( new RandomWalkerRobot(300, 300, 0, 5) );
		e.robots.add( new SineRobot        (535, 100, Robot.UP, 5) );
		e.robots.add( new CrazyRobot       (250, 375, 0, 5) );
		e.robots.add( new SquareRobot      (300, 425, Robot.UP, 5) );

		e.simulate();
	}
}
