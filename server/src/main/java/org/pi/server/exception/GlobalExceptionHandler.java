package org.pi.server.exception;


import lombok.extern.slf4j.Slf4j;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author hu1hu
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> runtimeExceptionHandler(RuntimeException e) {
        if (e instanceof IllegalArgumentException) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        }
        log.error("RuntimeException", e);
        return ResultUtils.error(ResultCode.SYSTEM_ERROR);
    }
}
