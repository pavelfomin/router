package tst.router;

/**
 * Route segment. 
 */
public class Segment {

	private String start;
	private String destination;
	private long duration;
	
	public Segment(String start, String destination, long duration) {
		setStart(start);
		setDestination(destination);
		setDuration(duration);
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
		return String.format("Segment [start=%s, destination=%s, duration=%s]",
				start, destination, duration);
	}
	
}
