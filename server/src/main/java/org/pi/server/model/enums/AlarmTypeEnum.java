package org.pi.server.model.enums;

import lombok.Getter;

/**
 * @author hu1hu
 * @date 2024/6/13 17:20
 * @description 报警类型
 */
@Getter
public enum AlarmTypeEnum {

    CPU("cpu"),
    MEMORY("mem"),
    DISK("disk"),
    NETWORK("network"),
    PROCESS("tcp"),
    CUSTOM("host");
    private final String type;

    AlarmTypeEnum(String type) {
        this.type = type;
    }
}
