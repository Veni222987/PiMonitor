package org.pi.server.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pi.server.model.enums.AlarmTypeEnum;
import org.pi.server.model.enums.SeverityEnum;

import java.time.LocalDateTime;

/**
 * @author hu1hu
 * @date 2024/6/13 17:17
 * @description 数据库报警规则表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_alarm")
public class Alarm {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 主机id
     */
    private Long hostId;
    /**
     * 报警类型
     */
    private AlarmTypeEnum alarmType;
    /**
     * 阈值
     */
    private Double thresholdValue;
    /**
     * 通知类型
     */
    private SeverityEnum severity;
    /**
     * 持续时间
     */
    private Long duration;
    /**
     * 通知账号
     */
    private String notificationAccount;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
