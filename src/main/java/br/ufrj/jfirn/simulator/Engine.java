package br.ufrj.jfirn.simulator;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufrj.jfirn.common.BasicParticle;
import br.ufrj.jfirn.common.CrazyParticle;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.PointParticle;
import br.ufrj.jfirn.common.RandomWalkerParticle;
import br.ufrj.jfirn.common.SineParticle;
import br.ufrj.jfirn.common.SquareParticle;
import br.ufrj.jfirn.intelligent.Eye;
import br.ufrj.jfirn.intelligent.IntelligentParticle;
import br.ufrj.jfirn.intelligent.SightEvent;


/**
 * Engine to run online simulations.
 * 
 */
public class Engine {

	private static final Logger logger = LoggerFactory.getLogger(Engine.class);

	private final int iterations = 200;
	private final Set<PointParticle> particles = new HashSet<>();
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
			final Set<PointParticle> seenParticles = new HashSet<>();
			for(PointParticle p : particles) {
				if ( eye.sees(p) ) {
					seenParticles.add(p);
				}
			}
			if (!seenParticles.isEmpty()) {
				eye.onSight(new SightEvent() {
					@Override
					public Set<PointParticle> getParticlesSighted() {
						return seenParticles;
					}
				});
			}
		}
	}

	private void move() {
		for (PointParticle particle : this.particles) {
			particle.move();
		}
	}

	/**
	 * Render stuff (if a renderer exists).
	 */
	private void render() {
		if (!renderers.isEmpty()) {
			for (PointParticle particle : this.particles) {
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

		IntelligentParticle p = new IntelligentParticle (200, 200, 0, 5,
			new Point(400, 100), new Point(200, 400), new Point(300, 450), new Point(200, 200) //targets
		);
		e.eyes.add(new Eye(200, p));
		e.particles.add( p );

		e.particles.add( new BasicParticle       (185, 175, 0, 5) );
		e.particles.add( new RandomWalkerParticle(300, 300, 0, 5) );
		e.particles.add( new SineParticle        (535, 100, PointParticle.UP, 5) );
		e.particles.add( new CrazyParticle       (250, 375, 0, 5) );
		e.particles.add( new SquareParticle      (300, 425, PointParticle.UP, 5) );

		e.simulate();
		logger.debug(p.toString());
	}
}
