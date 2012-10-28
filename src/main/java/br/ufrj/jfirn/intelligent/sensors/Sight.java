package br.ufrj.jfirn.intelligent.sensors;

import br.ufrj.jfirn.common.Robot;


/**
 * An interface that allows its implementor to see things in the simulation.
 * More specifically, it allows its implementor to see other {@link Robot}s.
 */
public interface Sight {

	public void onSight(SightEvent e);

}
