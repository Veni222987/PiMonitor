package org.pi.server.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_team")
public class Team {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private LocalDateTime createTime;
    private String name;
    private Long owner;
    private String token;
}
