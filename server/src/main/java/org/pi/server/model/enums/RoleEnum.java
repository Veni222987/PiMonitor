package org.pi.server.model.enums;

public enum RoleEnum {
    ADMIN("admin"),
    USER("user");

    String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
