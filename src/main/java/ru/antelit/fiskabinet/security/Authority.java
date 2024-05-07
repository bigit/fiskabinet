package ru.antelit.fiskabinet.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

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
