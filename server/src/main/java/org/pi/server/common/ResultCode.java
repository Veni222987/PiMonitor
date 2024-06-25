package org.pi.server.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义状态码,前三位为HTTP转态码，后两位为扩展状态码
 * @author hu1hu
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(20000, "成功"),

    PARAMS_ERROR(40000, "请求参数错误"),
    TYPE_NOT_SUPPORT(40001, "类型不支持"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    REQUEST_TOO_FREQUENT(40200, "请求次数过于频繁"),
    VERIFY_CODE_ERROR(40201, "验证码错误"),
    PASSWORD_ERROR(40200, "密码错误"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    TOKEN_EXPIRED(40202, "token过期"),
    REPEAT_OPERATION(40203, "重复操作"),

    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 业务状态码
     */
    private final int code;

    /**
     * 描述信息
     */
    private final String message;

}
