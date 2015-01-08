package tst.router;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Router {

	private static List<Route> routes;

	public static void main(String[] args) throws IOException {
		
		String dataFile = "data.txt";
		
		Router ferry = new Router();
		routes = ferry.loadRoutes(dataFile);
		System.out.println("Ferry.main(): routes"+ routes); //TODO: use logger
		String[] trip = new String[]{};
		//List<Route> matched = ferry.getRoutes(trip);
		
	}

	private List<Route> getRoutes(String[] stops) {
		
		Stack<Route> stack = new Stack<Route>();
		Route candidate = null;
		for (String stop : stops) {
			if (candidate != null && stop.equals(candidate.getDestination())) {
				
			}
			Route route = findByStart(stop);
			if (route != null) {
				candidate = route;
			}
		}
		List<Route> matched = new ArrayList<Route>();
		return matched ;
	}

	private Route findByStart(String start) {
		
		for (Route route : routes) {
			if (start.equals(route.getStart())) {
				return route;
			}
		}
		
		return null;
	}

	private List<Route> loadRoutes(String dataFile) {
		List<Route> routes = new ArrayList<Route>();
		
		BufferedReader br = null;
		try {
			
			br = new BufferedReader(new FileReader(dataFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] words = line.split(","); //TODO: better handling of the separator
				if (words.length == 3) {
					Route route = new Route(words[0], words[1],
							Long.parseLong(words[2]));
					routes.add(route);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to load data from file: "+ dataFile, e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO: use logger
					e.printStackTrace();
				}
			}
		}
		
		return routes;
	}

}
