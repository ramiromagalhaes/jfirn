package br.ufrj.jfirn.intelligent;

import br.ufrj.jfirn.common.Point;

public interface ParticleDataLogger {

	public void addEntry(Point position, double speed, double direction);

	public int entriesAdded();

	public int getObservedObjectId();

}
