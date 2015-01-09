package tst.router;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouterTest {
	private static final Logger logger = LoggerFactory.getLogger(RouterTest.class);
	
	private Router router;
	
    @Before
    public void before() {
    	router = new Router("data.txt");
    }
	
	@Test
	public void testRoute1_The_out_and_back() {
		
		testRoute(new String[]{"Wisteria Island", "Fleming Key", "Sigsbee Park"}, 2, 18);
	}

	@Test
	public void testRoute2_Night_on_the_town() {
		
		testRoute(new String[]{"Wisteria Island", "Southernmost Point"}, 1, 25);
	}
	
	@Test
	public void testRoute3_Northside_tour() {
		
		testRoute(new String[]{"Wisteria Island", "Southernmost Point", "Sigsbee Park"}, 2, 43);
	}
	
	@Test
	public void testRoute4_round_the_world_tour() {
		
		testRoute(new String[]{"Wisteria Island", "Sunset Key", "Fleming Key", "Sigsbee Park", "Southernmost Point"}, 4, 58);
	}
	
	@Test
	public void testRoute5_Eastside_tour() {
		
		Route route = router.getRoute(new String[]{"Wisteria Island", "Sunset Key", "Southernmost Point"});
		if (logger.isDebugEnabled()) {
			logger.debug("testRoute: route="+ route);
		}
		assertNull("Expected null route", route);
	}

	@Test
	public void testGetRoutes6_Southernmost_Point_with_3_stops() {
		
		String start = "Southernmost Point";
		String destination = start;
		List<Route> routes = router.getRoutes(start, destination);
		if (logger.isDebugEnabled()) {
			logger.debug("testGetRoutes6: routes="+ routes);
		}
		assertNotNull("Expected not null routes", routes);
		assertEquals("Expected routes number to match", 2, routes.size());
		
		int with3stops = 0;
		for (Route route : routes) {
			List<Segment> segments = route.getSegments();
			if (segments.size() == 2) {
				if (logger.isDebugEnabled()) {
					logger.debug("testGetRoutes6: 3 stops route="+ route);
				}
				assertNotNull("Expected not null route", route);
				assertNotNull("Expected not null segments", segments);
				
				assertEquals("Expected start to match", start, segments.get(0).getStart());
				assertEquals("Expected destination to match", destination, segments.get(segments.size() - 1).getDestination());
				with3stops++;
			}
		}
		
		assertEquals("Expected routes number to match", 1, with3stops);
	}

	@Test
	public void testGetRoutes7_Wisteria_Island_Sigsbee_Park_with_4_stops() {
		
		String start = "Wisteria Island";
		String destination = "Sigsbee Park";
		List<Route> routes = router.getRoutes(start, destination);
		if (logger.isDebugEnabled()) {
			logger.debug("testGetRoutes7: routes="+ routes);
		}
		assertNotNull("Expected not null routes", routes);
		assertEquals("Expected routes number to match", 4, routes.size());
		
		int with4stops = 0;
		for (Route route : routes) {
			List<Segment> segments = route.getSegments();
			if (segments.size() == 4) {
				if (logger.isDebugEnabled()) {
					logger.debug("testGetRoutes7: 4 stops route="+ route);
				}
				assertNotNull("Expected not null route", route);
				assertNotNull("Expected not null segments", segments);
				
				assertEquals("Expected start to match", start, segments.get(0).getStart());
				assertEquals("Expected destination to match", destination, segments.get(segments.size() - 1).getDestination());
				with4stops++;
			}
		}
		
		assertEquals("Expected routes number to match", 1, with4stops);
	}

	@Test
	public void testGetRoutes8_shortest_time_Wisteria_Island_Sigsbee_Park() {
		
		String start = "Wisteria Island";
		String destination = "Sigsbee Park";
		List<Route> routes = router.getRoutes(start, destination);
		if (logger.isDebugEnabled()) {
			logger.debug("testGetRoutes8: routes="+ routes);
		}
		
		Route shortest = null;
		for (Route route : routes) {
			if (shortest == null || shortest.getTotalDuration() > route.getTotalDuration()) {
				shortest = route;
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("testGetRoutes8: shortest="+ shortest);
		}
		
		assertNotNull("Expected not null route", shortest);
		assertNotNull("Expected not null segments", shortest.getSegments());
		assertEquals("Expected segments number to match", 2, shortest.getSegments().size());
		assertEquals("Expected total duration to match", 18, shortest.getTotalDuration());
	}

	@Test
	public void testGetRoutes9_shortest_trip_Fleming_Key() {
		
		String start = "Fleming Key";
		String destination = start;
		List<Route> routes = router.getRoutes(start, destination);
		if (logger.isDebugEnabled()) {
			logger.debug("testGetRoutes9: routes="+ routes);
		}
		
		Route shortest = null;
		for (Route route : routes) {
			if (shortest == null || shortest.getTotalDuration() > route.getTotalDuration()) {
				shortest = route;
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("testGetRoutes9: shortest="+ shortest);
		}
		
		assertNotNull("Expected not null route", shortest);
		assertNotNull("Expected not null segments", shortest.getSegments());
		assertEquals("Expected segments number to match", 4, shortest.getSegments().size());
		assertEquals("Expected total duration to match", 63, shortest.getTotalDuration());
	}

	@Test
	public void testGetRoutes10_Sigsbee_Park_less_than_hour() {
		
		String start = "Sigsbee Park";
		String destination = start;
		List<Route> routes = router.getRoutes(start, destination);
		if (logger.isDebugEnabled()) {
			logger.debug("testGetRoutes10: routes="+ routes);
		}
		
		int lessThanHour = 0;
		for (Route route : routes) {
			List<Segment> segments = route.getSegments();
			long totalDuration = route.getTotalDuration();
			if (totalDuration < 60) {
				if (logger.isDebugEnabled()) {
					logger.debug("testGetRoutes10: less than an hour route="+ route);
				}
				assertNotNull("Expected not null route", route);
				assertNotNull("Expected not null segments", segments);
				
				assertEquals("Expected start to match", start, segments.get(0).getStart());
				assertEquals("Expected destination to match", destination, segments.get(segments.size() - 1).getDestination());
				lessThanHour++;
			}
		}
		
		assertEquals("Expected routes number to match", 1, lessThanHour);
	}	
	
	private void testRoute(String[] trip, int expectedSegmentsNumber, long expectedDuration) {
		
		Route route = router.getRoute(trip);
		long duration = route.getTotalDuration();
		if (logger.isDebugEnabled()) {
			logger.debug("testRoute: route="+ route);
		}
		assertNotNull("Expected not null route", route);
		assertNotNull("Expected not null segments", route.getSegments());
		assertEquals("Expected segments number to match", expectedSegmentsNumber, route.getSegments().size());
		assertEquals("Expected total duration to match", expectedDuration, duration);
	}

}
