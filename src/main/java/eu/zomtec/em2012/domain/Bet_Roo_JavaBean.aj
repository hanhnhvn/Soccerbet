// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package eu.zomtec.em2012.domain;

import eu.zomtec.em2012.domain.Bet;
import eu.zomtec.em2012.domain.BetScoreType;
import eu.zomtec.em2012.domain.BetStatus;
import eu.zomtec.em2012.domain.BetUser;
import eu.zomtec.em2012.domain.Game;
import java.util.Date;

privileged aspect Bet_Roo_JavaBean {
    
    public BetUser Bet.getBetUser() {
        return this.betUser;
    }
    
    public void Bet.setBetUser(BetUser betUser) {
        this.betUser = betUser;
    }
    
    public Game Bet.getGame() {
        return this.game;
    }
    
    public void Bet.setGame(Game game) {
        this.game = game;
    }
    
    public Integer Bet.getScoreHome() {
        return this.scoreHome;
    }
    
    public void Bet.setScoreHome(Integer scoreHome) {
        this.scoreHome = scoreHome;
    }
    
    public Integer Bet.getScoreAway() {
        return this.scoreAway;
    }
    
    public void Bet.setScoreAway(Integer scoreAway) {
        this.scoreAway = scoreAway;
    }
    
    public Date Bet.getLastBetChange() {
        return this.lastBetChange;
    }
    
    public void Bet.setLastBetChange(Date lastBetChange) {
        this.lastBetChange = lastBetChange;
    }
    
    public Date Bet.getLastBetCalculation() {
        return this.lastBetCalculation;
    }
    
    public void Bet.setLastBetCalculation(Date lastBetCalculation) {
        this.lastBetCalculation = lastBetCalculation;
    }
    
    public BetStatus Bet.getBetStatus() {
        return this.betStatus;
    }
    
    public void Bet.setBetStatus(BetStatus betStatus) {
        this.betStatus = betStatus;
    }
    
    public BetScoreType Bet.getScoreType() {
        return this.scoreType;
    }
    
    public void Bet.setScoreType(BetScoreType scoreType) {
        this.scoreType = scoreType;
    }
    
    public Integer Bet.getScore() {
        return this.score;
    }
    
    public void Bet.setScore(Integer score) {
        this.score = score;
    }
    
}
