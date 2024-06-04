package org.pi.server.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Code;
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
     * @return
     * @doc https://help.aliyun.com/zh/oss/user-guide/form-upload?spm=a2c4g.11186623.0.i130
     */
    @GetMapping("/aliyun/oss/signature")
    public Result generatePostSignature(@RequestParam String dir) {
        JSONObject signature;
        try {
            signature = aliyunOssService.generatePostSignature(dir);
        } catch (Exception e) {
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(signature);
    }

    /**
     * 获取oss url
     * @param fileName
     * @return
     */
    @GetMapping("/aliyun/oss/signedURL")
    public Result generateSignedURL(@RequestParam String fileName) {
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
     * @param template
     * @param toAddress
     * @param map
     * @return
     */
    @PostMapping("/aliyun/email")
    public Result email(@RequestParam String template,@RequestParam String toAddress, @RequestBody Map<String, String> map) {
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
     * @param email
     * @return
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
     * @param phoneNumber
     * @return
     */
    @PostMapping("aliyun/sms/code")
    public Result smsCode(@RequestParam String phoneNumber) {
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
     * @param account 手机号码或邮箱
     * @return
     */
    @PostMapping("/aliyun/code")
    public Result<Object> sendCode( @RequestParam String account) {
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
                Map<String, String> map = Map.of("${code}", code);
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
     * 验证验证码
     * @param userID 用户ID
     * @param requestMap type login/register/resetPassword/bind
     * @return
     */
    @PostMapping("authCode")
    public Result<Object> authCode(@GetAttribute String userID, @NotNull @RequestBody Map<String,Object> requestMap) {
        String jwt = null;
        if (requestMap.get("type").equals("loginOrRegister")) { // 登录或注册
            jwt = authCodeService.loginOrRegisterAuthCode(requestMap);
        } else if (requestMap.get("type").equals("resetPassword")) { // 重置密码
            jwt = authCodeService.resetPasswordAuthCode(requestMap);
        } else if (requestMap.get("type").equals("bind")) { // 绑定手机号或邮箱
            jwt = authCodeService.bind(userID, requestMap);
        }
        if (jwt == null) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        } else if (jwt.equals("-1")) {
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        } else if (jwt.equals("-2")) {
            return ResultUtils.error(ResultCode.VERIFY_CODE_ERROR);
        } else if (jwt.equals("-3")) {
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", JwtUtils.tokenHead + jwt);
        return ResultUtils.success(map);
    }
}
