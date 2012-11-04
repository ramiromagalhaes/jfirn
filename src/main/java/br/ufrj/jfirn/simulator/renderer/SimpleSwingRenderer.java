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
public class SimpleSwingRenderer implements SimulationRenderer, ChangeListener {

	private static final int AREA_WIDTH = 1024;
	private static final int AREA_HEIGHT = 768;

	private final JFrame frame;
	private final JSlider tickSelector;
	private List<List<RobotData>> robotData = new ArrayList<>(0);
	private int currentTick;
	private int tickToDisplay = 0;

	public SimpleSwingRenderer() {
		tickSelector = new JSlider(JSlider.HORIZONTAL);
		tickSelector.setMinimum(0);
		tickSelector.setPaintTicks(false);
		tickSelector.setPaintLabels(false);
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
			robotData.add(new ArrayList<RobotData>());
			tickSelector.setMaximum(currentTick);
		}

		robotData.get(currentTick).add(
			new RobotData(robot.position(), robot.direction(), robot.hashCode())
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

	private static class RobotData {
		public final Point position;
		public final double direction;
		public final int hashCode;

		public RobotData(final Point position, final double direction, final int hashCode) {
			this.position = new Point(position.x(), AREA_HEIGHT - position.y());
			this.direction = direction;
			this.hashCode = hashCode;
		}
	}

	private class ThePane extends JPanel {
		private static final long serialVersionUID = 1L;
		private final Dimension preferredSize = new Dimension(AREA_WIDTH, AREA_HEIGHT);

		public ThePane() {
		}

		@Override @Transient
		public Dimension getPreferredSize() {
			return preferredSize;
		}

		public void paintComponent(Graphics componentGraphics) {
			super.paintComponent(componentGraphics);

			//Draws the selected tickSelector situation
			final Image image = new BufferedImage(AREA_WIDTH, AREA_HEIGHT, BufferedImage.TYPE_INT_BGR);
			final Graphics g = image.getGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, AREA_WIDTH, AREA_HEIGHT);

			for (RobotData data : robotData.get(tickToDisplay)) {
				final Triangle t = new Triangle(data.position, data.direction);
				g.setColor(ColorPaleteForRenderers.getColor(data.hashCode));
				g.fillPolygon(t.x, t.y, t.n);
				//g.drawOval((int)data.position.x() - 5, (int)data.position.y() - 5, 10, 10);
			}

			componentGraphics.drawImage(image, 0, 0, AREA_WIDTH, AREA_HEIGHT, Color.WHITE, null);
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
