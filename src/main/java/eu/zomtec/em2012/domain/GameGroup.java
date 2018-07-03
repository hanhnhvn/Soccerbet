package eu.zomtec.em2012.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findGameGroupsByNameEquals" })
public class GameGroup {

    @NotNull
    @Column(unique = true)
    private String name;

    private Integer sortOrder;

    @Value("1")
    private Integer factor;

    public GameGroup(String name, Integer sortOrder) {
        super();
        this.name = name;
        this.sortOrder = sortOrder;
    }

    public static List<eu.zomtec.em2012.domain.GameGroup> findAllGameGroupsBySortOrder() {
        return entityManager().createQuery("SELECT o FROM GameGroup o ORDER BY o.sortOrder ASC, o.name ASC", GameGroup.class).getResultList();
    }
}
