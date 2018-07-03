package eu.zomtec.em2012.updater;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONException;

public class LeagueService {
	
	private static final Logger LOG = Logger.getLogger(LeagueService.class);
	
	private LeagueParser leagueParser = new LeagueParser();
	
	private String feedUrl = "http://worldcup.sfg.io/matches"; 
			//"http://nak.zomtec.eu/kicker-demo.json";
	private String teamFeedUrl = "http://worldcup.sfg.io/teams/";
	
	private int version = 1;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getTeamFeedUrl() {
		return teamFeedUrl;
	}

	public void setTeamFeedUrl(String teamFeedUrl) {
		this.teamFeedUrl = teamFeedUrl;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	public String loadFeed() throws ClientProtocolException, IOException {
		final HttpClient httpClient = new DefaultHttpClient();
		
        try {
            final HttpGet httpget = new HttpGet(feedUrl);
            final ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String responseBody = httpClient.execute(httpget, responseHandler);
            return responseBody;
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
        	httpClient.getConnectionManager().shutdown();
        }
	}

	public String loadTeamFeed() throws ClientProtocolException, IOException {
		final HttpClient httpClient = new DefaultHttpClient();
		
        try {
            final HttpGet httpget = new HttpGet(teamFeedUrl);
            final ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String responseBody = httpClient.execute(httpget, responseHandler);
            return responseBody;
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
        	httpClient.getConnectionManager().shutdown();
        }
	}
	
	public League loadLeague() throws ClientProtocolException, IOException, JSONException, ParseException {
		
		League league = new League();
		LOG.info("League Service version " + version);
		switch (version) {
			case 2 :
				LOG.info("League Service loadLeagueV2()");
				league = loadLeagueV2();
				break;
			
			default :
				LOG.info("League Service loadLeague()");
				final String feed = loadFeed();
				league = leagueParser.parse(feed);
				break;
				
		}
		
		return league;
	}
	
	/*
	 * World Cup 2018 using api
	 * http://worldcup.sfg.io/matches
	 * http://worldcup.sfg.io/teams/
	 */
	public League loadLeagueV2() throws ClientProtocolException, IOException, JSONException, ParseException {

		final String teamsJson = loadTeamFeed();//loadTeamsJson();
		final String matchesJson = loadFeed();
		return leagueParser.parseV2(teamsJson, matchesJson);
	}
	
	public String loadTeamsJson() {
		String jsonText = null;
		try {
			final InputStream inputStream = LeagueService.class.getClassLoader().getResourceAsStream("./teams.json");
			jsonText = IOUtils.toString(inputStream);
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return jsonText;
	}
	
}
