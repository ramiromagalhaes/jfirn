package br.ufrj.jfirn.simulator.renderer;

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
import br.ufrj.jfirn.common.Robot;

/**
 * Very simple implementation of a {@link TimedSimulationRenderer}.
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class SimpleTimedSwingRenderer implements SimulationRenderer, ChangeListener {

	private static final int AREA_HEIGHT = 768;

	private final JFrame frame;
	private final JSlider tickSelector;
	private List<List<ParticleData>> robotData = new ArrayList<>(0);
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
	public void draw(Robot robot) {
		if (currentTick >= robotData.size()) {
			robotData.add(new ArrayList<ParticleData>());
			tickSelector.setMaximum(currentTick);
		}

		robotData.get(currentTick).add(
			new ParticleData(robot.position(), robot.hashCode())
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
			this.position = new Point(position.x(), AREA_HEIGHT - position.y());
			this.hashCode = hashCode;
		}
	}

	private class ThePane extends JPanel {
		private static final long serialVersionUID = 1L;
		private final Dimension preferredSize = new Dimension(1024, AREA_HEIGHT);

		public ThePane() {
		}

		@Override @Transient
		public Dimension getPreferredSize() {
			return preferredSize;
		}

		public void paintComponent(Graphics componentGraphics) {
			super.paintComponent(componentGraphics);

			//Draws the selected tickSelector situation
			final Image image = new BufferedImage(1024, AREA_HEIGHT, BufferedImage.TYPE_INT_BGR);
			final Graphics g = image.getGraphics();

			for (ParticleData data : robotData.get(tickToDisplay)) {
				g.setColor(ColorPaleteForRenderers.getColor(data.hashCode));
				g.fillOval((int) data.position.x(), (int) data.position.y(), 8, 8);
			}

			componentGraphics.drawImage(image, 0, 0, 1024, AREA_HEIGHT, Color.WHITE, null);
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
