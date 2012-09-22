package br.ufrj.jfirn.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.Transient;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.ufrj.jfirn.common.PointParticle;

public class SimpleSwingRenderer implements SimulationRenderer {

	private final JFrame frame;
	private final Image image = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_BGR);

	public SimpleSwingRenderer() {
		frame = new JFrame("Simulator");

        frame.add(new ThePane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void draw(PointParticle particle) {
		final Graphics g = image.getGraphics();

		//TODO find a nice way to set colors.
		switch (particle.hashCode()) { //crazy way to set color
			case 1:
				g.setColor(Color.blue);
				break;
			case 2:
				g.setColor(Color.red);
				break;
			case 3:
				g.setColor(Color.green);
				break;
			case 4:
				g.setColor(Color.yellow);
				break;
		}

		g.fillRect((int)particle.x(), (int)particle.y(), 3, 3);
		frame.repaint();
	}

	private class ThePane extends JPanel {
		private static final long serialVersionUID = 1L;
		private final Dimension preferredSize = new Dimension(1024, 768);

		public ThePane() {
		}

		@Override @Transient
		public Dimension getPreferredSize() {
			return preferredSize;
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(SimpleSwingRenderer.this.image, 0, 0, 1024, 768, Color.WHITE, null);
		}
	}

}
