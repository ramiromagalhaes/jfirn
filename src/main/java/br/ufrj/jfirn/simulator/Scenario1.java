package br.ufrj.jfirn.simulator;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.BasicRobot;
import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.common.geometry.Point;
import br.ufrj.jfirn.intelligent.IntelligentRobot;
import br.ufrj.jfirn.intelligent.sensors.Eye;

public class Scenario1 {

	public static void main(String[] args) {
		final Engine engine = new Engine();

		{ //TODO need to improve the IntelligentRobot registration process.
			final IntelligentRobot ir = new IntelligentRobot (200, 200, 0,
				new Point(400, 100),
				new Point(200, 400),
				new Point(300, 450)
			);

			engine.addIntelligentRobot(ir, new Eye(200, ir), ir);
		}

		engine.addRobot( new BasicRobot(185, 175, Robot.RIGHT, 5) );
		engine.addRobot( new BasicRobot(400, 250, Robot.LEFT + FastMath.PI / 4, 5) );
		engine.addRobot( new BasicRobot(535, 150, Robot.LEFT, 5) );
		engine.addRobot( new BasicRobot(250, 125, Robot.RIGHT, 5) );
		engine.addRobot( new BasicRobot(300, 280, Robot.DOWN, 5) );

		engine.addRobot( new BasicRobot(800, 300, Robot.LEFT, 5) );
		engine.addRobot( new BasicRobot(800, 275, Robot.LEFT, 5) );
		engine.addRobot( new BasicRobot(800, 250, Robot.LEFT, 5) );
		engine.addRobot( new BasicRobot(800, 225, Robot.LEFT, 5) );
		engine.addRobot( new BasicRobot(800, 200, Robot.LEFT, 5) );
		engine.addRobot( new BasicRobot(800, 175, Robot.LEFT, 5) );

		engine.addRobot( new BasicRobot(200, 800, Robot.DOWN, 5) );
		engine.addRobot( new BasicRobot(225, 800, Robot.DOWN, 5) );
		engine.addRobot( new BasicRobot(250, 800, Robot.DOWN, 5) );
		engine.addRobot( new BasicRobot(275, 800, Robot.DOWN, 5) );
		engine.addRobot( new BasicRobot(300, 800, Robot.DOWN, 5) );
		engine.addRobot( new BasicRobot(325, 800, Robot.DOWN, 5) );

		engine.simulate(150);
	}
}
