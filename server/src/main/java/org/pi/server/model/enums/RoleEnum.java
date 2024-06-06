package org.pi.server.model.enums;

import lombok.Getter;

/**
 * @author hu1hu
 */

@Getter
public enum RoleEnum {
    ADMIN("admin"),
    USER("user");

    final String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

}
