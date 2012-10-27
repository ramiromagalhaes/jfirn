package br.ufrj.jfirn.simulator;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufrj.jfirn.common.BasicRobot;
import br.ufrj.jfirn.common.CrazyRobot;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.common.RandomWalkerRobot;
import br.ufrj.jfirn.common.SineRobot;
import br.ufrj.jfirn.common.SquareRobot;
import br.ufrj.jfirn.intelligent.Eye;
import br.ufrj.jfirn.intelligent.IntelligentAgent;
import br.ufrj.jfirn.intelligent.SightEvent;


/**
 * Engine to run online simulations.
 * 
 */
public class Engine {

	private static final Logger logger = LoggerFactory.getLogger(Engine.class);

	private final int iterations = 200;
	private final Set<Robot> particles = new HashSet<>();
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
			final Set<Robot> seenParticles = new HashSet<>();
			for(Robot p : particles) {
				if ( eye.sees(p) ) {
					seenParticles.add(p);
				}
			}
			if (!seenParticles.isEmpty()) {
				eye.onSight(new SightEvent() {
					@Override
					public Set<Robot> getParticlesSighted() {
						return seenParticles;
					}
				});
			}
		}
	}

	private void move() {
		for (Robot particle : this.particles) {
			particle.move();
		}
	}

	/**
	 * Render stuff (if a renderer exists).
	 */
	private void render() {
		if (!renderers.isEmpty()) {
			for (Robot particle : this.particles) {
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

		IntelligentAgent p = new IntelligentAgent (200, 200, 0, 5,
			new Point(400, 100), new Point(200, 400), new Point(300, 450), new Point(200, 200) //targets
		);
		e.eyes.add(new Eye(200, p));
		e.particles.add( p );

		e.particles.add( new BasicRobot       (185, 175, 0, 5) );
		e.particles.add( new RandomWalkerRobot(300, 300, 0, 5) );
		e.particles.add( new SineRobot        (535, 100, Robot.UP, 5) );
		e.particles.add( new CrazyRobot       (250, 375, 0, 5) );
		e.particles.add( new SquareRobot      (300, 425, Robot.UP, 5) );

		e.simulate();
		logger.debug(p.toString());
	}
}
