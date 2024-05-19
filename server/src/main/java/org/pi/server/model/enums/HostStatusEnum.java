package org.pi.server.model.enums;


/**
 * 状态枚举
 * https://baomidou.com/guides/auto-convert-enum/
 */
public enum HostStatusEnum {

    MONITORING("monitoring"),
    UNMONITORED("unmonitored"),
    UNKNOWN("unknown");
    private String status;

    HostStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
