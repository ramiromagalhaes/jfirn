package br.ufrj.jfirn.simulator.renderer;

import br.ufrj.jfirn.common.Robot;

/**
 * Interface that abstracts the method used to effectively draw the simulation somewhere.
 *  
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public interface SimulationRenderer {

	public void draw(Robot robot);

	/**
	 * Advances the time tick. Works with simulation renderers that knows how to handle time.
	 */
	public void nextTick();

	/**
	 * Call it when you have finished adding data to the renderer.
	 */
	public void done();

}
