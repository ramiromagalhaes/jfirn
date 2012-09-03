package br.ufrj.jfirn.simulator.event;



public abstract class Event {
	private long scheduledTime;

	public void setScheduledTime(long time) {
		this.scheduledTime = time;
	}

	public long getScheduledTime() {
		return this.scheduledTime;
	}

	public abstract void happen();

}
