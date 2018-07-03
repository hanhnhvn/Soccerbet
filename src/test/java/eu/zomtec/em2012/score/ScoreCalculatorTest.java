package eu.zomtec.em2012.score;

import junit.framework.Assert;

import org.junit.Test;

import eu.zomtec.em2012.domain.Bet;
import eu.zomtec.em2012.domain.BetScoreType;
import eu.zomtec.em2012.domain.BetStatus;
import eu.zomtec.em2012.domain.Game;
import eu.zomtec.em2012.domain.GameGroup;
import eu.zomtec.em2012.domain.GameStatus;

public class ScoreCalculatorTest {
	private ScoreCalculator scoreCalculator = new ScoreCalculator();
	
	
	@Test
	public void testProblem1() {
		final Game game = new Game();
		game.setScoreHome(1);
		game.setScoreAway(3);
		game.setGameStatus(GameStatus.RUNNING);
		game.setGameGroup(new GameGroup());
		
		final Bet bet = new Bet();
		bet.setScoreHome(1);
		bet.setScoreAway(1);
		
		scoreCalculator.reviewBet(game, bet);
		Assert.assertEquals(BetScoreType.NOTHING, bet.getScoreType());
		Assert.assertEquals(Integer.valueOf(BetScoreType.NOTHING.getPoints()), bet.getScore());
		Assert.assertEquals(BetStatus.SCORE_TEMP, bet.getBetStatus());
	}
	
	@Test
	public void testBetUpdate() {
		final Game game = new Game();
		game.setScoreHome(1);
		game.setScoreAway(1);
		game.setGameStatus(GameStatus.RUNNING);
		game.setGameGroup(new GameGroup());
		
		final Bet bet = new Bet();
		bet.setScoreHome(1);
		bet.setScoreAway(1);
		
		scoreCalculator.reviewBet(game, bet);
		Assert.assertEquals(BetScoreType.SCORE_MATCH, bet.getScoreType());
		Assert.assertEquals(Integer.valueOf(BetScoreType.SCORE_MATCH.getPoints()), bet.getScore());
		Assert.assertEquals(BetStatus.SCORE_TEMP, bet.getBetStatus());

		game.setGameStatus(GameStatus.FINISHED);
		scoreCalculator.reviewBet(game, bet);
		Assert.assertEquals(BetStatus.SCORE_FINAL, bet.getBetStatus());
		
		game.setGameStatus(GameStatus.RECALCULATE);
		scoreCalculator.reviewBet(game, bet);
		Assert.assertEquals(BetStatus.SCORE_FINAL, bet.getBetStatus());

		game.setGameStatus(GameStatus.UPCOMMING);
		scoreCalculator.reviewBet(game, bet);
		Assert.assertEquals(BetStatus.OPEN, bet.getBetStatus());
	}
	
	@Test
	public void testBetUpdateFactor() {
		final Game game = new Game();
		game.setScoreHome(1);
		game.setScoreAway(1);
		game.setGameStatus(GameStatus.RUNNING);
		final GameGroup gameGroup = new GameGroup();
		gameGroup.setFactor(4);
		game.setGameGroup(gameGroup);
		
		final Bet bet = new Bet();
		bet.setScoreHome(1);
		bet.setScoreAway(1);
		
		scoreCalculator.reviewBet(game, bet);
		Assert.assertEquals(BetScoreType.SCORE_MATCH, bet.getScoreType());
		Assert.assertEquals(Integer.valueOf(BetScoreType.SCORE_MATCH.getPoints()*4), bet.getScore());
		Assert.assertEquals(BetStatus.SCORE_TEMP, bet.getBetStatus());
	}
	
	@Test
	public void testDraw() {
		final Game game = new Game();
		game.setScoreHome(1);
		game.setScoreAway(1);
		game.setGameStatus(GameStatus.RUNNING);
		game.setGameGroup(new GameGroup());
		
		final Bet bet = new Bet();

		bet.setScoreHome(1);
		bet.setScoreAway(1);
		Assert.assertEquals(BetScoreType.SCORE_MATCH, scoreCalculator.detectBetScoreType(game, bet));
		
		bet.setScoreHome(2);
		bet.setScoreAway(2);
		Assert.assertEquals(BetScoreType.SCORE_DIFFERENCE, scoreCalculator.detectBetScoreType(game, bet));
		
		bet.setScoreHome(0);
		bet.setScoreAway(1);
		Assert.assertEquals(BetScoreType.NOTHING, scoreCalculator.detectBetScoreType(game, bet));
		
		bet.setScoreHome(1);
		bet.setScoreAway(0);
		Assert.assertEquals(BetScoreType.NOTHING, scoreCalculator.detectBetScoreType(game, bet));
	}
	
	@Test
	public void testHome() {
		final Game game = new Game();
		game.setScoreHome(5);
		game.setScoreAway(1);
		game.setGameGroup(new GameGroup());
		
		final Bet bet = new Bet();
		
		bet.setScoreHome(5);
		bet.setScoreAway(1);
		Assert.assertEquals(BetScoreType.SCORE_MATCH, scoreCalculator.detectBetScoreType(game, bet));
		
		bet.setScoreHome(6);
		bet.setScoreAway(2);
		Assert.assertEquals(BetScoreType.SCORE_DIFFERENCE, scoreCalculator.detectBetScoreType(game, bet));
		
		bet.setScoreHome(1);
		bet.setScoreAway(0);
		Assert.assertEquals(BetScoreType.SCORE_WINNER, scoreCalculator.detectBetScoreType(game, bet));
		
		bet.setScoreHome(0);
		bet.setScoreAway(1);
		Assert.assertEquals(BetScoreType.NOTHING, scoreCalculator.detectBetScoreType(game, bet));
		
		bet.setScoreHome(2);
		bet.setScoreAway(6);
		Assert.assertEquals(BetScoreType.NOTHING, scoreCalculator.detectBetScoreType(game, bet));
	}
	
	@Test
	public void testAway() {
		final Game game = new Game();
		game.setScoreHome(4);
		game.setScoreAway(6);
		game.setGameGroup(new GameGroup());
		
		final Bet bet = new Bet();
		
		bet.setScoreHome(4);
		bet.setScoreAway(6);
		Assert.assertEquals(BetScoreType.SCORE_MATCH, scoreCalculator.detectBetScoreType(game, bet));
		
		bet.setScoreHome(5);
		bet.setScoreAway(7);
		Assert.assertEquals(BetScoreType.SCORE_DIFFERENCE, scoreCalculator.detectBetScoreType(game, bet));
		
		bet.setScoreHome(0);
		bet.setScoreAway(1);
		Assert.assertEquals(BetScoreType.SCORE_WINNER, scoreCalculator.detectBetScoreType(game, bet));
		
		bet.setScoreHome(1);
		bet.setScoreAway(0);
		Assert.assertEquals(BetScoreType.NOTHING, scoreCalculator.detectBetScoreType(game, bet));
		
		bet.setScoreHome(6);
		bet.setScoreAway(4);
		Assert.assertEquals(BetScoreType.NOTHING, scoreCalculator.detectBetScoreType(game, bet));
	}
}
