// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package eu.zomtec.em2012.domain;

import eu.zomtec.em2012.domain.Team;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Team_Roo_Finder {
    
    public static TypedQuery<Team> Team.findTeamsByExternalTeamIdEquals(Long externalTeamId) {
        if (externalTeamId == null) throw new IllegalArgumentException("The externalTeamId argument is required");
        EntityManager em = Team.entityManager();
        TypedQuery<Team> q = em.createQuery("SELECT o FROM Team AS o WHERE o.externalTeamId = :externalTeamId", Team.class);
        q.setParameter("externalTeamId", externalTeamId);
        return q;
    }
    
}