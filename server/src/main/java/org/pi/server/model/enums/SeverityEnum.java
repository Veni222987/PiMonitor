package org.pi.server.model.enums;

import lombok.Getter;

/**
 * @author hu1hu
 * @date 2024/6/13 17:22
 * @description 通知类型
 */
@Getter
public enum SeverityEnum {
        CRITICAL("critical"),
        WARNING("warning"),
        INFO("info");
        private final String severity;
        SeverityEnum(String severity) {
            this.severity = severity;
        }
}
