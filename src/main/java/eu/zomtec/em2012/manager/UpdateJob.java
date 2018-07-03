package eu.zomtec.em2012.manager;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import eu.zomtec.em2012.domain.Bet;
import eu.zomtec.em2012.domain.Game;
import eu.zomtec.em2012.domain.GameStatus;
import eu.zomtec.em2012.domain.Team;
import eu.zomtec.em2012.score.ScoreCalculator;
import eu.zomtec.em2012.updater.League;
import eu.zomtec.em2012.updater.LeagueService;
import eu.zomtec.em2012.updater.Match;
import eu.zomtec.em2012.updater.MatchStatus;

@Component
public class UpdateJob {
	private static final Long TBD_TEAM_ID = Long.valueOf(-1L);

	private static final Logger LOG = Logger.getLogger(UpdateJob.class);
	
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private ScoreCalculator scoreCalculator;
	
	@Async
	public void processDetailUpdates() {
		try {
			startProcessDetailUpdates();
		} catch (Exception e) {
			LOG.error("Detail updater error!", e);
		}
	}

	public void startProcessDetailUpdates() throws ClientProtocolException, IOException, JSONException, ParseException {
		final TypedQuery<Game> gamesWithoutTeams = Game.findGamesWithUnknownTeams();
		final List<Game> games = gamesWithoutTeams.getResultList();
		
		if (games.isEmpty()) {
			LOG.info("No games without teams!");
		} else {
			LOG.info("Found "+games.size()+" games without teams!");
			
			final League league = leagueService.loadLeague();
			for (Game game : games) {
				if (updateGameDetails(league, game)) {
					LOG.info("Updated game details: "+game);
					game.merge();
				}
			}
		}
	}
	
	public void updateAllGameDetails() throws ClientProtocolException, IOException, JSONException, ParseException {
		LOG.info("Manual complete game update!");
		
		final List<Game> games = Game.findAllGames();
		final League league = leagueService.loadLeague();
		
		for (Game game : games) {
			updateScore(league, game);
			game.merge();
		}
		LOG.info("Finished games!");
	}
	
	public void calculateAllBets() throws ClientProtocolException, IOException, JSONException, ParseException {
		LOG.info("Manual complete bet calculation!");
		
		final List<Game> games = Game.findAllGames();
		for (Game game : games) {
			LOG.info("Recalculating scores for: " + game);
			final TypedQuery<Bet> betsQuery = Bet.findBetsByGame(game);
			final List<Bet> bets = betsQuery.getResultList();
			scoreCalculator.reviewBets(game, bets);
			saveBets(bets);
		}
		LOG.info("Finshed bets!");
	}
	
	@Async
	public void processScoreUpdates() {
		try {
			startProcessScoreUpdates();
		} catch (Exception e) {
			LOG.error("Score updater error!", e);
		}
	}
	
	public void startProcessScoreUpdates() throws ClientProtocolException, IOException, JSONException, ParseException {
		final List<Game> games = findGames();
		if (games.isEmpty()) {
			LOG.info("No games need to be updated!");
			return;
		}
		
		final League league = leagueService.loadLeague();
		LOG.info(games.size()+" games found - loaded new scores: "+league);
		
		for (Game game : games) {
			try {
				boolean recalculateScores = false;

				if (!GameStatus.RECALCULATE.equals(game.getGameStatus())) {
					recalculateScores |= updateScore(league, game);
					game.merge();
				} else {
					recalculateScores = true;
				}

				if (recalculateScores) {
					LOG.info("Recalculating scores for: " + game);
					final TypedQuery<Bet> betsQuery = Bet.findBetsByGame(game);
					final List<Bet> bets = betsQuery.getResultList();
					scoreCalculator.reviewBets(game, bets);
					saveBets(bets);
				}
			} catch (Exception e) {
				LOG.error("Score updater error for game: "+game, e);
			}
		}
	}
	
	private void saveBets(List<Bet> bets) {
		for (Bet bet : bets) {
			bet.merge();
		}
	}

	private boolean updateScore(League league, Game game) {
		final Match match = league.getMatches().get(game.getExternalGameId());
		
		if (match == null) {
			throw new IllegalStateException("No match for "+game+" with external: "+game.getExternalGameId());
		}
		updateGameDetails(match, game);
		validateMatchVsGame(match, game);
		
		boolean change = false;
		change |= !match.getScoreHome().equals(game.getScoreHome());
		change |= !match.getScoreAway().equals(game.getScoreAway());
		final GameStatus newGameStatus = getGameStatusForMatchStatus(match.getStatus());
		change |= !newGameStatus.equals(game.getGameStatus());
		
		if (change) {
			game.setScoreAway(match.getScoreAway());
			game.setScoreHome(match.getScoreHome());
			game.setGameStatus(newGameStatus);
			game.setLastScoreUpdate(new Date());
		}
		
		return change;
	}
	
	public boolean updateGameDetails(League league, Game game) {
		return updateGameDetails(league.getMatches().get(game.getExternalGameId()), game);
	}
	
	public boolean updateGameDetails(Match match, Game game) {
		boolean update = false;
		
		final Long matchTeamIdHome = match.getTeamIdHome();
		if (TBD_TEAM_ID.equals(game.getTeamHome().getExternalTeamId()) && !TBD_TEAM_ID.equals(matchTeamIdHome)) {
			final Team newTeamHome = Team.findTeamsByExternalTeamIdEquals(matchTeamIdHome).getSingleResult();
			game.setTeamHome(newTeamHome);
			update = true;
		}
		
		final Long matchTeamIdAway = match.getTeamIdAway();
		if (TBD_TEAM_ID.equals(game.getTeamAway().getExternalTeamId()) && !TBD_TEAM_ID.equals(matchTeamIdAway)) {
			final Team newTeamAway = Team.findTeamsByExternalTeamIdEquals(matchTeamIdAway).getSingleResult();
			game.setTeamAway(newTeamAway);
			update = true;
		}
		
		final Date startTime = match.getStartTime();
		if (!startTime.equals(game.getKickOff())) {
			game.setKickOff(startTime);
			update = true;
		}
		
		return update;
	}
	
	private void validateMatchVsGame(Match match, Game game) {
		final Long teamAway = game.getTeamAway().getExternalTeamId();
		if (!teamAway.equals(match.getTeamIdAway())) {
			throw new RuntimeException("Team-IDs don't match for Game "+game.getId());
		} 
		
		final Long teamHome = game.getTeamHome().getExternalTeamId();
		if (!teamHome.equals(match.getTeamIdHome())) {
			throw new RuntimeException("Team-IDs don't match for Game "+game.getId());
		} 
	}

	private GameStatus getGameStatusForMatchStatus(MatchStatus matchStatus) {
		switch (matchStatus) {
		case ACTIVE:
			return GameStatus.RUNNING;
		case FINISHED:
			return GameStatus.FINISHED;
		default:
		case SCHEDULED:
			return GameStatus.UPCOMMING;
		}
	}

	private List<Game> findGames() {
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(GregorianCalendar.MINUTE, Game.CLOSE_BET_BEFORE_GAME_IN_MINUTES);
		final TypedQuery<Game> gamesQuery = Game.findGamesByKickOffLessThanEqualsAndNotFinished(calendar.getTime());
		final List<Game> games = gamesQuery.getResultList();
		return games;
	}
}
