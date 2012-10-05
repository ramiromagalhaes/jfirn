package br.ufrj.jfirn.simulator;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.PointParticle;

/**
 * Logs objects to a Writer.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class WriterRenderer implements SimulationRenderer {
	
	private final Writer writer;

	//TODO improve this class's algorithm and data structure

	private int currentTick = 0;
	private Map<PointParticle, List<ParticleData>> particleData = new HashMap<>();

	public WriterRenderer(Writer w) {
		this.writer = w;
	}

	@Override
	public void draw(PointParticle particle) {
		if ( !particleData.containsKey(particle) ) {
			List<ParticleData> newList = new ArrayList<>(currentTick);
			particleData.put(particle, newList);
		}

		particleData.get(particle).add(currentTick,
			new ParticleData(
				particle.position(),
				particle.directionDegrees(),
				particle.speed()
			)
		);
	}

	@Override
	public void nextTick() {
		currentTick++;
	}

	@Override
	public void done() {
		try {
			final Set<PointParticle> particles = particleData.keySet();

			for (int i = 0; i < currentTick; i++) {
				for (PointParticle particle : particles) {
					final ParticleData d = particleData.get( particle ).get(i);

					d.writeData(writer);
				}
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static final class ParticleData {
		public final Point position;
		public final double direction;
		public final double speed;

		public ParticleData(Point position, double direction, double speed) {
			this.position = position;
			this.direction = direction;
			this.speed = speed;
		}

		public void writeData(Writer w) throws IOException {
			w.write( Double.toString(position.x()) );
			w.append('\t');
			w.write( Double.toString(position.y()) );
			w.append('\t');
			w.write( Double.toString(direction) );
			w.append('\t');
			w.write( Double.toString(speed) );
			w.append('\t');
		}
	}

}
