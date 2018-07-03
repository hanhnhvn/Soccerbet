package eu.zomtec.em2012.score;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;


@ContextConfiguration(locations = { "/META-INF/spring/applicationContext.xml" })
public class HighScoreServiceTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private HighScoreService highScoreService;
	
	@Test 
	public void testIntegration() {
		Assert.assertTrue(true);
		final List<HighScore> scores = highScoreService.getHighScoreTemp(5);
		Assert.assertNotNull(scores);
		final List<HighScore> scores2 = highScoreService.getHighScoreFinal(5);
		Assert.assertNotNull(scores2);
	}
	
	@Test
	public void testHighScorePartForUser() {
		List<HighScore> highScoreTemp = new ArrayList<HighScore>();
		highScoreTemp.add(new HighScore(1, "Tester1", 5, 1L));
		highScoreTemp.add(new HighScore(2, "Tester2", 4, 2L));
		highScoreTemp.add(new HighScore(2, "Tester3", 4, 3L));
		highScoreTemp.add(new HighScore(3, "Tester4", 3, 4L));
		highScoreTemp.add(new HighScore(2, "Tester5", 2, 5L));
		
		List<HighScore> highScorePartForUser = highScoreService.getHighScorePartForUser(3L, 1, highScoreTemp);
		Assert.assertEquals(3, highScorePartForUser.size());
		Assert.assertEquals(2L, highScorePartForUser.get(0).getUserId().longValue());
		Assert.assertEquals(3L, highScorePartForUser.get(1).getUserId().longValue());
		Assert.assertEquals(4L, highScorePartForUser.get(2).getUserId().longValue());
		
		highScorePartForUser = highScoreService.getHighScorePartForUser(5L, 1, highScoreTemp);
		Assert.assertEquals(2, highScorePartForUser.size());
		Assert.assertEquals(4L, highScorePartForUser.get(0).getUserId().longValue());
		Assert.assertEquals(5L, highScorePartForUser.get(1).getUserId().longValue());
		
		highScorePartForUser = highScoreService.getHighScorePartForUser(1L, 1, highScoreTemp);
		Assert.assertEquals(2, highScorePartForUser.size());
		Assert.assertEquals(1L, highScorePartForUser.get(0).getUserId().longValue());
		Assert.assertEquals(2L, highScorePartForUser.get(1).getUserId().longValue());
		
		highScorePartForUser = highScoreService.getHighScorePartForUser(1L, 20, highScoreTemp);
		Assert.assertEquals(5, highScorePartForUser.size());
	}
	
	@Test
	public void testHighScorePartForUser2() {
		List<HighScore> highScoreTemp = new ArrayList<HighScore>();
		
		highScoreTemp.add(new HighScore(1, "Tester1", 5, 1L));
		highScoreTemp.add(new HighScore(1, "Tester2", 5, 2L));
		highScoreTemp.add(new HighScore(1, "Tester3", 5, 3L));
		highScoreTemp.add(new HighScore(1, "Tester4", 5, 4L));
		highScoreTemp.add(new HighScore(1, "Tester5", 5, 5L));
		highScoreTemp.add(new HighScore(1, "Tester6", 5, 6L));
		highScoreTemp.add(new HighScore(1, "Tester7", 5, 7L));
		highScoreTemp.add(new HighScore(1, "Tester8", 5, 8L));
		highScoreTemp.add(new HighScore(1, "Tester9", 5, 9L));
		
		List<HighScore> highScorePartForUser = highScoreService.getHighScorePartForUser(6L, 2, highScoreTemp);
		Assert.assertEquals(5, highScorePartForUser.size());
		Assert.assertEquals(4L, highScorePartForUser.get(0).getUserId().longValue());
		Assert.assertEquals(5L, highScorePartForUser.get(1).getUserId().longValue());
		Assert.assertEquals(6L, highScorePartForUser.get(2).getUserId().longValue());
		Assert.assertEquals(7L, highScorePartForUser.get(3).getUserId().longValue());
		Assert.assertEquals(8L, highScorePartForUser.get(4).getUserId().longValue());
	}
}