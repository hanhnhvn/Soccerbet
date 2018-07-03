package eu.zomtec.em2012.score;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import eu.zomtec.em2012.domain.Bet;
import eu.zomtec.em2012.domain.BetScoreType;
import eu.zomtec.em2012.domain.Game;
import eu.zomtec.em2012.domain.GameStatus;

@Component
public class ScoreCalculator {
	public void reviewBets(Game game, List<Bet> bets) {
		for (Bet bet : bets) {
			reviewBet(game, bet);
		}
	}

	protected void reviewBet(Game game, Bet bet) {
		final GameStatus gameStatus = game.getGameStatus();

		if (GameStatus.UPCOMMING.equals(gameStatus)) {
			bet.setBetStatus(gameStatus.getBetStatus());
		} else {
			final BetScoreType betScoreType = detectBetScoreType(game, bet);
			int factor = game.getGameGroup().getFactor() != null ? game.getGameGroup().getFactor() : 1;
			bet.setScoreType(betScoreType);
			bet.setScore(betScoreType.getPoints() * factor);
			bet.setBetStatus(gameStatus.getBetStatus());
			bet.setLastBetCalculation(new Date());
		}
	}
	
	public BetScoreType detectBetScoreType(Game game, Bet bet) {
		boolean gameDraw = game.getScoreHome().equals(game.getScoreAway()); 
		boolean betDraw = bet.getScoreHome().equals(bet.getScoreAway()); 
		boolean gameHomeWin = game.getScoreHome() > game.getScoreAway();
		boolean gameAwayWin = game.getScoreAway() > game.getScoreHome();
		boolean betHomeWin = bet.getScoreHome() > bet.getScoreAway();
		boolean betAwayWin = bet.getScoreAway() > bet.getScoreHome();
		
		if (game.getScoreHome().equals(bet.getScoreHome()) && game.getScoreAway().equals(bet.getScoreAway())) {
			return BetScoreType.SCORE_MATCH;
		} else if ((game.getScoreHome() - game.getScoreAway()) == (bet.getScoreHome() - bet.getScoreAway())) {
			return BetScoreType.SCORE_DIFFERENCE;
		} else if ((gameDraw && betDraw) || (gameHomeWin && betHomeWin) || (gameAwayWin && betAwayWin)) {
			return BetScoreType.SCORE_WINNER;
		} else {
			return BetScoreType.NOTHING;
		}
	}
}
