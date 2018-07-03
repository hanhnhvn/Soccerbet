package eu.zomtec.em2012.updater;

import java.util.Date;
import java.util.Map;

public class League {
	private boolean tournament;
	private Long leagueId;
	private Date timestamp;
	private Map<Long, Match> matches;

	//26-June-2018 +++
	private Map<String, TeamSfg> teams;
	private Map<String, String> groups;	
	
	public Map<String, TeamSfg> getTeams() {
		return teams;
	}

	public void setTeams(Map<String, TeamSfg> teams) {
		this.teams = teams;
	}

	public Map<String, String> getGroups() {
		return groups;
	}

	public void setGroups(Map<String, String> groups) {
		this.groups = groups;
	}
	// +++

	public boolean isTournament() {
		return tournament;
	}

	public void setTournament(boolean tournament) {
		this.tournament = tournament;
	}

	public Long getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(Long leagueId) {
		this.leagueId = leagueId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Map<Long, Match> getMatches() {
		return matches;
	}

	public void setMatches(Map<Long, Match> matches) {
		this.matches = matches;
	}

}
