package br.ufrj.jfirn.simulator.event;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Engine to run event-driven simulations.
 *
 */
public class Engine {

	private long currentTime;

	//private Queue<Event> events = new LinkedBlockingQueue<Event>();
	private Queue<Event> events = new PriorityBlockingQueue<Event>();

	public void simulate() {
		while ( !this.events.isEmpty() ) {
			Event event = this.events.poll();

			assert this.currentTime <= event.getScheduledTime();

			this.currentTime = event.getScheduledTime();

			event.happen();
		}
	}

	private void evaluate(Event event) {
		// TODO Auto-generated method stub
	}

	private void addEvent(Event event, long timeFromNow) {
		event.setScheduledTime(this.currentTime + timeFromNow);
		this.events.add(event);
	}

}
