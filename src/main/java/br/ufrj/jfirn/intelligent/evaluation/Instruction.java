package br.ufrj.jfirn.intelligent.evaluation;

import br.ufrj.jfirn.common.Robot;



/**
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class Instruction {

	public double newDirection, newSpeed;

	public void apply(Robot	r) {
		r.direction(newDirection);
		r.speed(newSpeed);
	}

	@Override
	public String toString() {
		return "Instruction[direction: " + newDirection + "; speed: " + newSpeed + "]";
	}

}
