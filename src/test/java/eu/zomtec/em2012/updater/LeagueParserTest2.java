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

public class LeagueParserTest2 {
	private LeagueParser leagueParser = new LeagueParser();
	
	private String teamsJson = null;
	private String matchesJson = null;
	
	public LeagueParserTest2() {
		try {
			final InputStream inputStreamTeams = LeagueParser.class.getClassLoader().getResourceAsStream("./teams.json");
			teamsJson = IOUtils.toString(inputStreamTeams);
			
			final InputStream inputStreamMatches = LeagueParser.class.getClassLoader().getResourceAsStream("./matches.json");
			matchesJson = IOUtils.toString(inputStreamMatches);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLeague() throws JSONException, ParseException {
		Assert.assertTrue(StringUtils.isNotBlank(teamsJson));
		Assert.assertTrue(StringUtils.isNotBlank(matchesJson));
		
		final League league = leagueParser.parseV2(teamsJson, matchesJson);
		Assert.assertNotNull(league);
		
		Assert.assertEquals(new Long(1000L), league.getLeagueId());
		Assert.assertTrue(league.isTournament());
		//Assert.assertEquals(1335547910000L, league.getTimestamp().getTime());
	}
	
	@Test
	public void testTeams() throws JSONException, ParseException {
		League league = new League();
		
		final League league1 = leagueParser.parseTeams(league, teamsJson);
		
		final Map<String, TeamSfg> teams = league1.getTeams();
		Assert.assertNotNull(teams);
		Assert.assertFalse(teams.isEmpty());
		
		Assert.assertEquals(33, teams.size());
		
		final TeamSfg team = teams.get("RUS");
		Assert.assertEquals(Long.valueOf(1L), team.getId());
		Assert.assertEquals("RUS", team.getFifa_code());
		Assert.assertEquals("A", team.getGroup_letter());
		Assert.assertEquals("Russia", team.getCountry());
		Assert.assertEquals("1", team.getGroup_id());
	}
	
	@Test
	public void testMatch01() throws JSONException, ParseException {
		
		League league = new League();
		League league1 = leagueParser.parseTeams(league, teamsJson);
		
		league1 = leagueParser.parseMatches(league1, matchesJson);
		
		final Map<Long, Match> matches = league1.getMatches();
		Assert.assertNotNull(matches);
		Assert.assertFalse(matches.isEmpty());
		
		Assert.assertEquals(64, matches.size());
		
		final Match match = matches.get(300340184L);
		Assert.assertEquals(Long.valueOf(300340184L), match.getMatchId());
		//Assert.assertEquals(0, match.getPhase().intValue());
		Assert.assertEquals(2, match.getScoreAway().intValue());
		Assert.assertEquals(2, match.getScoreHome().intValue());
		Assert.assertEquals(MatchStatus.FINISHED, match.getStatus());
		Assert.assertEquals(6L, match.getTeamIdHome().longValue());
		Assert.assertEquals(7L, match.getTeamIdAway().longValue());
		//Assert.assertEquals("Gruppe A", match.getTournamentGroup());
		//Assert.assertEquals(1339171200000L, match.getStartTime().getTime());
		
		//2018-06-25T18:00:00Z
	}
	
	
	@Test
	public void testMatch() throws JSONException, ParseException {
		final League league = leagueParser.parseV2(teamsJson, matchesJson);
		
		final Map<Long, Match> matches = league.getMatches();
		Assert.assertNotNull(matches);
		Assert.assertFalse(matches.isEmpty());
		
		Assert.assertEquals(64, matches.size());
		
		final Match match = matches.get(300340184L);
		Assert.assertEquals(Long.valueOf(300340184L), match.getMatchId());
		//Assert.assertEquals(0, match.getPhase().intValue());
		Assert.assertEquals(2, match.getScoreAway().intValue());
		Assert.assertEquals(2, match.getScoreHome().intValue());
		Assert.assertEquals(MatchStatus.FINISHED, match.getStatus());
		Assert.assertEquals(6L, match.getTeamIdHome().longValue());
		Assert.assertEquals(7L, match.getTeamIdAway().longValue());
		//Assert.assertEquals("Gruppe A", match.getTournamentGroup());
		//Assert.assertEquals(1339171200000L, match.getStartTime().getTime());
		
		//2018-06-25T18:00:00Z
	}
}
