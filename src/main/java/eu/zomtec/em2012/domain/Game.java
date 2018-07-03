package eu.zomtec.em2012.domain;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findGamesByGameGroup", "findGamesByKickOffLessThanEquals" })
public class Game {

	@Transient
    public static final int CLOSE_BET_BEFORE_GAME_IN_MINUTES = 10;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date kickOff;

    @NotNull
    @ManyToOne
    private Team teamHome;

    @NotNull
    @ManyToOne
    private Team teamAway;

    private Integer scoreHome;

    private Integer scoreAway;

    @NotNull
    @Enumerated
    private GameStatus gameStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date lastScoreUpdate;

    @NotNull
    @ManyToOne
    private GameGroup gameGroup;

    @NotNull
    @Column(unique=true)
    private Long externalGameId;

    public static TypedQuery<Game> findGamesByGameGroupOrderByStart(GameGroup gameGroup) {
        if (gameGroup == null) throw new IllegalArgumentException("The gameGroup argument is required");
        EntityManager em = entityManager();
        TypedQuery<Game> q = em.createQuery("SELECT o FROM Game AS o WHERE o.gameGroup = :gameGroup ORDER BY o.gameStatus DESC, o.kickOff ASC", Game.class);
        q.setParameter("gameGroup", gameGroup);
        return q;
    }

    @Transient
    public boolean isBetOpen() {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.MINUTE, CLOSE_BET_BEFORE_GAME_IN_MINUTES);
        return GameStatus.UPCOMMING.equals(gameStatus) && calendar.getTime().before(kickOff);
    }

    public static TypedQuery<Game> findGamesByKickOffLessThanEqualsAndNotFinished(Date kickOff) {
        if (kickOff == null) throw new IllegalArgumentException("The kickOff argument is required");
        EntityManager em = Game.entityManager();
        TypedQuery<Game> q = em.createQuery("SELECT o FROM Game AS o WHERE o.kickOff <= :kickOff AND o.gameStatus != :gameStatus", Game.class);
        q.setParameter("kickOff", kickOff);
        q.setParameter("gameStatus", GameStatus.FINISHED);
        return q;
    }

    public static TypedQuery<Game> findGamesWithUnknownTeams() {
    	final Team tbdTeam = Team.findTeamsByExternalTeamIdEquals(-1L).getSingleResult();
        EntityManager em = Game.entityManager();
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.DAY_OF_YEAR, 10);
        TypedQuery<Game> q = em.createQuery("SELECT o FROM Game AS o WHERE o.kickOff < :filter AND (o.teamHome = :team OR o.teamAway = :team) ", Game.class);
        q.setParameter("filter", calendar.getTime());
        q.setParameter("team", tbdTeam);
        return q;
    }

    
    public static TypedQuery<Game> findNextGames(int i) {
    	EntityManager em = Game.entityManager();
    	TypedQuery<Game> q = em.createQuery("SELECT o FROM Game AS o WHERE o.kickOff > :kickOff ORDER BY o.kickOff ASC", Game.class);
    	q.setParameter("kickOff", new Date());
    	q.setMaxResults(i);
    	return q;
    }
    
    public static TypedQuery<Game> findPastGames(GameStatus status, int i) {
    	EntityManager em = Game.entityManager();
    	TypedQuery<Game> q = em.createQuery("SELECT o FROM Game AS o WHERE o.gameStatus = :status AND o.kickOff < :kickOff ORDER BY o.kickOff DESC", Game.class);
    	q.setParameter("kickOff", new Date());
    	q.setParameter("status", status);
    	q.setMaxResults(i);
    	return q;
    }

    @Override
	public String toString() {
		return "Game "+getId()+" [teamHome=" + teamHome + ", teamAway=" + teamAway
				+ ", externalGameId=" + externalGameId + "]";
	}
}
