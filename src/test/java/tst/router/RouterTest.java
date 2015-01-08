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
		
		List<Route> routes = router.getRoutes("Southernmost Point", "Southernmost Point");
		if (logger.isDebugEnabled()) {
			logger.debug("testGetRoutes6_Southernmost_Point_with_3_stops: routes="+ routes);
		}
		assertNotNull("Expected not null routes", routes);
		assertEquals("Expected routes number to match", 1, routes.size());
		
		Route route = routes.get(0);
		assertNotNull("Expected not null route", route);
		assertNotNull("Expected not null segments", route.getSegments());
		assertEquals("Expected segments number to match", 2, route.getSegments().size());
		
		assertEquals("Expected stop 1 to match", "Southernmost Point", route.getSegments().get(0).getStart());
		assertEquals("Expected stop 2 to match", "Sigsbee Park", route.getSegments().get(0).getDestination());
		assertEquals("Expected stop 3 to match", "Southernmost Point", route.getSegments().get(1).getDestination());
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
