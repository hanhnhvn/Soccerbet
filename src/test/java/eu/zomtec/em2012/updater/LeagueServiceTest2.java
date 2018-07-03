package eu.zomtec.em2012.updater;

import java.io.IOException;
import java.text.ParseException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

public class LeagueServiceTest2 {
	private static final String TEST_URL = "http://worldcup.sfg.io/matches";//"http://nak.zomtec.eu/kicker-demo.json";
	private static final String TEST_TEAM_FEEDURL = "http://worldcup.sfg.io/teams/";
	
	private LeagueService leagueService = new LeagueService();
	//private String jsonText = null;
	
	public LeagueServiceTest2() {
		leagueService = new LeagueService();
		leagueService.setFeedUrl(TEST_URL);
		leagueService.setTeamFeedUrl(TEST_TEAM_FEEDURL);
		
		/*
		try {
			final InputStream inputStream = LeagueServiceTest2.class.getClassLoader().getResourceAsStream("./kicker-demo.json");
			jsonText = IOUtils.toString(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	@Test
	public void testInit() {
		Assert.assertEquals(TEST_URL, leagueService.getFeedUrl());
	}
	
	@Test
	public void testLoadAndParse() throws ClientProtocolException, IOException, JSONException, ParseException {
		final League league = leagueService.loadLeagueV2();
		Assert.assertNotNull(league);
		//Assert.assertEquals(107L, league.getLeagueId().longValue());
	}
}
