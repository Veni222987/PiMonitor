package org.pi.server.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.pi.server.model.enums.UserStatusEnum;

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
@TableName("t_user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 用户名
    */
    private String username;

    /**
    * 头像，存储url 使用oss存储
    */
    private String avater;

    /**
    * 电话号码
    */
    private String phoneNumber;

    /**
    * 邮箱
    */
    private String email;

    /**
    * 密码
    */
    private String password;

    /**
    * 角色id
    */
    private String roleId;

    /**
    * 状态
    */
    private UserStatusEnum status;

    /**
    * 最后登录时间
    */
    private LocalDateTime lastLoginTime;

    /**
    * 记录用户登录尝试次数，用于安全监控
    */
    private Byte loginAttempts;

}
