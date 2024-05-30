package org.pi.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.service.AliyunEmailService;
import org.pi.server.service.AliyunOssService;
import org.pi.server.service.AuthCodeService;
import org.pi.server.service.RedisService;
import org.pi.server.utils.AliSmsUtils;
import org.pi.server.utils.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 获取oss上传签名
     * @return
     * @doc https://help.aliyun.com/zh/oss/user-guide/form-upload?spm=a2c4g.11186623.0.i130
     */
    @GetMapping("/aliyun/oss/signature")
    public Result generatePostSignature(@RequestParam String dir) {
        String signature;
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
        return ResultUtils.success(signedUrl.toString());
    }

    @PostMapping("/aliyun/email")
    public Result email(@RequestParam String template,@RequestParam String toAddress, @RequestBody Map<String, String> map) {
        try {
            aliyunEmailService.send(template, toAddress, map);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success();
    }

    @PostMapping("/aliyun/email/code")
    public Result emailCode(@RequestParam String email) {
        String s = redisService.get(email);
        if (s != null) { // 限制发送频率
            return ResultUtils.error(ResultCode.REQUEST_TOO_FREQUENT);
        }
        String code = CodeUtils.generateVerifyCode(6);
        String template = "code";
        Map<String, String> map = Map.of("${code}", code);
        // 缓存验证码, 5分钟
        redisService.set(email, code, 300);
        try {
            aliyunEmailService.send(template, email, map);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success();
    }

    @PostMapping("aliyun/sms/code")
    public Result smsCode(@RequestParam String phoneNumber) {
        String s = redisService.get(phoneNumber);
        if (s != null) { // 限制发送频率
            return ResultUtils.error(ResultCode.REQUEST_TOO_FREQUENT);
        }
        String code = CodeUtils.generateVerifyCode(6);
        // 缓存验证码
        redisService.set(phoneNumber, code, 300);
        try {
            aliSmsUtils.send(code, phoneNumber);
        } catch (Exception e) {
            log.error("发送短信失败", e);
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success();
    }

    @PostMapping("authCode")
    public Result authCode(@GetAttribute String userID, @NotNull @RequestBody Map<String,Object> requestMap) {
        String jwt = null;
        if (requestMap.get("type").equals("login")) {
            jwt = authCodeService.loginAuthCode(requestMap);
        } else if (requestMap.get("type").equals("register")) {
            jwt = authCodeService.registerAuthCode(requestMap);
        } else if (requestMap.get("type").equals("resetPassword")) {
            jwt = authCodeService.registerAuthCode(requestMap);
        } else if (requestMap.get("type").equals("bind")) {
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
        return ResultUtils.success(jwt);
    }
}
