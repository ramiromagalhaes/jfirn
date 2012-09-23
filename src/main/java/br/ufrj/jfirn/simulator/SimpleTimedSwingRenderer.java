package br.ufrj.jfirn.simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.PointParticle;

/**
 * Very simple implementation of a {@link TimedSimulationRenderer}.
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class SimpleTimedSwingRenderer implements TimedSimulationRenderer, ChangeListener {

	private final JFrame frame;
	private final JSlider tickSelector;
	private List<List<ParticleData>> particleData = new ArrayList<>(0);
	private int currentTick;
	private int tickToDisplay = 0;

	public SimpleTimedSwingRenderer() {
		tickSelector = new JSlider(JSlider.HORIZONTAL);
		tickSelector.setMinimum(0);
		tickSelector.setMajorTickSpacing(50);
		tickSelector.setMinorTickSpacing(10);
		tickSelector.setPaintTicks(true);
		tickSelector.setPaintLabels(true);
		tickSelector.addChangeListener(this);

		frame = new JFrame("Simulator");
        frame.setLayout(new BorderLayout());

        frame.add(new ThePane(), BorderLayout.NORTH);
        frame.add(tickSelector, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
	}

	@Override
	public void draw(PointParticle particle) {
		if (currentTick >= particleData.size()) {
			particleData.add(new ArrayList<ParticleData>());
			tickSelector.setMaximum(currentTick);
		}

		particleData.get(currentTick).add(
			new ParticleData(particle.position(), particle.hashCode())
		);

	}

	@Override
	public void nextTick() {
		this.currentTick++;
	}

	@Override
	public void done() {
		frame.setVisible(true);
	}

	private static class ParticleData {
		public final Point position;
		public final int hashCode;

		public ParticleData(final Point position, final int hashCode) {
			this.position = position;
			this.hashCode = hashCode;
		}
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

		public void paintComponent(Graphics componentGraphics) {
			super.paintComponent(componentGraphics);

			//Draws the selected tickSelector situation
			final Image image = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_BGR);
			final Graphics g = image.getGraphics();

			for (ParticleData data : particleData.get(tickToDisplay)) {
				//TODO find a nice way to set colors.
				switch (data.hashCode) { //crazy way to set color
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
					case 5:
						g.setColor(Color.cyan);
						break;
				}

				g.fillRect((int)data.position.x(), (int)data.position.y(), 3, 3);
			}

			componentGraphics.drawImage(image, 0, 0, 1024, 768, Color.WHITE, null);
		}
	}

	/**
	 * Updates what is shown based on the current tick.
	 */
	@Override
	public void stateChanged(ChangeEvent evt) {
		this.tickToDisplay = ((JSlider)evt.getSource()).getValue();
		frame.repaint();
	}


}
