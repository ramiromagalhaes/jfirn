package br.ufrj.jfirn.simulator.renderer;

import java.awt.Color;

final class ColorPaleteForRobots {

	private static final Color[] colorArray = {
		Color.orange, Color.blue,
		Color.red, Color.green,
		Color.darkGray, Color.magenta,
		new Color(0x9c00ff), new Color(0x23707e)
	};

	private static final Color[] lighterColorArray = {
		new Color(0xffc880), new Color(0xa9b3ff),
		new Color(0xff8989), new Color(0x80ff8c),
		Color.lightGray, new Color(0xf998ff),
		new Color(0xd085ff), new Color(0x23707e)
	};

	public static final Color getColor(int id) {
		return colorArray[id % colorArray.length];
	}

	public static final Color getLighter(int id) {
		return lighterColorArray[id % lighterColorArray.length];
	}

}
