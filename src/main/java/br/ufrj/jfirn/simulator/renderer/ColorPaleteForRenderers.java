package br.ufrj.jfirn.simulator.renderer;

import java.awt.Color;

final class ColorPaleteForRenderers {

	private static final Color[] colorArray = {
		Color.orange, Color.blue,
		Color.red, Color.green,
		Color.darkGray, Color.magenta,
		new Color(0x9c00ff), new Color(0x23707e)
	};

	public static final Color getColor(int id) {
		return colorArray[id % colorArray.length];
	}

}
