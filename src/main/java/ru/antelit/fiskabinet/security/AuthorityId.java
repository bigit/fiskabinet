package ru.antelit.fiskabinet.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthorityId implements Serializable {
    private Integer userId;
    private Integer organizationId;
}
