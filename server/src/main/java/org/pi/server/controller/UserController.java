package org.pi.server.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.model.entity.Auth;
import org.pi.server.model.entity.User;
import org.pi.server.service.AuthService;
import org.pi.server.service.UserService;
import org.pi.server.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hu1hu
 */
@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    /**
     * 登录
     * @param account 账号
     * @param password 密码
     * @return ResultCode.SUCCESS 登录成功 ResultCode.NOT_FOUND_ERROR 账号不存在 ResultCode.PASSWORD_ERROR 密码错误
     */
    @GetMapping("/login")
    public Result<Object> login(@RequestParam @NotNull String account, @RequestParam @NotNull String password) {
        long id = userService.login(account, password);
        User user = userService.getByID(id);
        List<Auth> auths = authService.getAuthsByUserID(id);

        // 账号不存在
        if (id == -1) {
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        } else if (id == -2) {
            // 密码错误
            return ResultUtils.error(ResultCode.PASSWORD_ERROR);
        }
        // 生成jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put("userID", id + "");
        String jwt = JwtUtils.tokenHead + JwtUtils.generateJwt(claims);
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", jwt);
        map.put("user", user);
        map.put("auths", auths);
        return ResultUtils.success(map);
    }

    /**
     * 重置密码
     * @param phoneNumber 手机号
     * @param email 邮箱
     * @param data 新密码
     * @return ResultCode.SUCCESS 重置成功 ResultCode.NOT_FOUND_ERROR 账号不存在
     */
    @PostMapping("/resetPassword")
    public Result<Object> resetPassword(@GetAttribute("phoneNumber") String phoneNumber, @GetAttribute("email") String email, @RequestBody @NotNull JSONObject data) {
        String password = data.getString("password");
        boolean result;
        if (phoneNumber != null) {
            result = userService.setPasswordByPhoneNumber(phoneNumber, password);
        } else if (email != null) {
            result = userService.setPasswordByEmail(email, password);
        } else {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        if (result) {
            return ResultUtils.success();
        } else { // 账号不存在
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        }
    }

    @PostMapping("/modify")
    public Result<Object> modify(@GetAttribute("userID") @NotNull String userID, @RequestBody @NotNull User user) {
        if (!userService.modify(Long.parseLong(userID), user)) {
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success();
    }

    @GetMapping("/info")
    public Result<Object> info(@GetAttribute("userID") @NotNull String userID) {
        User user = userService.getByID(Long.parseLong(userID));
        List<Auth> auths = authService.getAuthsByUserID(Long.parseLong(userID));
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("auths", auths);
        return ResultUtils.success(map);
    }
}
