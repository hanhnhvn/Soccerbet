package eu.zomtec.em2012.domain;

public enum BetScoreType {
	SCORE_MATCH(4), SCORE_DIFFERENCE(3), SCORE_WINNER(2), NOTHING(0);
	
	final int points;

	public int getPoints() {
		return points;
	}

	private BetScoreType(int points) {
		this.points = points;
	} 
}
