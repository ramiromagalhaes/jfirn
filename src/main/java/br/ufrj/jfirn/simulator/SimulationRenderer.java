package br.ufrj.jfirn.simulator;

import br.ufrj.jfirn.common.PointParticle;

/**
 * Interface that abstracts the method used to effectively draw the simulation somewhere.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public interface SimulationRenderer {

	public void draw(PointParticle particle);

}
