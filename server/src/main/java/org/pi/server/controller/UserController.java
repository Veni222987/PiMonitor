package org.pi.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.model.entity.User;
import org.pi.server.service.UserService;
import org.pi.server.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public Result<Object> login(@RequestParam String account, @RequestParam String password) {
        long id = userService.login(account, password);
        if (id == -1) { // 账号不存在
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        } else if (id == -2) { // 密码错误
            return ResultUtils.error(ResultCode.PASSWORD_ERROR);
        }
        // 生成jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put("userID", id + "");
        String jwt = JwtUtils.tokenHead + JwtUtils.generateJwt(claims);
        Map<String, Object> map = new HashMap<>();
        map.put("jwt", jwt);
        return ResultUtils.success(map);
    }

    @PostMapping("/resetPassword")
    public Result<Object> resetPassword(@GetAttribute("phoneNumber") String phoneNumber, @GetAttribute("email") String email, @RequestParam String password) {
        boolean result = false;
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
    public Result<Object> modify(@GetAttribute("userID") String userID, @RequestBody User user) {
        if (!userService.modify(Long.parseLong(userID), user)) {
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success();
    }
}
