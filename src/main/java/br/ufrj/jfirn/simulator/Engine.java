package br.ufrj.jfirn.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.ufrj.jfirn.common.BasicParticle;
import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.PointParticle;
import br.ufrj.jfirn.common.RandomWalkerParticle;
import br.ufrj.jfirn.common.SineParticle;
import br.ufrj.jfirn.intelligent.Eye;
import br.ufrj.jfirn.intelligent.IntelligentParticle;
import br.ufrj.jfirn.intelligent.SightEvent;


/**
 * Engine to run online simulations.
 *
 */
public class Engine {

	private static final Logger logger = LoggerFactory.getLogger(Engine.class);

	private int iterations = 200;
	private Set<PointParticle> particles = new HashSet<>();
	private Set<Eye> eyes = new HashSet<>();
	private Graphics renderer;

	public void simulate() {
		init();

		for (int i = 0; i < this.iterations; i++) {
			sense();
			move();
			render();
		}
	}

	private void init() {
		//nothing to do yet...
	}

	private void sense() {
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

	//TODO Fix this code written for prototyping purposes
	private void render() {
		for (PointParticle particle : this.particles) {
			switch (particle.hashCode()) {
				case 1:
					renderer.setColor(Color.blue);
					break;
				case 2:
					renderer.setColor(Color.red);
					break;
				case 3:
					renderer.setColor(Color.green);
					break;
				case 4:
					renderer.setColor(Color.black);
					break;
			}
			renderer.fillOval((int)particle.x(), (int)particle.y(), 3, 3);
		}
	}


	//TODO this main is for prototyping purposes. Lots of bad code here.
	public static void main(String[] args) {
		final Engine e = new Engine();

		JFrame frame = new JFrame("Simulator") {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics arg0) {
				e.render();
			}
		};
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(1000, 800));
		frame.setVisible(true);
		e.renderer = frame.getGraphics();


		IntelligentParticle p = new IntelligentParticle (200, 200, 0, 5, new Point(400, 100), new Point(300, 400), new Point(200, 500));
		e.eyes.add(new Eye(200, p));
		e.particles.add( p );
		e.particles.add( new BasicParticle       (200, 200, 0, 5));
		e.particles.add( new RandomWalkerParticle(200, 200, 0, 5));
		e.particles.add( new SineParticle(200, 200, 0, 5));

		e.simulate();
		logger.debug(p.toString());
	}
}
