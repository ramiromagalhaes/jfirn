package br.ufrj.jfirn.intelligent;

import br.ufrj.jfirn.common.PointParticle;


/**
 * An interface that allows its implementor to see things in the simulation.
 * More specifically, it allows its implementor to see other {@link PointParticle}s.
 */
public interface Sight {

	public void onSight(SightEvent e);

}
