package io.swagger.security.auth;

/**
 * User permission enum
 */
public enum ApplicationUserPermission {
    ACCOUNT_CREATE("account:create"),
    ACCOUNT_READ("account:read"),
    ACCOUNT_UPDATE("account:update"),
    ACCOUNT_DELETE("account:delete"),
    USER_CREATE("patient:create"),
    USER_READ("patient:read"),
    USER_UPDATE("patient:update"),
    USER_DELETE("patient:delete");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
