package eu.zomtec.em2012.updater;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

public class LeagueServiceTest {
	private static final String TEST_URL = "http://worldcup.sfg.io/matches";//"http://nak.zomtec.eu/kicker-demo.json";
	private static final String TEST_TEAM_FEEDURL = "http://worldcup.sfg.io/teams/";
	
	private LeagueService leagueService = new LeagueService();
	private String jsonText = null;
	
	public LeagueServiceTest() {
		leagueService = new LeagueService();
		leagueService.setFeedUrl(TEST_URL);
		leagueService.setTeamFeedUrl(TEST_TEAM_FEEDURL);
		
		try {
			final InputStream inputStream = LeagueServiceTest.class.getClassLoader().getResourceAsStream("./kicker-demo.json");
			jsonText = IOUtils.toString(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInit() {
		Assert.assertEquals(TEST_URL, leagueService.getFeedUrl());
	}
	
	@Test
	public void testLoadAndParse() throws ClientProtocolException, IOException, JSONException, ParseException {
		final League league = leagueService.loadLeague();
		Assert.assertNotNull(league);
		Assert.assertEquals(107L, league.getLeagueId().longValue());
	}
}
