package br.ufrj.jfirn.simulator;

public interface TimedSimulationRenderer extends SimulationRenderer {

	/**
	 * Advances the time tick.
	 * 
	 * Works with simulation renderers that knows how to handle time.
	 */
	public void nextTick();

}
