package br.ufrj.jfirn.intelligent;

import br.ufrj.jfirn.common.Point;

public class Collision {
	//TODO still need to define the collision area and its probability
	public final int withObjectId;
	public final Point position;
	public final double time;

	public Collision(int withObjectId, Point position, double time) {
		this.withObjectId = withObjectId;
		this.position = position;
		this.time = time;
	}

	public String toString() {
		return new StringBuilder()
			.append("Collision with ")
			.append(withObjectId)
			.append(" at ")
			.append(position)
			.append(" in ")
			.append(time)
			.append(" time units.")
			.toString();
	}
}