package org.hibernate.bugs.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(schema = "schema_primary")
public class AnEntity {

    @Id
    private Long id;

    private String name;
}
