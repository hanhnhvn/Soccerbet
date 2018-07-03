package eu.zomtec.em2012.updater;

import java.util.Date;

public class Match {
	private String tournamentGroup;
	private Long matchId;
	private Long teamIdAway;
	private Long teamIdHome;
	private Integer scoreHome;
	private Integer scoreAway;
	private Integer phase;
	private MatchStatus status;
	private Date startTime;

	public String getTournamentGroup() {
		return tournamentGroup;
	}

	public void setTournamentGroup(String tournamentGroup) {
		this.tournamentGroup = tournamentGroup;
	}

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public Long getTeamIdAway() {
		return teamIdAway;
	}

	public void setTeamIdAway(Long teamIdAway) {
		this.teamIdAway = teamIdAway;
	}

	public Long getTeamIdHome() {
		return teamIdHome;
	}

	public void setTeamIdHome(Long teamIdHome) {
		this.teamIdHome = teamIdHome;
	}

	public Integer getScoreHome() {
		return scoreHome;
	}

	public void setScoreHome(Integer scoreHome) {
		this.scoreHome = scoreHome;
	}

	public Integer getScoreAway() {
		return scoreAway;
	}

	public void setScoreAway(Integer scoreAway) {
		this.scoreAway = scoreAway;
	}

	public Integer getPhase() {
		return phase;
	}

	public void setPhase(Integer phase) {
		this.phase = phase;
	}

	public MatchStatus getStatus() {
		return status;
	}

	public void setStatus(MatchStatus status) {
		this.status = status;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
}
