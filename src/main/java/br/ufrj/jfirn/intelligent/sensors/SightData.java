package br.ufrj.jfirn.intelligent.sensors;

import java.io.Serializable;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.intelligent.IntelligentRobot;

/**
 * Data collected by the {@link IntelligentRobot}'s {@link Eye} for future processing.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class SightData implements Serializable {

	private static final long serialVersionUID = 1L;

	public final int id;
	public final Point position;
	public final double speed;
	public final double direction;

	public SightData(int id, Point position, double speed, double direction) {
		this.id = id;
		this.position = position;
		this.speed = speed;
		this.direction = direction;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		return s.append("Robot ")
				.append(id)
				.append(" on ")
				.append(position)
				.append(" with ")
				.append(speed)
				.append(" going ")
				.append(direction)
				.append('.')
				.toString();
	}

}
