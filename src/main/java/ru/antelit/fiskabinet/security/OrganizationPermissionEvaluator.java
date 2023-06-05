package ru.antelit.fiskabinet.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import ru.antelit.fiskabinet.domain.Organization;

import java.io.Serializable;

public class OrganizationPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (!(targetDomainObject instanceof Organization)){
            return false;
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
