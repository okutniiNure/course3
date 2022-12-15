package io.swagger.security.auth;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static io.swagger.security.auth.ApplicationUserPermission.*;

/**
 * User role enum
 */
public enum ApplicationUserRole {
    USER(Sets.newHashSet(USER_CREATE, USER_READ, USER_UPDATE, USER_DELETE, ACCOUNT_READ, ACCOUNT_CREATE, ACCOUNT_UPDATE, ACCOUNT_DELETE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    /**
     * @return set of authorities defined for the specific role and role itself as the authority
     */
    public Set<SimpleGrantedAuthority> getGrantedAuthority() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissions;
    }
}
