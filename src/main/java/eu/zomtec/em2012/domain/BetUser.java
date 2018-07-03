package eu.zomtec.em2012.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.PostUpdate;
import javax.persistence.PreUpdate;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findBetUsersByUsernameEquals", "findBetUsersByUsernameLike" })
public class BetUser {

    @NotNull
    @Size(min = 3, max = 30)
    @Column(unique = true)
    private String username;

    @NotNull
    @Size(max = 100)
    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<BetUserRole> roles = new HashSet<BetUserRole>();

    @NotNull
    private Boolean enabled;

    @NotNull
    @Column(unique = true)
    private String email;

    private Integer money;

    @Override
    public String toString() {
        return username + "(" + getId() + ")";
    }

    
    public static long countBetUsers(String username) {
        if (username == null || username.length() == 0) throw new IllegalArgumentException("The username argument is required");
        final TypedQuery<Long> query = entityManager().createQuery("SELECT COUNT(o) FROM BetUser o WHERE LOWER(o.username) = LOWER(:username)", Long.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }
    
    public static long countBetUsersByEmail(String email) {
    	if (email == null || email.length() == 0) throw new IllegalArgumentException("The email argument is required");
    	final TypedQuery<Long> query = entityManager().createQuery("SELECT COUNT(o) FROM BetUser o WHERE LOWER(o.email) = LOWER(:email)", Long.class);
    	query.setParameter("email", email);
    	return query.getSingleResult();
    }
    
    public static Long countMoney() {
        final TypedQuery<Long> query = entityManager().createQuery("SELECT SUM(o.money) FROM BetUser o", Long.class);
        return query.getSingleResult();
    }
    
    public static long countBetUsersActive() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BetUser o WHERE o.enabled = true", Long.class).getSingleResult();
    }
}
