package eu.zomtec.em2012.score;

public class HighScore {
	private Integer position;
	private String username;
	private Integer totalScore;
	private Long userId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getTotalScore() {
		return totalScore == null ? 0 : totalScore;
	}

	public HighScore() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Long getUserId() {
		return userId;
	}

	public HighScore(Integer position, String username, Integer totalScore,
			Long userId) {
		super();
		this.position = position;
		this.username = username;
		this.totalScore = totalScore;
		this.userId = userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return "HighScore [userId=" + userId + ", username=" + username
				+ ", totalScore=" + totalScore + "]";
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
}
