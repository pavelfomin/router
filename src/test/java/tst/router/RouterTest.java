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
    	if (logger.isDebugEnabled()) {
			logger.debug("before: router="+ router);
		}
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
		
		List<Segment> route = router.getRoute(new String[]{"Wisteria Island", "Sunset Key", "Southernmost Point"});
		if (logger.isDebugEnabled()) {
			logger.debug("testRoute: route="+ route);
		}
		assertNull("Expected null route", route);
	}
	
	private void testRoute(String[] trip, int expectedSegmentsNumber, long expectedDuration) {
		
		List<Segment> route = router.getRoute(trip);
		long duration = Router.getTotalDuration(route);
		if (logger.isDebugEnabled()) {
			logger.debug("testRoute: route="+ route);
		}
		assertNotNull("Expected not null route", route);
		assertEquals("Expected to segments number to match", expectedSegmentsNumber, route.size());
		assertEquals("Expected total duration to match", expectedDuration, duration);
	}

}
