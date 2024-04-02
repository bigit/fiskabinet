package ru.antelit.fiskabinet.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(schema = "security", name = "authorities")
@IdClass(AuthorityId.class)
public class Authority {
    @Id
    Integer userId;
    @Id
    Integer organizationId;

    @Column
    String authority;

}
