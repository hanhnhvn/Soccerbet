package eu.zomtec.em2012.score;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class HighScoreService {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
	    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	//private static final String SQL_QUERY_HIGHSCORE_TEMP = "SELECT bet_user.id AS userId, bet_user.username AS username, IFNULL(SUM(bet.score),0) AS totalScore FROM bet_user LEFT JOIN bet ON bet.bet_user = bet_user.id AND bet.bet_status IN (2,3) WHERE bet_user.enabled = 1 GROUP BY bet_user.id ORDER BY totalScore DESC, username ASC";
	//private static final String SQL_QUERY_HIGHSCORE_FINAL = "SELECT bet_user.id AS userId, bet_user.username AS username, IFNULL(SUM(bet.score),0) AS totalScore FROM bet_user LEFT JOIN bet ON bet.bet_user = bet_user.id AND bet.bet_status IN (3) WHERE bet_user.enabled = 1 GROUP BY bet_user.id ORDER BY totalScore DESC, username ASC";
	//26-Jun-2018 oracle
	private static final String SQL_QUERY_HIGHSCORE_TEMP = "SELECT bet_user.id AS userId, bet_user.username AS username, NVL(SUM(bet.score),0) AS totalScore FROM bet_user LEFT JOIN bet ON bet.bet_user = bet_user.id AND bet.bet_status IN (2,3) WHERE bet_user.enabled = 1 GROUP BY bet_user.id, bet_user.username ORDER BY totalScore DESC, username ASC";
	private static final String SQL_QUERY_HIGHSCORE_FINAL = "SELECT bet_user.id AS userId, bet_user.username AS username, NVL(SUM(bet.score),0) AS totalScore FROM bet_user LEFT JOIN bet ON bet.bet_user = bet_user.id AND bet.bet_status IN (3) WHERE bet_user.enabled = 1 GROUP BY bet_user.id, bet_user.username ORDER BY totalScore DESC, username ASC";
	private static final String SQL_BET_MISSING = "SELECT bet_user.username FROM bet_user WHERE NOT EXISTS (SELECT * FROM bet WHERE bet.bet_user = bet_user.id AND bet.game = 7)";
	
	public List<HighScore> getHighScoreTemp() {
		return getHighScoreTemp(Integer.MAX_VALUE);
	}
	
	public List<HighScore> getHighScoreFinal() {
		return getHighScoreFinal(Integer.MAX_VALUE);
	}
	
	public List<HighScore> getHighScoreTemp(int limit) {
		//final List<HighScore> list = namedParameterJdbcTemplate.query(SQL_QUERY_HIGHSCORE_TEMP+" LIMIT "+limit, new HashMap<String, String>(0), new BeanPropertyRowMapper<HighScore>(HighScore.class));
		final List<HighScore> list = namedParameterJdbcTemplate.query(SQL_QUERY_HIGHSCORE_TEMP, new HashMap<String, String>(0), new BeanPropertyRowMapper<HighScore>(HighScore.class));
		assignPossitions(list);
		return list;
	}
	
	public List<HighScore> getHighScoreFinal(int limit) {
		//final List<HighScore> list = namedParameterJdbcTemplate.query(SQL_QUERY_HIGHSCORE_FINAL+" LIMIT "+limit, new HashMap<String, String>(0), new BeanPropertyRowMapper<HighScore>(HighScore.class));
		final List<HighScore> list = namedParameterJdbcTemplate.query(SQL_QUERY_HIGHSCORE_FINAL, new HashMap<String, String>(0), new BeanPropertyRowMapper<HighScore>(HighScore.class));
		assignPossitions(list);
		return list;
	}

	private void assignPossitions(final List<HighScore> list) {
		HighScore lastScore = null;
		Integer i = 0;
		
		for (HighScore highScore : list) {
			i++;
			if (lastScore == null) {
				highScore.setPosition(1);
			} else if (lastScore.getTotalScore().equals(highScore.getTotalScore())) {
				highScore.setPosition(lastScore.getPosition());
			} else if (lastScore.getTotalScore() > highScore.getTotalScore()){
				highScore.setPosition(i);
			} else {
				throw new RuntimeException("Scoreboard broken!");
			}

			lastScore = highScore;
		}
	}
	
	public List<HighScore> getHighScorePartForUser(Long userId, int range, List<HighScore> highScoreTemp) {
		Integer i = -1;
		
		for (HighScore highScore : highScoreTemp) {
			i++;
			if (userId.equals(highScore.getUserId())) {
				break;
			}
		}
		
		final int maxIndex = highScoreTemp.size();
		final int upperLimit = NumberUtils.min(i+(range+1), maxIndex, maxIndex);
		final int lowerLimit = NumberUtils.max(i-(range), 0, 0);
		
		return highScoreTemp.subList(lowerLimit, upperLimit);
	}
}
