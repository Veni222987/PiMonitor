package org.pi.server.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
    private String avatar;

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

}
