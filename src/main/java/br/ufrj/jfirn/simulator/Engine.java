package br.ufrj.jfirn.simulator;

import java.util.HashSet;
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
	private final SimulationRenderer renderer;

	public Engine() {
		this.renderer = new SimpleTimedSwingRenderer();
	}

	public Engine(SimulationRenderer renderer) {
		this.renderer = renderer;
	}

	public void simulate() {
		init();

		for (int i = 0; i < this.iterations; i++) {
			sense();
			move();
			render();
		}

		renderer.done();
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
		if (renderer != null) {
			for (PointParticle particle : this.particles) {
				this.renderer.draw(particle);
			}
			this.renderer.nextTick();
		}
	}

	//TODO this main is for prototyping purposes. Lots of bad code here.
	public static void main(String[] args) {
		final Engine e = new Engine(new SimpleSwingRenderer());

		IntelligentParticle p = new IntelligentParticle (200, 200, 0, 5,
			new Point(400, 100), new Point(200, 400), new Point(300, 450), new Point(200, 200)
		);
		e.eyes.add(new Eye(200, p));
		e.particles.add( p );

		e.particles.add( new BasicParticle       (100, 200, 0, 5) );
		e.particles.add( new RandomWalkerParticle(300, 300, 0, 5) );
		e.particles.add( new SineParticle        (250, 100, 0, 5) );
		e.particles.add( new CrazyParticle       (400, 400, 0, 5) );
		e.particles.add( new SquareParticle      (500, 250, 1, 5) );

		e.simulate();
		logger.debug(p.toString());
	}
}
