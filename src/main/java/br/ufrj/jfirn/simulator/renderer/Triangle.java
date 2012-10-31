package br.ufrj.jfirn.simulator.renderer;

import org.apache.commons.math3.util.FastMath;

import br.ufrj.jfirn.common.Point;

/**
 * Helps to render the robots as triangles.
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
class Triangle {
	public final int x[], y[], n = 3;

	private final double alpha = FastMath.PI / 6d + FastMath.PI / 2d;
	private final double beta = 10d;

	public Triangle(final Point center, final double direction) {
		x = new int[] {
			(int)(center.x() + FastMath.cos(direction) * beta),
			(int)(center.x() + FastMath.cos(direction + alpha) * beta / 2d),
			(int)(center.x() + FastMath.cos(direction - alpha) * beta / 2d)
		};
		y = new int[] {
			(int)(center.y() - FastMath.sin(direction) * beta),
			(int)(center.y() - FastMath.sin(direction + alpha) * beta / 2d),
			(int)(center.y() - FastMath.sin(direction - alpha) * beta / 2d)
		};
	}

}
