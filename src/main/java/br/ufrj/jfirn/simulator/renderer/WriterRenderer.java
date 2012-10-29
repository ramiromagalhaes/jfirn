package br.ufrj.jfirn.simulator.renderer;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;

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
	private Map<Robot, List<RobotData>> robotData = new HashMap<>();

	public WriterRenderer(Writer w) {
		this.writer = w;
	}

	@Override
	public void draw(Robot robot) {
		if ( !robotData.containsKey(robot) ) {
			List<RobotData> newList = new ArrayList<>(currentTick);
			robotData.put(robot, newList);
		}

		robotData.get(robot).add(currentTick,
			new RobotData(
				robot.position(),
				robot.directionDegrees(),
				robot.speed()
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
			final Set<Robot> robots = robotData.keySet();

			for (int i = 0; i < currentTick; i++) {
				for (Robot robot : robots) {
					final RobotData d = robotData.get( robot ).get(i);

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

	private static final class RobotData {
		public final Point position;
		public final double direction;
		public final double speed;

		private static final DecimalFormat fmt;
		static {
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
			otherSymbols.setDecimalSeparator(',');
			fmt = new DecimalFormat("###.###", otherSymbols);
		}

		public RobotData(Point position, double direction, double speed) {
			this.position = position;
			this.direction = direction;
			this.speed = speed;
		}

		public void writeData(Writer w) throws IOException {
			w.write( fmt.format(position.x()) );
			w.append('\t');
			w.write( fmt.format(position.y()) );
			w.append('\t');
			w.write( fmt.format(direction) );
			w.append('\t');
			w.write( fmt.format(speed) );
			w.append('\t');
		}
	}

}
