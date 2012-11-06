package br.ufrj.jfirn.simulator.renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;
import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.intelligent.Collision;
import br.ufrj.jfirn.intelligent.IntelligentRobot;

/**
 * Very simple implementation of a {@link TimedSimulationRenderer}.
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class SimpleSwingRenderer implements SimulationRenderer, ChangeListener {

	private static final int AREA_WIDTH = 1024;
	private static final int AREA_HEIGHT = 768;

	private final JFrame simulationFrame, collisionFrame;
	private final JLabel collisions;
	private final JSlider tickSelector;
	private List<List<RobotPositionData>> robotData = new ArrayList<>(0);
	private int currentTick;
	private int tickToDisplay = 0;

	public SimpleSwingRenderer() {
		simulationFrame = new JFrame("Simulator");
        simulationFrame.setLayout(new BorderLayout());

		tickSelector = new TimeTickSelector();
		tickSelector.addChangeListener(this);

        simulationFrame.add(new MainPane(), BorderLayout.NORTH);
        simulationFrame.add(tickSelector, BorderLayout.SOUTH);

        simulationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        simulationFrame.pack();

        collisionFrame = new JFrame("Collisions");
        collisionFrame.setLayout(new BorderLayout());

        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(AREA_WIDTH, 40));
        collisions = new JLabel();
        collisionFrame.add(p);
        collisionFrame.add(collisions, BorderLayout.NORTH);

        collisionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        collisionFrame.pack();
	}

	@Override
	public void draw(Robot robot) {
		if (currentTick >= robotData.size()) {
			robotData.add(new ArrayList<RobotPositionData>());
			tickSelector.setMaximum(currentTick);
		}

		//TODO find a more elegant way to add multiple robot data for future rendering.
		if (robot instanceof IntelligentRobot) {
			final IntelligentRobot ir = (IntelligentRobot)robot;
			robotData.get(currentTick).add(
				new IntelligentRobotPositionData(
						ir.position(), ir.direction(), ir.hashCode(),
						ir.getThoughts().allColisions().toArray(
								new Collision[ir.getThoughts().allColisions().size()]
						)
				)
			);
		} else {
			robotData.get(currentTick).add(
				new RobotPositionData(robot.position(), robot.direction(), robot.hashCode())
			);
		}

	}

	@Override
	public void nextTick() {
		this.currentTick++;
	}

	@Override
	public void done() {
		simulationFrame.setVisible(true);
		collisionFrame.setVisible(true);
	}

	/**
	 * Updates what is shown based on the current tick.
	 */
	@Override
	public void stateChanged(ChangeEvent evt) {
		this.tickToDisplay = ((JSlider)evt.getSource()).getValue();
		simulationFrame.repaint();
	}

	private static class RobotPositionData {
		public final Point position;
		public final double direction;
		public final int hashCode;

		public RobotPositionData(final Point position, final double direction, final int hashCode) {
			this.position = new Point(position.x(), AREA_HEIGHT - position.y());
			this.direction = direction;
			this.hashCode = hashCode;
		}
	}

	/**
	 * Store relevant data of an IntelligentRobot for future rendering.
	 * 
	 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
	 */
	private static class IntelligentRobotPositionData extends RobotPositionData {
		final public Collision[] collision;

		public IntelligentRobotPositionData(final Point position, final double direction,
				final int hashCode, final Collision[] collisions) {
			super(position, direction, hashCode);
			this.collision = collisions;
		}
	}

	private static class TimeTickSelector extends JSlider {
		private static final long serialVersionUID = 1L;

		public TimeTickSelector() {
			super(JSlider.HORIZONTAL);
			this.setMinimum(0);
			this.setPaintTicks(false);
			this.setPaintLabels(false);
		}
	}



	/**
	 * The {@link JPanel} that draws the robots moving area.
	 * 
	 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
	 *
	 */
	private class MainPane extends JPanel {
		private static final long serialVersionUID = 1L;
		private final Dimension preferredSize = new Dimension(AREA_WIDTH, AREA_HEIGHT);

		public MainPane() {
		}

		@Override @Transient
		public Dimension getPreferredSize() {
			return preferredSize;
		}

		public void paintComponent(final Graphics componentGraphics) {
			super.paintComponent(componentGraphics);

			//Draws the selected tickSelector situation
			final Image image = new BufferedImage(AREA_WIDTH, AREA_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			final Graphics imageGraphics = image.getGraphics();

			//paints the whole background as white
			imageGraphics.setColor(Color.white);
			imageGraphics.fillRect(0, 0, AREA_WIDTH, AREA_HEIGHT);

			//paint the robots
			for (RobotPositionData data : robotData.get(tickToDisplay)) {
				if (data instanceof IntelligentRobotPositionData) {
					final IntelligentRobotPositionData irData = (IntelligentRobotPositionData)data;
					intelligentRobotPainter(irData, imageGraphics);
				} else {
					basicRobotPainter(data, imageGraphics);
				}
			}

			//draw the image in the panel.
			componentGraphics.drawImage(image, 0, 0, AREA_WIDTH, AREA_HEIGHT, Color.WHITE, null);
		}


		private Point[] arrangePointsAsConvexQuadrilateral(Point[] points) {
			//TODO there probably is a better algorithm for this...
			final Point center = new Point(
				(points[0].x() + points[1].x() + points[2].x() + points[3].x()) / 4d,
				(points[0].y() + points[1].y() + points[2].y() + points[3].y()) / 4d
			);

			final Map<Double, Point> map = new HashMap<>(4);
			map.put(center.directionTo(points[0]), points[0]);
			map.put(center.directionTo(points[1]), points[1]);
			map.put(center.directionTo(points[2]), points[2]);
			map.put(center.directionTo(points[3]), points[3]);

			Double[] angles = new Double[4];
			angles = map.keySet().toArray(angles);
			Arrays.sort(angles);

			final LinkedList<Point> toReturn = new LinkedList<>();
			for (Double angle : angles) {
				toReturn.addLast(map.get(angle));
			}

			return toReturn.toArray(new Point[4]);
		}

		//TODO this is not an elegant way to draw the robots. Should probably use the Strategy pattern for that
		private void basicRobotPainter(RobotPositionData data, Graphics imageGraphics) {
			final Triangle t = new Triangle(data.position, data.direction);
			imageGraphics.setColor(ColorPaleteForRobots.getColor(data.hashCode));
			imageGraphics.fillPolygon(t.x, t.y, t.n);
			imageGraphics.drawOval((int)data.position.x() - 5, (int)data.position.y() - 5, 10, 10);
		}

		//TODO this is not an elegant way to draw the robots. Should probably use the Strategy pattern for that
		private void intelligentRobotPainter(IntelligentRobotPositionData irData, Graphics imageGraphics) {
			final StringBuilder message = new StringBuilder();

			for (Collision collision : irData.collision) {
				collision.area = arrangePointsAsConvexQuadrilateral(collision.area);
				final int[] x = new int[collision.area.length];
				final int[] y = new int[collision.area.length];
				for (int i = 0; i < collision.area.length; i++) {
					x[i] = (int)collision.area[i].x();
					y[i] = AREA_HEIGHT - (int)collision.area[i].y();
				}
				imageGraphics.setColor(ColorPaleteForRobots.getLighter(collision.withObjectId));
				imageGraphics.fillPolygon(x, y, collision.area.length);
				//imageGraphics.setColor(new Color(0xffaaff));
				//imageGraphics.fillOval((int)collision.position.x(), AREA_HEIGHT-(int)collision.position.y(), 10, 10);
				message.append("Collision with " + collision.withObjectId + " with probability " + collision.probability);
				message.append('\n');
			}

			collisions.setText(message.toString());

			basicRobotPainter(irData, imageGraphics);
		}

		/**
		 * Helper class to draw the robots as triangles that points towards their movement direction.
		 * 
		 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
		 */
		private class Triangle {
			public final int x[], y[], n = 3;

			private final double alpha = FastMath.PI / 6d + FastMath.PI / 2d;
			private final double beta = 10d;

			public Triangle(final Point center, final double direction) {
				x = new int[] {
					(int)(center.x() + FastMath.cos(direction) * beta),
					(int)(center.x() + FastMath.cos(direction + alpha) * beta / 2d),
					(int)(center.x() + FastMath.cos(direction - alpha) * beta / 2d)
				};

				//Here we use minus because the the image Y dimension orientation
				//grows towards the bottom of the screen.
				y = new int[] {
					(int)(center.y() - FastMath.sin(direction) * beta),
					(int)(center.y() - FastMath.sin(direction + alpha) * beta / 2d),
					(int)(center.y() - FastMath.sin(direction - alpha) * beta / 2d)
				};
			}
		}

	}

}
