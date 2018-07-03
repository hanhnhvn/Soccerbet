package eu.zomtec.em2012;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

import eu.zomtec.em2012.domain.BetUser;
import eu.zomtec.em2012.domain.BetUserRole;
import eu.zomtec.em2012.domain.Game;
import eu.zomtec.em2012.domain.GameGroup;
import eu.zomtec.em2012.domain.GameStatus;
import eu.zomtec.em2012.domain.Team;
import eu.zomtec.em2012.domain.TeamGroup;
import eu.zomtec.em2012.updater.League;
import eu.zomtec.em2012.updater.LeagueService;
import eu.zomtec.em2012.updater.Match;
import eu.zomtec.em2012.updater.TeamSfg;

public class Bootstrapper implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = Logger.getLogger(Bootstrapper.class);

    @Autowired
    private LeagueService leagueService;

    private boolean bootstrap = true;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            bootstrap();
        }
    }

    private void bootstrap() {
        if (TeamGroup.findAllTeamGroups().isEmpty()) {
            LOG.info("Bootstrapping started...");
            switch (leagueService.getVersion()) {
            	case 2:
            		LOG.info("Bootstrapping init v2");
            		initV2();
            		break;
            	default:
            		LOG.info("Bootstrapping init default (v1)");
            		init();
            		break;
            }
            
            LOG.info("Bootstrapping finised!");
        } else {
            LOG.info("Bootstrapping skipped!");
        }
    }

    private void init() {
        final BetUserRole roleAdmin = new BetUserRole("ROLE_ADMIN");
        roleAdmin.persist();
        final BetUserRole roleUserAdmin = new BetUserRole("ROLE_USER");
        roleUserAdmin.persist();

        final BetUser admin = new BetUser();
        admin.setUsername("admin");
        admin.setEnabled(true);
        admin.setEmail("admin@xxxxxxxxxx.xxx");
        // "password"
        admin.setPassword("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");

        final HashSet<BetUserRole> roles = new HashSet<BetUserRole>(1);
        roles.add(roleAdmin);
        roles.add(roleUserAdmin);
        admin.setRoles(roles);
        admin.persist();

        /* Bootstrap for WM 2014 */
        TeamGroup teamGroupA = new TeamGroup("A");
        TeamGroup teamGroupB = new TeamGroup("B");
        TeamGroup teamGroupC = new TeamGroup("C");
        TeamGroup teamGroupD = new TeamGroup("D");
        TeamGroup teamGroupE = new TeamGroup("E");
        TeamGroup teamGroupF = new TeamGroup("F");
        TeamGroup teamGroupG = new TeamGroup("G");
        TeamGroup teamGroupH = new TeamGroup("H");
        TeamGroup teamGroupTBD = new TeamGroup("TBD");

        teamGroupA.persist();
        teamGroupB.persist();
        teamGroupC.persist();
        teamGroupD.persist();
        teamGroupE.persist();
        teamGroupF.persist();
        teamGroupG.persist();
        teamGroupH.persist();
        teamGroupTBD.persist();

        Team teamNA = new Team("TBD", teamGroupTBD, -1L);
        Team brasilien = new Team("Brasilien", teamGroupA, 920L);
        Team italien = new Team("Italien", teamGroupD, 924L);
        Team chile = new Team("Chile", teamGroupB, 925L);
        Team kamerun = new Team("Kamerun", teamGroupA, 926L);
        Team frankreich = new Team("Frankreich", teamGroupE, 928L);
        Team spanien = new Team("Spanien", teamGroupB, 932L);
        Team nigeria = new Team("Nigeria", teamGroupF, 933L);
        Team niederlande = new Team("Niederlande", teamGroupB, 936L);
        Team belgien = new Team("Belgien", teamGroupH, 937L);
        Team suedkorea = new Team("Suedkorea", teamGroupH, 938L);
        Team mexiko = new Team("Mexiko", teamGroupA, 939L);
        Team deutschland = new Team("Deutschland", teamGroupG, 940L);
        Team usa = new Team("USA", teamGroupG, 941L);
        Team iran = new Team("Iran", teamGroupF, 943L);
        Team kolumbien = new Team("Kolumbien", teamGroupC, 945L);
        Team england = new Team("England", teamGroupD, 946L);
        Team argentinien = new Team("Argentinien", teamGroupF, 948L);
        Team japan = new Team("Japan", teamGroupC, 949L);
        Team kroatien = new Team("Kroatien", teamGroupA, 951L);
        Team schweiz = new Team("Schweiz", teamGroupE, 1303L);
        Team griechenland = new Team("Griechenland", teamGroupC, 1306L);
        Team portugal = new Team("Portugal", teamGroupG, 1323L);
        Team boszniaHercegovina = new Team("Bosznia-Hercegovina", teamGroupF, 1332L);
        Team russland = new Team("Russland", teamGroupH, 1335L);
        Team ghana = new Team("Ghana", teamGroupG, 1612L);
        Team algerien = new Team("Algerien", teamGroupH, 1621L);
        Team ecuador = new Team("Ecuador", teamGroupE, 3128L);
        Team uruguay = new Team("Uruguay", teamGroupD, 3130L);
        Team australien = new Team("Australien", teamGroupB, 3133L);
        Team costaRica = new Team("Costa Rica", teamGroupD, 3500L);
        Team honduras = new Team("Honduras", teamGroupE, 3560L);
        Team elfenbeinkueste = new Team("Elfenbeinkueste", teamGroupC, 3599L);


        teamNA.persist();
        brasilien.persist();
        italien.persist();
        chile.persist();
        kamerun.persist();
        frankreich.persist();
        spanien.persist();
        nigeria.persist();
        niederlande.persist();
        belgien.persist();
        suedkorea.persist();
        mexiko.persist();
        deutschland.persist();
        usa.persist();
        iran.persist();
        kolumbien.persist();
        england.persist();
        argentinien.persist();
        japan.persist();
        kroatien.persist();
        schweiz.persist();
        griechenland.persist();
        portugal.persist();
        boszniaHercegovina.persist();
        russland.persist();
        ghana.persist();
        algerien.persist();
        ecuador.persist();
        uruguay.persist();
        australien.persist();
        costaRica.persist();
        honduras.persist();
        elfenbeinkueste.persist();


        /*
        Bootstrap for EM 2012

		TeamGroup teamGroupA = new TeamGroup("A");
		TeamGroup teamGroupB = new TeamGroup("B");
		TeamGroup teamGroupC = new TeamGroup("C");
		TeamGroup teamGroupD = new TeamGroup("D");
		TeamGroup teamGroupTBD = new TeamGroup("TBD");
		
		teamGroupA.persist();
		teamGroupB.persist();
		teamGroupC.persist();
		teamGroupD.persist();
		teamGroupTBD.persist();
		
		Team teamNA = new Team("TBD",teamGroupTBD, -1L);
		Team polen = new Team("Polen",teamGroupA, 1317L);
		Team ukraine = new Team("Ukraine",teamGroupD, 1315L);
		Team deutschland = new Team("Deutschland",teamGroupB, 940L);
		Team italien = new Team("Italien",teamGroupC, 924L);
		Team niederlande = new Team("Niederlande",teamGroupB, 936L);
		Team spanien = new Team("Spanien",teamGroupC, 932L);
		Team england = new Team("England",teamGroupD, 946L);
		Team daenemark = new Team("Daenemark",teamGroupB, 931L);
		Team frankreich = new Team("Frankreich",teamGroupD, 928L);
		Team griechenland = new Team("Griechenland",teamGroupA, 1306L);
		Team russland = new Team("Russland",teamGroupA, 1335L);
		Team schweden = new Team("Schweden",teamGroupD, 1318L);
		Team irland = new Team("Irland",teamGroupC, 1327L);
		Team kroatien = new Team("Kroatien",teamGroupC, 951L);
		Team portugal = new Team("Portugal",teamGroupB, 1323L);
		Team tschechien = new Team("Tschechien",teamGroupA, 1333L);
		
		teamNA.persist();
		polen.persist();
		ukraine.persist();
		deutschland.persist();
		italien.persist();
		niederlande.persist();
		spanien.persist();
		england.persist();
		daenemark.persist();
		frankreich.persist();
		griechenland.persist();
		russland.persist();
		schweden.persist();
		irland.persist();
		kroatien.persist();
		portugal.persist();
		tschechien.persist();
        */

        try {
            final League league = leagueService.loadLeague();
            final Map<Long, Match> matchesMap = league.getMatches();
            final Collection<Match> matches = matchesMap.values();

            // Create tournament groups
            final HashSet<String> tournamentGroupNames = new HashSet<String>();
            for (Match match : matches) {
                final String tournamentGroup = match.getTournamentGroup();
                tournamentGroupNames.add(tournamentGroup);
            }

            final HashMap<String, GameGroup> gameGroups = new HashMap<String, GameGroup>();
            for (String tournamentGroup : tournamentGroupNames) {
                GameGroup gameGroup = new GameGroup(tournamentGroup, 0);

                if (org.apache.commons.lang3.StringUtils.startsWith(tournamentGroup, "Gruppe")) {
                    gameGroup.setSortOrder(0);
                    gameGroup.setFactor(1);
                } else if ("Finale".equals(tournamentGroup)) {
                    gameGroup.setSortOrder(3);
                    gameGroup.setFactor(4);
                } else if ("Halbfinale".equals(tournamentGroup)) {
                    gameGroup.setSortOrder(2);
                    gameGroup.setFactor(3);
                } else if ("Viertelfinale".equals(tournamentGroup)) {
                    gameGroup.setSortOrder(1);
                    gameGroup.setFactor(2);
                }

                gameGroup.persist();
                gameGroups.put(tournamentGroup, gameGroup);
            }

            for (Match match : matches) {
                Game game = new Game();
                game.setGameGroup(gameGroups.get(match.getTournamentGroup()));
                game.setGameStatus(GameStatus.getByMatchStatus(match.getStatus()));
                game.setKickOff(match.getStartTime());
                game.setScoreAway(match.getScoreAway());
                game.setScoreHome(match.getScoreHome());
                game.setTeamHome(Team.findTeamsByExternalTeamIdEquals(match.getTeamIdHome()).getSingleResult());
                game.setTeamAway(Team.findTeamsByExternalTeamIdEquals(match.getTeamIdAway()).getSingleResult());
                game.setExternalGameId(match.getMatchId());
                game.persist();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    /*init Bootstrap for WC 2018 */

    private void initV2() {

    	initAdminUser();
        /* Bootstrap for WC 2018 */

        try {

        	final League league = leagueService.loadLeagueV2();
            final Map<Long, Match> matchesMap = league.getMatches();
            final Collection<Match> matches = matchesMap.values();
            
            final Collection<String> groups = league.getGroups().keySet();
            final HashMap<String, TeamGroup> teamGroups = new HashMap<String, TeamGroup>();
            //+++ 26-June-2018
            
            TeamGroup teamGroup = null;
            for (String group : groups) {
            	teamGroup = new TeamGroup(group);
            	teamGroup.persist();
            	teamGroups.put(group, teamGroup);
            }
            TeamGroup teamGroupTBD = new TeamGroup("TBD");
            teamGroupTBD.persist();
            teamGroups.put("TBD", teamGroupTBD);
            
            
            final Map<String, TeamSfg> teamsMap = league.getTeams();
            final Collection<String> teamsCode = teamsMap.keySet();
            eu.zomtec.em2012.domain.Team teamDB = null;
            TeamSfg tmpTeam = null;
            
            for (String teamCode : teamsCode) {
            	
            	tmpTeam = teamsMap.get(teamCode);
            	teamGroup = teamGroups.get(tmpTeam.getGroup_letter());
            	
            	if (tmpTeam != null && teamGroup != null) {
            		LOG.info("Bootstrap init - Create team " + teamCode + " , Country " + tmpTeam.getCountry() + " , Group " + tmpTeam.getGroup_letter() + " | " + tmpTeam.getGroup_id());
                	teamDB = new Team(teamCode, teamGroup, tmpTeam.getId());
                	teamDB.persist();
            	} else {
            		LOG.info("Bootstrap init - Team details not found " + teamCode);
            	}
            }
            
//            Team teamNA = new Team("TBD", teamGroupTBD, -1L);
//            teamNA.persist();
            
            /* 	The api https://worldcup.sfg.io/matches does not include tournament Group so commented out code which generate tournament Group,
             	Please insert data manually
            
            // Create tournament groups
            
            final HashSet<String> tournamentGroupNames = new HashSet<String>();
            for (Match match : matches) {
                final String tournamentGroup = match.getTournamentGroup();
                tournamentGroupNames.add(tournamentGroup);
            }

            final HashMap<String, GameGroup> gameGroups = new HashMap<String, GameGroup>();
            for (String tournamentGroup : tournamentGroupNames) {
                GameGroup gameGroup = new GameGroup(tournamentGroup, 0);

                if (org.apache.commons.lang3.StringUtils.startsWith(tournamentGroup, "Gruppe")) {
                    gameGroup.setSortOrder(0);
                    gameGroup.setFactor(1);
                } else if ("Finale".equals(tournamentGroup)) {
                    gameGroup.setSortOrder(3);
                    gameGroup.setFactor(4);
                } else if ("Halbfinale".equals(tournamentGroup)) {
                    gameGroup.setSortOrder(2);
                    gameGroup.setFactor(3);
                } else if ("Viertelfinale".equals(tournamentGroup)) {
                    gameGroup.setSortOrder(1);
                    gameGroup.setFactor(2);
                }

                gameGroup.persist();
                gameGroups.put(tournamentGroup, gameGroup);
            }*/
            
            // mark all games default is vong loai
            GameGroup gameGroup = new GameGroup("Vong Bang", 0);
            gameGroup.setFactor(1);
            gameGroup.persist();

            for (Match match : matches) {
                Game game = new Game();
                game.setGameGroup(gameGroup);
                game.setGameStatus(GameStatus.getByMatchStatus(match.getStatus()));
                game.setKickOff(match.getStartTime());
                game.setScoreAway(match.getScoreAway());
                game.setScoreHome(match.getScoreHome());
                game.setTeamHome(Team.findTeamsByExternalTeamIdEquals(match.getTeamIdHome()).getSingleResult());
                game.setTeamAway(Team.findTeamsByExternalTeamIdEquals(match.getTeamIdAway()).getSingleResult());
                game.setExternalGameId(match.getMatchId());
                game.persist();
                
                LOG.info("Bootstrap init - Create game ext id: " + match.getMatchId() + " , "
                	+"Between: " + game.getTeamHome()  + " vs. " +  game.getTeamAway() + " , " 
                	+"Score: " + match.getScoreHome() + " - " + match.getScoreHome() + " , "
                	+ "Kick-off time: " +  match.getStartTime() + " , "
                	+ "Status: " + game.getGameStatus() + " , "
                	+ "Game group: " + gameGroup.getName());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

	private void initAdminUser() {
		final BetUserRole roleAdmin = new BetUserRole("ROLE_ADMIN");
        roleAdmin.persist();
        final BetUserRole roleUserAdmin = new BetUserRole("ROLE_USER");
        roleUserAdmin.persist();

        final BetUser admin = new BetUser();
        admin.setUsername("admin");
        admin.setEnabled(true);
        admin.setEmail("admin@xxxxxxxxxx.xxx");
        // "password"
        admin.setPassword("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");

        final HashSet<BetUserRole> roles = new HashSet<BetUserRole>(1);
        roles.add(roleAdmin);
        roles.add(roleUserAdmin);
        admin.setRoles(roles);
        admin.persist();
	}

    public boolean isBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(boolean bootstrap) {
        this.bootstrap = bootstrap;
    }
}
