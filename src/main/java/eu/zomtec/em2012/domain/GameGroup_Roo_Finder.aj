// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package eu.zomtec.em2012.domain;

import eu.zomtec.em2012.domain.GameGroup;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect GameGroup_Roo_Finder {
    
    public static TypedQuery<GameGroup> GameGroup.findGameGroupsByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = GameGroup.entityManager();
        TypedQuery<GameGroup> q = em.createQuery("SELECT o FROM GameGroup AS o WHERE o.name = :name", GameGroup.class);
        q.setParameter("name", name);
        return q;
    }
    
}