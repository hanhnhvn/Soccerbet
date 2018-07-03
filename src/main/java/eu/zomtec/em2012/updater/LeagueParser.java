package eu.zomtec.em2012.updater;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LeagueParser {
	private static final String MATCHES2 = "matches";
	private static final String TIMESTAMP = "timestamp";
	private static final String IS_TOURNAMENT = "is_tournament";
	private static final String LEAGUE_ID = "leagueID";
	private static final String TOURNAMENT_GROUP = "tournament_group";
	private static final String STATUS = "status";
	private static final String PHASE = "phase";
	private static final String START_TIME = "startTime";
	private static final String TEAM2_SCORE = "team2Score";
	private static final String TEAM1_SCORE = "team1Score";
	private static final String TEAM2_ID = "team2Id";
	private static final String TEAM1_ID = "team1Id";
	private static final String MATCH_ID = "matchID";
	
	//private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	//2018-06-14T15:00:00Z
	
	static {
		SDF.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	public League parse(String string) throws JSONException, ParseException {
		JSONObject leagueObj = new JSONObject(string);
		
		final League league = new League();
		league.setLeagueId(leagueObj.getLong(LEAGUE_ID));
		league.setTournament(leagueObj.getBoolean(IS_TOURNAMENT));
		league.setTimestamp(SDF.parse(leagueObj.getString(TIMESTAMP)));
		
		final JSONArray matchesArray = leagueObj.getJSONArray(MATCHES2);
		final int length = matchesArray.length();
		final HashMap<Long, Match> matches = new HashMap<Long, Match>(length);
		
		for (int i = 0; i < length; i++) {
			final JSONObject jsonObject = matchesArray.getJSONObject(i);
			final Match match = parseMatch(jsonObject);
			matches.put(match.getMatchId(), match);
		}
		
		league.setMatches(matches);
		return league;
	}

	private Match parseMatch(JSONObject jsonObject) throws JSONException, ParseException {
		final Match match = new Match();
		
		if (!jsonObject.isNull(TOURNAMENT_GROUP)) {
			match.setTournamentGroup(jsonObject.getString(TOURNAMENT_GROUP));
		}
		
		match.setMatchId(jsonObject.getLong(MATCH_ID));
		
		match.setTeamIdHome(jsonObject.getLong(TEAM1_ID));
		match.setTeamIdAway(jsonObject.getLong(TEAM2_ID));
		match.setScoreHome(jsonObject.getInt(TEAM1_SCORE));
		match.setScoreAway(jsonObject.getInt(TEAM2_SCORE));
		
		match.setStartTime(SDF.parse(jsonObject.getString(START_TIME)));
		
		if (!jsonObject.isNull(PHASE)) {
			match.setPhase(jsonObject.getInt(PHASE));
		}
		
		match.setStatus(MatchStatus.valueOf(jsonObject.getString(STATUS).toUpperCase()));
		return match;
	}
	
	/*
	 * 
	 */
	public League parseV2(String teamsJson, String matchesJson) throws JSONException, ParseException {
		League league = new League();
		
		league.setLeagueId(1000L);
		league.setTournament(true);
		league = parseTeams(league, teamsJson);
		league = parseMatches(league, matchesJson);
		
		return league;		
	}

	/*
	 * 
	 */
	public League parseMatches(League league, String jsonText) throws JSONException, ParseException {
		
		final JSONArray matchesArray = new JSONArray(jsonText);
		final int length = matchesArray.length();
		final HashMap<Long, Match> matches = new HashMap<Long, Match>(length);
		final Map<String, TeamSfg> teams = league.getTeams();
		
		for (int i = 0; i < length; i++) {
			final JSONObject jsonObject = matchesArray.getJSONObject(i);
			final Match match = parseMatch02(jsonObject, teams);
			matches.put(match.getMatchId(), match);
		}
		
		league.setMatches(matches);
		return league;
	}
	
	/*
	 * 
	 */
	private Match parseMatch02(JSONObject jsonObject, Map<String, TeamSfg> teams) throws JSONException, ParseException {

		final Match match = new Match();
		
		match.setMatchId(jsonObject.getLong("fifa_id"));
		
		match.setTeamIdHome(Long.valueOf( ((TeamSfg)teams.get(jsonObject.getJSONObject("home_team").getString("code"))).getId()) );
		match.setTeamIdAway(Long.valueOf( ((TeamSfg)teams.get(jsonObject.getJSONObject("away_team").getString("code"))).getId()) );
		
		Boolean nullValue = false;
		nullValue = jsonObject.getJSONObject("home_team").isNull("goals");
		
		match.setScoreHome(nullValue? 0: jsonObject.getJSONObject("home_team").getInt("goals"));
		
		nullValue = jsonObject.getJSONObject("away_team").isNull("goals");
		match.setScoreAway(nullValue? 0: jsonObject.getJSONObject("away_team").getInt("goals"));
		
		match.setStartTime(SDF.parse(jsonObject.getString("datetime")));
		
		String tmpStr = jsonObject.getString("status").toUpperCase();
		MatchStatus status = MatchStatus.SCHEDULED;

		if ("COMPLETED".equalsIgnoreCase(tmpStr)) {
			status = MatchStatus.FINISHED;
		} else if ("FUTURE".equalsIgnoreCase(tmpStr)) {
			status = MatchStatus.SCHEDULED;
		} else {
			status = MatchStatus.ACTIVE;
		}
		
		/*
		switch (tmpStr) {
			case ("COMPLETED") :  
				status = MatchStatus.FINISHED;
				break;
			case ("FUTURE") :  
				status = MatchStatus.SCHEDULED;
				break;
			default: 
				status = MatchStatus.ACTIVE;
				break;
		}*/
		
		match.setStatus(status);
		return match;
	}
	
	
	/*
	 * [{"id":24,"country":"Korea Republic","alternate_name":null,"fifa_code":"KOR","group_id":6,"group_letter":"F"},,,]
	 */
	public League parseTeams(League league, String jsonText) throws JSONException, ParseException {

		final JSONArray teamsArray = new JSONArray(jsonText);
		final int length = teamsArray.length();
		final HashMap<String, TeamSfg> teams = new HashMap<String, TeamSfg>(length);
		
		final HashMap<String, String> groups = new HashMap<String, String>();
		
		for (int i = 0; i < length; i++) {
			final JSONObject jsonObject = teamsArray.getJSONObject(i);
			final TeamSfg team = parseTeam(jsonObject);
			teams.put(team.getFifa_code(), team);
			
			if (!groups.containsKey(team.getGroup_letter())) {
				groups.put(team.getGroup_letter(), team.getGroup_id());
			}
		}
		
		//+++ add default team
		TeamSfg tbdTeam = new TeamSfg();
		tbdTeam.setFifa_code("TBD");
		tbdTeam.setId(-1L);
		tbdTeam.setGroup_letter("TBD");
		teams.put(tbdTeam.getFifa_code(), tbdTeam);
		//+++
		
		league.setTeams(teams);
		league.setGroups(groups);
		return league;
	}
	
	/*
	 * {"id":24,"country":"Korea Republic","alternate_name":null,"fifa_code":"KOR","group_id":6,"group_letter":"F"}
	 */
	private TeamSfg parseTeam(JSONObject jsonObject) throws JSONException, ParseException {
		final TeamSfg team = new TeamSfg();
		team.setId(jsonObject.getLong("id"));
		team.setCountry(jsonObject.getString("country"));
		team.setAlternate_name(jsonObject.getString("alternate_name"));
		team.setFifa_code(jsonObject.getString("fifa_code"));
		team.setGroup_id(jsonObject.getString("group_id"));
		team.setGroup_letter(jsonObject.getString("group_letter"));
		return team;
	}

}
