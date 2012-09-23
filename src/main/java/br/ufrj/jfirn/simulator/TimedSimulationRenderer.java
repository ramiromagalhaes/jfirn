package br.ufrj.jfirn.simulator;

/**
 * Interface that abstracts the method used to effectively draw the simulation somewhere.
 * You should use this simulation if you want somehow to control the visualization of the
 * simulation over time.
 *  
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public interface TimedSimulationRenderer extends SimulationRenderer {

	/**
	 * Advances the time tick.
	 * 
	 * Works with simulation renderers that knows how to handle time.
	 */
	public void nextTick();

	/**
	 * Call it when you have finished adding data to the renderer.
	 */
	public void done();

}
