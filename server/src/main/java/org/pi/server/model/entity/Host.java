package org.pi.server.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.pi.server.model.enums.HostStatusEnum;

/**
* <p>
* 
* </p>
* @author hu1hu
* @since 2024-05-19
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_host")
public class Host {

    /**
    * 主机自增id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * MAC地址
    */
    private String mac;

    /**
    * cpu信息 {cpu_name core frequency}
    */
    private String cpu;

    /**
    * 内存大小
    */
    private Integer memory;

    /**
    * 磁盘大小
    */
    private Integer disk;

    /**
    * 网卡信息
    */
    private String networkCard;

    /**
    * 操作系统
    */
    private String os;

    /**
    * 最后一次上报时间，用于监控存活情况
    */
    private LocalDateTime lastTime;

    /**
    * 状态：非监控  监控 异常
    */
    private HostStatusEnum status;

    /**
    * 主机别名，默认为id
    */
    private String hostname;

}
