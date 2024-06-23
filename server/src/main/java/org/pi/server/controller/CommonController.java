package org.pi.server.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.service.*;
import org.pi.server.utils.AliSmsUtils;
import org.pi.server.utils.CodeUtils;
import org.pi.server.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hu1hu
 */
@Slf4j
@RestController
@RequestMapping("/v1/common")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommonController {
    private final AliSmsUtils aliSmsUtils;
    private final RedisService redisService;
    private final AliyunOssService aliyunOssService;
    private final AliyunEmailService aliyunEmailService;
    private final AuthCodeService authCodeService;
    private final UserService userService;

    /**
     * 获取oss上传签名
     * @return ResultCode.SUCCESS 返回签名 ResultCode.SYSTEM_ERROR 系统错误(数据库)
     * @see <a href="https://help.aliyun.com/zh/oss/user-guide/form-upload?spm=a2c4g.11186623.0.i130">oss签名</a>
     */
    @GetMapping("/aliyun/oss/signature")
    public Result<Object> generatePostSignature(@RequestParam @NotNull String dir) {
        JSONObject signature;
        try {
            signature = aliyunOssService.generatePostSignature(dir);
        } catch (Exception e) {
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(signature);
    }

    /**
     * 获取oss 临时 url
     * @param fileName 文件路径
     * @return ResultCode.SYSTEM_ERROR 系统错误 ResultCode.SUCCESS 成功
     */
    @GetMapping("/aliyun/oss/signedURL")
    public Result<Object> generateSignedURL(@RequestParam @NotNull String fileName) {
        String signedUrl;
        try {
            signedUrl = aliyunOssService.generateSignedURL(fileName);
        } catch (Exception e) {
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(Map.of("url", signedUrl));
    }

    /**
     * 发送邮件
     * @param data 邮件模板, 收件人, 模板参数
     * @return ResultCode.SUCCESS 成功 ResultCode.SYSTEM_ERROR 系统错误
     */
    @PostMapping("/aliyun/email")
    public Result email(@RequestBody  @NotNull JSONObject data) {
        String template = data.getString("template");
        String toAddress = data.getString("toAddress");
        Map<String, String> map = data.getJSONObject("map").toJavaObject(Map.class);
        try {
            boolean email = userService.exists("email", toAddress);
            if (!email) {
                return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
            }
            aliyunEmailService.send(template, toAddress, map);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success();
    }

    /**
     * 发送邮件验证码
     * @param email 邮箱
     * @return ResultCode.SUCCESS 成功 ResultCode.REQUEST_TOO_FREQUENT 请求过于频繁 ResultCode.SYSTEM_ERROR 系统错误
     */
    @PostMapping("/aliyun/email/code")
    public Result emailCode(@RequestParam String email) {
        // 限制发送频率
        String s = redisService.get(email);
        if (s != null) {
            return ResultUtils.error(ResultCode.REQUEST_TOO_FREQUENT);
        }
        // 生成验证码
        String code = CodeUtils.generateVerifyCode(6);
        String template = "code";
        Map<String, String> map = Map.of("${code}", code);
        // 缓存验证码, 2分钟
        redisService.set(email, code, 120);
        try {
            aliyunEmailService.send(template, email, map);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success();
    }

    /**
     * 发送短信验证码
     * @param phoneNumber 手机号码
     * @return ResultCode.SUCCESS 成功 ResultCode.REQUEST_TOO_FREQUENT 请求过于频繁 ResultCode.SYSTEM_ERROR 系统错误
     */
    @PostMapping("aliyun/sms/code")
    public Result smsCode(@RequestParam @NotNull String phoneNumber) {
        // 限制发送频率
        String s = redisService.get(phoneNumber);
        if (s != null) {
            return ResultUtils.error(ResultCode.REQUEST_TOO_FREQUENT);
        }
        String code = CodeUtils.generateVerifyCode(6);
        // 缓存验证码
        redisService.set(phoneNumber, code, 120);
        try {
            aliSmsUtils.send(code, phoneNumber);
        } catch (Exception e) {
            log.error("发送短信失败", e);
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success();
    }

    /**
     * 聚合邮件短信发送验证码
     * @param data 邮箱或手机号码
     * @return ResultCode.SUCCESS 成功 ResultCode.REQUEST_TOO_FREQUENT 请求过于频繁 ResultCode.SYSTEM_ERROR 系统错误
     */
    @PostMapping("/aliyun/code")
    public Result<Object> sendCode(@RequestBody @NotNull JSONObject data) {
        String account = data.getString("account");
        // 限制发送频率
        String s = redisService.get(account);
        if (s != null) {
            return ResultUtils.error(ResultCode.REQUEST_TOO_FREQUENT);
        }
        String code = CodeUtils.generateVerifyCode(6);
        // 缓存验证码
        if (account.contains("@")) {
            try {
                // 发送邮件
                String template = "code";
                Map<String, String> map = Map.of("code", code);
                aliyunEmailService.send(template, account, map);
            } catch (Exception e) {
                log.error("发送邮件失败", e);
                return ResultUtils.error(ResultCode.SYSTEM_ERROR);
            }
        } else {
            try {
                // 发送短信
                aliSmsUtils.send(code, account);
            } catch (Exception e) {
                log.error("发送短信失败", e);
                return ResultUtils.error(ResultCode.SYSTEM_ERROR);
            }
        }
        // 缓存验证码
        redisService.set(account, code, 120);
        return ResultUtils.success();
    }

    /**
     * 验证码登录或注册
     * @param requestMap 手机号码或邮箱, 验证码
     * @return ResultCode.SUCCESS 成功 ResultCode.PARAMS_ERROR 参数错误 ResultCode.NOT_FOUND_ERROR 未找到用户 ResultCode.VERIFY_CODE_ERROR 验证码错误 ResultCode.SYSTEM_ERROR 系统错误
     */
    @PostMapping("authCode/loginOrRegister")
    public Result<Object> loginOrRegisterAuthCode(@NotNull @RequestBody Map<String,Object> requestMap) {
        String jwt = authCodeService.loginOrRegisterAuthCode(requestMap);
        if (jwt == null) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        } else if ("-1".equals(jwt)) {
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        } else if ("-2".equals(jwt)) {
            return ResultUtils.error(ResultCode.VERIFY_CODE_ERROR);
        } else if ("-3".equals(jwt)) {
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", JwtUtils.tokenHead + jwt);
        return ResultUtils.success(map);
    }

    /**
     * 重置密码
     * @param requestMap 手机号码或邮箱, 验证码
     * @return ResultCode.SUCCESS 成功 ResultCode.PARAMS_ERROR 参数错误 ResultCode.NOT_FOUND_ERROR 未找到用户 ResultCode.VERIFY_CODE_ERROR 验证码错误 ResultCode.SYSTEM_ERROR 系统错误
     */
    @PostMapping("authCode/resetPassword")
    public Result<Object> resetPasswordAuthCode(@NotNull @RequestBody Map<String,Object> requestMap) {
        String jwt = authCodeService.resetPasswordAuthCode(requestMap);
        if (jwt == null) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        } else if ("-1".equals(jwt)) {
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        } else if ("-2".equals(jwt)) {
            return ResultUtils.error(ResultCode.VERIFY_CODE_ERROR);
        } else if ("-3".equals(jwt)) {
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", JwtUtils.tokenHead + jwt);
        return ResultUtils.success(map);
    }

    /**
     * 绑定手机号码或邮箱
     * @param userID 用户ID
     * @param requestMap 手机号码或邮箱, 验证码
     * @return ResultCode.SUCCESS 成功 ResultCode.PARAMS_ERROR 参数错误 ResultCode.NOT_FOUND_ERROR 未找到用户 ResultCode.VERIFY_CODE_ERROR 验证码错误 ResultCode.SYSTEM_ERROR 系统错误
     */
    @PostMapping("authCode/bind")
    public Result<Object> bind(@GetAttribute String userID, @NotNull @RequestBody Map<String,Object> requestMap) {
        String jwt = authCodeService.bind(userID, requestMap);
        if (jwt == null) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        } else if ("-1".equals(jwt)) {
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        } else if ("-2".equals(jwt)) {
            return ResultUtils.error(ResultCode.VERIFY_CODE_ERROR);
        } else if ("-3".equals(jwt)) {
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", JwtUtils.tokenHead + jwt);
        return ResultUtils.success(map);
    }
}
