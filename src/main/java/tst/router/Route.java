package tst.router;

import java.util.List;

/**
 * Route that contains the list of the segments. 
 */
public class Route {

	private List<Segment> segments;

	public Route(List<Segment> segments) {
		setSegments(segments);
	}
	
	/**
	 * Returns the total duration of the route.
	 * @param segments
	 * @return total duration of the route.
	 */
	public long getTotalDuration() {
		
		long duration = 0;

		if (getSegments() != null) {
			for (Segment segment : getSegments()) {
				duration += segment.getDuration(); 
			}
		}
		
		return duration;
	}
	
	public List<Segment> getSegments() {
		return segments;
	}

	private void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	/**
	 * Returns nicely formatted string representation.
	 * @return nicely formatted string representation
	 */
	public String toFormattedString() {

		StringBuilder sb = null;
		for (Segment segment : segments) {
			if (sb == null) {
				sb = new StringBuilder();
				sb.append(segment.getStart());
			} else {
				sb.append(" -> ").append(segment.getDestination());
			}
		}
		
		sb.append(": ").append(getTotalDuration());
		
		return sb.toString();
	}

	@Override
	public String toString() {
		return String.format("Route [segments=%s]", segments);
	}
	
}
