package br.ufrj.jfirn.intelligent.sensors;

import java.util.Set;

import br.ufrj.jfirn.common.Robot;
import br.ufrj.jfirn.intelligent.IntelligentRobot;

/**
 * Eye holds specific parameters that should be used while detecting agents or objects in the
 * simulation.
 * 
 * You should attach an instance of Eye to an instance of {@link IntelligentRobot}.
 * 
 * @author <a href="mailto:ramiro.p.magalhaes@gmail.com">Ramiro Pereira de Magalhães</a>
 *
 */
public class Eye implements Sight {

	private final double sightRadius;
	private final IntelligentRobot attachedTo; //this eye is attached to a robot

	public Eye(double sightRadius, IntelligentRobot attachedTo) {
		this.sightRadius = sightRadius;
		this.attachedTo = attachedTo;
	}

	/**
	 * Is parameter p under this Eye's sight?
	 * Returns false if attachedTo.equals(p).
	 */
	public boolean sees(Robot r) {
		return (!attachedTo.equals(r))
			&& (attachedTo.position().distanceTo(r.position()) <= sightRadius);
	}

	/**
	 * Simply redirects the event to the object that this Eye is attached to.
	 */
	@Override
	public void onSight(Set<SightData> sighted) {
		attachedTo.onSight(sighted);
	}

}
