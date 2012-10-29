package br.ufrj.jfirn.simulator.renderer;

import java.awt.Color;

final class ColorPaleteForRenderers {

	//TODO we can still improve the color selection for the renderers

	private static final Color[] colorArray = {
		Color.orange, Color.blue,
		Color.red, Color.green,
		Color.yellow, Color.cyan,
		Color.magenta, Color.pink
	};

	public static final Color getColor(int id) {
		return colorArray[id % colorArray.length];
	}

}
