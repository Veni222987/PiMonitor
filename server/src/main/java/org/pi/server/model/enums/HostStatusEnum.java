package org.pi.server.model.enums;


import lombok.Getter;

/**
 * 状态枚举
 * @see <a href="https://baomidou.com/guides/auto-convert-enum/">auto-convert-enum</a>
 * @author huhuayu
 */
@Getter
public enum HostStatusEnum {

    MONITORING("monitoring"),
    UNMONITORED("unmonitored"),
    UNKNOWN("unknown");
    private final String status;

    HostStatusEnum(String status) {
        this.status = status;
    }

}
