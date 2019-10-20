package org.hibernate.bugs.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(schema = "schema_secondary")
public class EntityB {

    @Id
    private Long id;

    @ManyToMany
    private List<EntityA> entityA;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<EntityA> getEntityA() {
        return entityA;
    }

    public void setEntityA(List<EntityA> entityA) {
        this.entityA = entityA;
    }
}
