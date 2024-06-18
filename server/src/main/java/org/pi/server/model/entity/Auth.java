package org.pi.server.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 * @author hu1hu
 * @since 2024-05-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_auth")
public class Auth {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String openId;
    private String type;
    private LocalDateTime bindTime;
    private String name;
    private String avatar;
}
