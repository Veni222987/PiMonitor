package org.pi.server.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

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
@TableName("t_user_role")
public class UserRole {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long userId;

    private Integer roleId;

}
