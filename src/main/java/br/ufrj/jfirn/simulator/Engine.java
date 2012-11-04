package br.ufrj.jfirn.simulator;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.intelligent.sensors.Eye;
import br.ufrj.jfirn.intelligent.sensors.PositioningSystem;
import br.ufrj.jfirn.intelligent.sensors.SightData;
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
	private final Set<PositioningSystem> gpss = new HashSet<>();
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

	public void addRobot(Robot robot) {
		this.robots.add(robot);
	}

	public void addIntelligentRobot(Robot robot, Eye eye, PositioningSystem gps) {
		this.robots.add(robot);
		this.eyes.add(eye);
		this.gpss.add(gps);
	}

	private void init() {
		//nothing to do yet...
	}

	private void sense() {
		//this will send a GPS signal to all robots that have an embedded GPS
		for (PositioningSystem gps : gpss) {
			Robot r = (Robot)gps;
			gps.onPositioningData(r.position(), r.direction(), r.speed());
		}

		//TODO sensing robots should both be solved efficiently. Maybe we should consider implementing a quadtree?
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

}
