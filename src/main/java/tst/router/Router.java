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
	public Route getRoute(String[] stops) {
		
		List<Segment> segments = new ArrayList<Segment>();
		
		for (int i = 0; i < stops.length - 1; i++) {
			
			String start = stops[i];
			String destination = stops[i + 1];
			Segment segment = findByStartAndDestination(start, destination);
			
			if (segment == null) {
				//if there is no segment found then discard any previosly found ones
				return null;
			}
			
			segments.add(segment);
		}
		
		return new Route(segments) ;
	}

	/**
	 * Returns all of the routes for a given starting point and a destination.
	 * @param start starting point
	 * @param destination destination
	 * @return routes
	 */
	public List<Route> getRoutes(String start, String destination) {
		
		List<Route> routes = new ArrayList<Route>();
		List<Segment> candidate = new ArrayList<Segment>();
		
		//traverse the routes recursively
		getRoutes(routes, candidate, start, destination, 0);
		
		return routes;
	}

	/**
	 * Builds the routes recursively for a given starting point and a destination.
	 * 
	 * @param routes list of routes
	 * @param candidate route candidate
	 * @param start starting point
	 * @param destination destination
	 * @param recursionLevel level of recursion
	 * @return updated candidate
	 */
	private List<Segment> getRoutes(List<Route> routes, List<Segment> candidate, String start, String destination, int recursionLevel) {
		
		for (Segment segment : segments) {
			if (start.equals(segment.getStart())) {
				//add segment to the candidate route stack
				candidate.add(segment);
				
				if (destination.equals(segment.getDestination())) {
					//the candidate contains the route matching the destination
					//add the route to the list 
					addRoute(candidate, routes);
				} else {
					//make a recursive call to traverse this route futher
					candidate = getRoutes(routes, candidate, segment.getDestination(), destination, recursionLevel + 1);
				}
				//remove last segment from the candidate
				candidate.remove(candidate.size() - 1);
			}
		}

		return candidate;
	}

	private void addRoute(List<Segment> candidate, List<Route> routes) {

		List<Segment> segments = new ArrayList<Segment>(candidate);
		routes.add(new Route(segments));
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
