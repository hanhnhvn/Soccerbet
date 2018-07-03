package eu.zomtec.em2012.updater;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

public class LeagueParserTest {
	private LeagueParser leagueParser = new LeagueParser();
	
	private String jsonText = null;
	
	public LeagueParserTest() {
		try {
			final InputStream inputStream = LeagueParser.class.getClassLoader().getResourceAsStream("./kicker-demo.json");
			jsonText = IOUtils.toString(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLeague() throws JSONException, ParseException {
		Assert.assertTrue(StringUtils.isNotBlank(jsonText));
		final League league = leagueParser.parse(jsonText);
		Assert.assertNotNull(league);
		
		Assert.assertEquals(new Long(107L), league.getLeagueId());
		Assert.assertTrue(league.isTournament());
		Assert.assertEquals(1335547910000L, league.getTimestamp().getTime());
	}
	
	@Test
	public void testMatch() throws JSONException, ParseException {
		final League league = leagueParser.parse(jsonText);
		
		final Map<Long, Match> matches = league.getMatches();
		Assert.assertNotNull(matches);
		Assert.assertFalse(matches.isEmpty());
		
		Assert.assertEquals(31, matches.size());
		
		final Match match = matches.get(1122625L);
		Assert.assertEquals(Long.valueOf(1122625L), match.getMatchId());
		Assert.assertEquals(0, match.getPhase().intValue());
		Assert.assertEquals(0, match.getScoreAway().intValue());
		Assert.assertEquals(0, match.getScoreHome().intValue());
		Assert.assertEquals(MatchStatus.SCHEDULED, match.getStatus());
		Assert.assertEquals(1317L, match.getTeamIdHome().longValue());
		Assert.assertEquals(1306L, match.getTeamIdAway().longValue());
		Assert.assertEquals("Gruppe A", match.getTournamentGroup());
		Assert.assertEquals(1339171200000L, match.getStartTime().getTime());
	}
}
