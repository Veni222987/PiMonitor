package org.pi.server.model.enums;

public enum UserStatusEnum {
    ACTIVE("active"),
    LOCKED("locked");

    private String status;

    UserStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
