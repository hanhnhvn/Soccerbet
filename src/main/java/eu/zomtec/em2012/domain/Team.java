package eu.zomtec.em2012.domain;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord(finders = { "findTeamsByExternalTeamIdEquals" })
public class Team {

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @ManyToOne
    private TeamGroup teamGroup;

    @NotNull
    @Column(unique = true)
    private Long externalTeamId;
    
    public Team(String name, TeamGroup teamGroup, Long externalTeamId) {
        super();
        this.name = name;
        this.teamGroup = teamGroup;
        this.externalTeamId = externalTeamId;
    }
    
    @Override
    public String toString() {
		return name + "(" + getId() + ")";
    }
}
