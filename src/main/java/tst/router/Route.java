package tst.router;

public class Route {

	private String start;
	private String destination;
	private long duration;
	
	public Route(String start, String destination, long duration) {
		this.start = start;
		this.destination = destination;
		this.duration = duration;
	}
	
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	@Override
	public String toString() {
		return String.format("Route [start=%s, destination=%s, duration=%s]",
				start, destination, duration);
	}
	
}
