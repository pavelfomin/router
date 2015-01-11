package tst.router;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
	 * @param fromClasspath true if the datafile should be searched in the classpath, filesystem otherwise 
	 */
	public Router(String dataFile, boolean fromClasspath) {
		this.segments = loadSegments(dataFile, fromClasspath);
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
		
		//traverse the routes recursively and store all found routes in the list passed in
		getRoutes(routes, candidate, start, destination, 0);
		
		return routes;
	}

	/**
	 * Builds the routes recursively for a given starting point and a destination.
	 * 
	 * @param routes list of routes which will be updated
	 * @param candidate route candidate which is updated between the recursive calls
	 * @param start starting point
	 * @param destination destination
	 * @param recursionLevel level of recursion
	 * @return updated candidate
	 */
	private List<Segment> getRoutes(List<Route> routes, List<Segment> candidate, String start, String destination, int recursionLevel) {
		
		for (Segment segment : segments) {
			//if the segment's start point matches and the segment is not already part of the candidate route
			if (start.equals(segment.getStart()) && (! candidate.contains(segment))) {
				//add segment to the candidate route stack
				candidate.add(segment);
				
				if (destination.equals(segment.getDestination())) {
					//the candidate contains the route matching the destination
					//create new route using the candidate's segments and add the route to the list 
					routes.add(new Route(new ArrayList<Segment>(candidate)));
				} else {
					//make a recursive call to traverse this route further
					candidate = getRoutes(routes, candidate, segment.getDestination(), destination, recursionLevel + 1);
				}
				//remove last segment from the candidate
				candidate.remove(candidate.size() - 1);
			}
		}

		return candidate;
	}

	/**
	 * Returns the segment matching the start and the destination point.
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
	 * @param dataFile data file
	 * @param fromClasspath true if the datafile should be searched in the classpath, filesystem otherwise 
	 * @return loaded segments
	 */
	private List<Segment> loadSegments(String dataFile, boolean fromClasspath) {
		
		List<Segment> routes = new ArrayList<Segment>();
		
		BufferedReader br = null;
		try {
			Reader reader = null;
			if (fromClasspath) {
				InputStream is = getClass().getClassLoader().getResourceAsStream(dataFile);
				if (is == null) {
					throw new IllegalArgumentException("Failed to locate data file: "+ dataFile +" in the classpath");
				}
				reader = new InputStreamReader(is);
			} else {
				reader = new FileReader(dataFile);
			}
			
			br = new BufferedReader(reader);
			
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] words = line.split(",");
				if (words.length == 3) {
					Segment route = new Segment(words[0], words[1], Long.parseLong(words[2]));
					routes.add(route);
				} else {
					logger.warn("Incorrect format. Skipping the input data line: "+ line);
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to load the data from the file: "+ dataFile, e);
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
