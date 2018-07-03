package eu.zomtec.em2012.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.persistence.TypedQuery;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.zomtec.em2012.domain.Bet;
import eu.zomtec.em2012.domain.BetUser;
import eu.zomtec.em2012.domain.Game;
import eu.zomtec.em2012.domain.GameStatus;
import eu.zomtec.em2012.domain.News;
import eu.zomtec.em2012.manager.BetManager;
import eu.zomtec.em2012.score.HighScore;
import eu.zomtec.em2012.score.HighScoreService;

@RequestMapping("/public/**")
@Controller
public class PublicController {
	
	private static final String REGEX_USERNAME = "^[a-zA-Z0-9-_.]{3,20}$";
	
	@Autowired
	private HighScoreService highScoreService;
	
	@Autowired
	private BetManager betManager;
	
	@RequestMapping(value={"/","/news"})
	public String news(ModelMap modelMap, Principal principal) {
		final List<News> news = News.findNewsEntries(10);
		modelMap.put("news", news);
		
		final List<HighScore> highScores = highScoreService.getHighScoreTemp();
		
		final BetUser user = getUser(principal);
		if (user != null) {
			modelMap.put("scores_temp_my", highScoreService.getHighScorePartForUser(user.getId(), 2, new ArrayList<HighScore>(highScores)));
			
	    	final List<Game> runningGames = Game.findPastGames(GameStatus.RUNNING, 4).getResultList();
	    	final List<Game> finishedGames = Game.findPastGames(GameStatus.FINISHED, 4).getResultList();
	    	
	    	final List<Game> allGames = new ArrayList<Game>(runningGames.size()+finishedGames.size());
	    	allGames.addAll(runningGames);
	    	allGames.addAll(finishedGames);
	    	modelMap.put("games",allGames);
	    	
	    	final HashMap<Long, Bet> bets = betManager.getBetsForGames(user, allGames);
	    	modelMap.put("bets", bets);
		}

		modelMap.put("scores_temp", highScores.size() > 5 ? highScores.subList(0, 5) : highScores);
		
		
		modelMap.put("money", BetUser.countMoney());
		modelMap.put("usersActive", BetUser.countBetUsersActive());
		modelMap.put("usersTotal", BetUser.countBetUsers());
		
		return "public/news";
	}
	
	private BetUser getUser(Principal principal) {
		if (principal == null || StringUtils.isBlank(principal.getName())) {
			return null;
		}
		
		final TypedQuery<BetUser> userQuery = BetUser.findBetUsersByUsernameEquals(principal.getName());
    	final BetUser user = userQuery.getSingleResult();
		return user;
	}
	
    @RequestMapping(value="/scores")
    public String scores(ModelMap modelMap) {
    	return "public/scores";
    }
    
    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String register(ModelMap modelMap) {
    	return "public/register";
    }
    
    @RequestMapping(value="/scores/final")
    public String finalScores(ModelMap modelMap) {
    	final List<HighScore> finalScores = highScoreService.getHighScoreFinal(1000);
    	modelMap.put("scores_final", finalScores);
    	
        return "public/score-board";
    }
    
    @RequestMapping(value="/scores/temp")
    public String tempScores(ModelMap modelMap) {
    	final List<HighScore> tempScores = highScoreService.getHighScoreTemp(1000);
    	modelMap.put("scores_temp", tempScores);
    	
        return "public/score-board";
    }
    
    
    @RequestMapping(value="/dashboard")
    public String dashboard(ModelMap modelMap, @RequestParam(defaultValue="5") Integer games, @RequestParam(defaultValue="1000") Integer scores,
    		@RequestParam(defaultValue="1") Integer header, @RequestParam(defaultValue="0") Integer reload) {
    	modelMap.put("showHeader",header > 0);
		modelMap.put("reload", reload);

    	final List<HighScore> tempScores = highScoreService.getHighScoreTemp(scores);
    	modelMap.put("scores_temp", tempScores);
    	
    	final List<Game> nextGames = Game.findNextGames(games).getResultList();
    	final List<Game> runningGames = Game.findPastGames(GameStatus.RUNNING, games).getResultList();
    	final List<Game> finishedGames = Game.findPastGames(GameStatus.FINISHED, games).getResultList();
    	modelMap.put("runningGames",runningGames);
    	modelMap.put("finishedGames",finishedGames);
    	modelMap.put("nextGames",nextGames);
    	
    	if (!nextGames.isEmpty()) {
    		final Game game = nextGames.get(0);
    		final Date kickOff = game.getKickOff();
    		modelMap.put("nextGame",game);
    		modelMap.put("nextGameTimeStamp", getCountDownString(kickOff));
    	}
    	
        return "public/dashboard";
    }

	private String getCountDownString(final Date kickOff) {
		final long time = kickOff.getTime() - System.currentTimeMillis();
		
		final long days = TimeUnit.MILLISECONDS.toDays(time);
		
		final long hoursAbsolut = TimeUnit.MILLISECONDS.toHours(time);
		final long hours = hoursAbsolut - TimeUnit.DAYS.toHours(days);
		
		final long minutesAbsolut = TimeUnit.MILLISECONDS.toMinutes(time);
		final long minutes = minutesAbsolut - TimeUnit.HOURS.toMinutes(hoursAbsolut);
		
		final long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(minutesAbsolut);
		
		final String timeString = StringUtils.leftPad(String.valueOf(days), 2, "0") + ":" + StringUtils.leftPad(String.valueOf(hours), 2, "0")
				+":"+StringUtils.leftPad(String.valueOf(minutes), 2, "0")
				+":"+StringUtils.leftPad(String.valueOf(seconds), 2, "0");
		
		return timeString;
	}
    
    @RequestMapping(value="/register", method = RequestMethod.POST)
    @ResponseBody
    public String registerPost(ModelMap modelMap, @RequestParam String username, @RequestParam String password, @RequestParam String password2, @RequestParam String email) {
    	username = StringUtils.trim(username);
    	
    	final StringBuilder sb = new StringBuilder();
    	
    	if (!Pattern.matches(REGEX_USERNAME, username)) {
    		sb.append("<li>Username is invalid (Minimum length: 3, Allowed characters: A-Z, 0-9, -, _, .)</li>");
    	} else if (BetUser.countBetUsers(username) > 0) {
    		sb.append("<li>Username already in use</li>");
    	} 
    	
    	if (StringUtils.isBlank(password) || password.length() < 3) {
    		sb.append("<li>Password must be at least 3 characters long</li>");
    	} else if (!password.equals(password2)) {
    		sb.append("<li>Passwords do not match</li>");
    	}
    	
    	if (StringUtils.isBlank(email) || !Pattern.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", email)) {
    		sb.append("<li>Email invalid</li>");
    	} else if (BetUser.countBetUsersByEmail(email) > 0) {
    		sb.append("<li>Email already in use</li>");
    	}
    	
    	final String errors = sb.toString();
    	
		if (StringUtils.isNotBlank(errors)) {
			return errors;
		} else {
			BetUser betUser = new BetUser();
			betUser.setUsername(username);
			betUser.setPassword(DigestUtils.sha256Hex(password));
			betUser.setEnabled(false);
			betUser.setEmail(email);
			betUser.setMoney(0);
			betUser.persist();
			
			return "success";
		}
    }
}