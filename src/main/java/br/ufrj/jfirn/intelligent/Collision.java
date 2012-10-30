package br.ufrj.jfirn.intelligent;

import br.ufrj.jfirn.common.Point;

public class Collision {
	public final int withObjectId;
	public final Point position;
	public final double time;
	public double probability;

	//TODO how I'm defining the intersection area? A circle? The effective collision area?

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