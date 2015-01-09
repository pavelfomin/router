package tst.router;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RouterDashboardTest {
	private Router router;
	
    @Before
    public void before() {
    	router = new Router("data/data.txt", true);
    }
	
	@Test
	public void testDashboard() {
		
		printRoute(new String[]{"Wisteria Island", "Fleming Key", "Sigsbee Park"}, "1. *The out-and-back*");
		printRoute(new String[]{"Wisteria Island", "Southernmost Point"}, "2. *Night on the town*");
		printRoute(new String[]{"Wisteria Island", "Southernmost Point", "Sigsbee Park"}, "3. *Northside tour*");
		printRoute(new String[]{"Wisteria Island", "Sunset Key", "Fleming Key", "Sigsbee Park", "Southernmost Point"}, "4. *The 'round the world' tour*");
		printRoute(new String[]{"Wisteria Island", "Sunset Key", "Southernmost Point"}, "5. *Eastside tour*");
		
		printRoutes6_Southernmost_Point_with_3_stops();
		printRoutes7_Wisteria_Island_Sigsbee_Park_with_4_stops();
		printShortestTime("Wisteria Island", "Sigsbee Park", "8. %d");
		printShortestTime("Fleming Key", "Fleming Key", "9. %d");
		printRoutes10_Sigsbee_Park_less_than_hour();
	}
	
	private void printRoutes6_Southernmost_Point_with_3_stops() {
		
		String start = "Southernmost Point";
		String destination = start;
		List<Route> routes = router.getRoutes(start, destination);
		
		int with3stops = 0;
		for (Route route : routes) {
			List<Segment> segments = route.getSegments();
			if (segments.size() == 2) {
				with3stops++;
			}
		}
		
		System.out.println(String.format("6. %d", with3stops));
	}

	private void printRoutes7_Wisteria_Island_Sigsbee_Park_with_4_stops() {
		
		String start = "Wisteria Island";
		String destination = "Sigsbee Park";
		List<Route> routes = router.getRoutes(start, destination);
		
		int with4stops = 0;
		for (Route route : routes) {
			List<Segment> segments = route.getSegments();
			if (segments.size() == 4) {
				with4stops++;
			}
		}
		
		System.out.println(String.format("7. %d", with4stops));
	}

	private void printShortestTime(String start, String destination, String format) {
		
		List<Route> routes = router.getRoutes(start, destination);
		
		Route shortest = null;
		for (Route route : routes) {
			if (shortest == null || shortest.getTotalDuration() > route.getTotalDuration()) {
				shortest = route;
			}
		}

		System.out.println(String.format(format, shortest.getTotalDuration()));
	}

	private void printRoutes10_Sigsbee_Park_less_than_hour() {
		
		String start = "Sigsbee Park";
		String destination = start;
		List<Route> routes = router.getRoutes(start, destination);
		
		int lessThanHour = 0;
		for (Route route : routes) {
			long totalDuration = route.getTotalDuration();
			if (totalDuration < 60) {
				lessThanHour++;
			}
		}
		
		System.out.println(String.format("10. %d", lessThanHour));
	}	
	
	private void printRoute(String[] trip, String name) {
		
		Route route = router.getRoute(trip);
		System.out.println(String.format("%s: %s: %s", name, toFormattedString(trip), (route == null ? "You'll have to swim that one" : route.getTotalDuration())));
	}

	private String toFormattedString(String[] trip) {

		StringBuilder sb = null;
		for (String segment : trip) {
			if (sb == null) {
				sb = new StringBuilder();
			} else {
				sb.append(" -> ");
			}
			sb.append(segment);
		}
		
		return sb.toString();
	}
	
}
