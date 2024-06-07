package org.pi.server.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * 统一响应工具类
 * @author hu1hu
 */
public class ResultUtils {

    /**
     * 成功
     */
    @NotNull
    @Contract(" -> new")
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS);
    }

    /**
     * 成功
     */
    @NotNull
    @Contract("_ -> new")
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS, data);
    }

    /**
     * 失败
     */
    @NotNull
    @Contract("_ -> new")
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode);
    }

    /**
     * 失败
     */
    @NotNull
    @Contract("_, _ -> new")
    public static <T> Result<T> error(ResultCode resultCode, T data) {
        return new Result<>(resultCode, data);
    }
}
