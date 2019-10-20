package org.hibernate.bugs.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(schema = "schema_secondary")
public class EntityA {

    @Id
    private Long id;

    @ManyToMany
    private List<EntityB> entityB;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<EntityB> getEntityB() {
        return entityB;
    }

    public void setEntityB(List<EntityB> entityB) {
        this.entityB = entityB;
    }
}
