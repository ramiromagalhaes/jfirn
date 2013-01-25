package br.ufrj.jfirn.simulator;

import java.util.Random;

import br.ufrj.jfirn.common.BasicRobot;
import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.common.geometry.Point;
import br.ufrj.jfirn.intelligent.IntelligentRobot;
import br.ufrj.jfirn.intelligent.sensors.Eye;
import br.ufrj.jfirn.mobileObstacle.CrazyRobot;
import br.ufrj.jfirn.mobileObstacle.RandomWalkerRobot;
import br.ufrj.jfirn.mobileObstacle.SineRobot;
import br.ufrj.jfirn.mobileObstacle.SquareRobot;

public class Scenario2 {

	public static void main(String[] args) {
		final Engine engine = new Engine();

		{ //TODO need to improve the IntelligentRobot registration process.
			final IntelligentRobot ir = new IntelligentRobot (200, 200, 0,
				new Point(400, 100),
				new Point(200, 400),
				new Point(300, 450),
				new Point(200, 200)
			);

			engine.addIntelligentRobot(ir, new Eye(200, ir), ir);
		}

		engine.addRobot( new BasicRobot       (185, 175, Robot.RIGHT, 5) );
		engine.addRobot( new RandomWalkerRobot(750, 200, Robot.LEFT, 5, new Random(1)) );
		engine.addRobot( new SineRobot        (535, 100, Robot.UP,    5, 7) );
		engine.addRobot( new CrazyRobot       (250, 375, Robot.RIGHT, 5, new Random(1)) );
		engine.addRobot( new SquareRobot      (300, 425, Robot.UP,    5) );

		engine.simulate(200);
	}
}
