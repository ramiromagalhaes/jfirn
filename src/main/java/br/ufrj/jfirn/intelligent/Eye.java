package br.ufrj.jfirn.intelligent;

import br.ufrj.jfirn.common.Robot;

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

	private double sightRadius;
	private IntelligentRobot attachedTo; //this eye is attached to a certain point

	public Eye(double sightDistance, IntelligentRobot attachedTo) {
		this.sightRadius = sightDistance;
		this.attachedTo = attachedTo;
	}

	/**
	 * Is parameter p under this Eye's sight?
	 * Returns false if attachedTo.equals(p).
	 */
	public boolean sees(Robot p) {
		return !attachedTo.equals(p) &&
				attachedTo.position().distanceTo(p.position()) <= sightRadius;
	}

	/**
	 * Simply redirects the event to the object that this Eye is attached to.
	 */
	@Override
	public void onSight(SightEvent e) {
		attachedTo.onSight(e);
	}

}
