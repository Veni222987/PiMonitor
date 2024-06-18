package org.pi.server.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * 统一返回结果
 * @author hu1hu
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> implements Serializable {
    /**
     * 业务转态码
     */
    private int code;

    /**
     * 描述
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public Result(@NotNull ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public Result(@NotNull ResultCode resultCode, T data) {
        this(resultCode.getCode(), resultCode.getMessage(), data);
    }
}
