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
	}

	@Override
	public void draw(PointParticle particle) {
		final Graphics g = image.getGraphics();

		g.setColor(ColorPaleteForRenderers.getColor(particle.hashCode()));
		g.fillOval((int)particle.x(), (int)particle.y(), 8, 8);
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

	@Override
	public void nextTick() {
		//intentionally does nothing
	}

	@Override
	public void done() {
		frame.setVisible(true);
	}

}
