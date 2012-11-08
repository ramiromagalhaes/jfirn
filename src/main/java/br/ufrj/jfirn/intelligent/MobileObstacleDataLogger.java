package br.ufrj.jfirn.intelligent;

import br.ufrj.jfirn.common.Point;

public interface MobileObstacleDataLogger {

	public void addEntry(Point position, double speed, double direction);

	public void clear();

}
