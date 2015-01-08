package tst.router;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Router that is responsible for the routes calculation. 
 */
public class Router {
	private static final Logger logger = LoggerFactory.getLogger(Router.class);
	
	private List<Segment> segments;

	/**
	 * Creates new instance.
	 * @param segments segments
	 */
	public Router(List<Segment> segments) {
		this.segments = segments;
	}

	/**
	 * Creates new instance.
	 * @param dataFile comma separated data file
	 */
	public Router(String dataFile) {
		this.segments = loadSegments(dataFile);
	}

	/**
	 * Returns the route as a list of segments. 
	 * @param stops list of stops
	 * @return route
	 */
	public List<Segment> getRoute(String[] stops) {
		
		List<Segment> route = new ArrayList<Segment>();
		
		for (int i = 0; i < stops.length - 1; i++) {
			
			String start = stops[i];
			String destination = stops[i + 1];
			Segment segment = findByStartAndDestination(start, destination);
			
			if (segment == null) {
				//if there is no segment found then discard any previosly found ones
				return null;
			}
			
			route.add(segment);
		}
		
		return route ;
	}

	/**
	 * Returns the total duration of the route.
	 * @param segments
	 * @return total duration of the route.
	 */
	public static long getTotalDuration(List<Segment> segments) {
		
		long duration = 0;
		for (Segment route : segments) {
			duration += route.getDuration(); 
		}
		return duration;
	}
	
	/**
	 * Returns the segment matching the start and destination points.
	 * @param start
	 * @param destination
	 * @return matching segment 
	 */
	private Segment findByStartAndDestination(String start, String destination) {
		
		for (Segment route : segments) {
			if (start.equals(route.getStart()) && destination.equals(route.getDestination())) {
				return route;
			}
		}
		
		return null;
	}

	/**
	 * Loads segments from the comma separated data file.
	 * @param dataFile
	 * @return loaded segments
	 */
	private List<Segment> loadSegments(String dataFile) {
		List<Segment> routes = new ArrayList<Segment>();
		
		BufferedReader br = null;
		try {
			
			br = new BufferedReader(new FileReader(dataFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] words = line.split(","); //TODO: better handling of the separator?
				if (words.length == 3) {
					Segment route = new Segment(words[0], words[1], Long.parseLong(words[2]));
					routes.add(route);
				} else {
					logger.warn("Incorrect format. Skipping the input data line: "+ line);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to load data from file: "+ dataFile, e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("Failed to close the reader", e);
				}
			}
		}
		
		return routes;
	}
	
}
