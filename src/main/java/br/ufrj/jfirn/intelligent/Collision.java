package br.ufrj.jfirn.intelligent;

import br.ufrj.jfirn.common.Point;

public class Collision {
	public final int withObjectId;
	public final Point position;
	public final double time;
	public double probability;
	public Point[] area;

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
			.append(" time units with ")
			.append(probability)
			.append(" chance.")
			.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Collision)) {
			return false;
		}

		Collision c = (Collision)o;
		return this.withObjectId == c.withObjectId;
	}

	@Override
	public int hashCode() {
		return withObjectId;
	}

}
