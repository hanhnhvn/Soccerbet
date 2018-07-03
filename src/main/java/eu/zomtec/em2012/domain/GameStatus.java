package eu.zomtec.em2012.domain;

import eu.zomtec.em2012.updater.MatchStatus;

public enum GameStatus {
	UPCOMMING(BetStatus.OPEN), RUNNING(BetStatus.SCORE_TEMP), FINISHED(BetStatus.SCORE_FINAL), RECALCULATE(BetStatus.SCORE_FINAL);
	
	private GameStatus(BetStatus betStatus) {
		this.betStatus = betStatus;
	}

	private final BetStatus betStatus;
	
	public static final GameStatus getByMatchStatus(MatchStatus matchStatus) {
		switch (matchStatus) {
		case ACTIVE:
			return RUNNING;
		case FINISHED:
			return FINISHED;
		case SCHEDULED:
			return UPCOMMING;
		default:
			return null;
		}
	}

	public BetStatus getBetStatus() {
		return betStatus;
	}
}
