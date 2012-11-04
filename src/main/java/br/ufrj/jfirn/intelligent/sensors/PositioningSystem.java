package br.ufrj.jfirn.intelligent.sensors;

import br.ufrj.jfirn.common.Point;

public interface PositioningSystem {

	public void onPositioningData(Point position, double direction, double speed);

}
