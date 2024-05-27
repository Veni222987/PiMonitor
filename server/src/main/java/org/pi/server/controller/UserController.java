package org.pi.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.model.entity.User;
import org.pi.server.service.UserService;
import org.pi.server.utils.JwtUtil;
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
        log.debug("account:{},password:{}", account, password);
        long id = userService.login(account, password);
        if (id == -1) {
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        } else if (id == -2) {
            return ResultUtils.error(ResultCode.PASSWORD_ERROR);
        }
        // 生成jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put("userID", id);
        String jwt = JwtUtil.generateJwt(claims);
        return ResultUtils.success(jwt);
    }

    @PostMapping("/register")
    public Result<Object> register(@GetAttribute("phoneNumber") String phoneNumber, @GetAttribute("email") String email, @RequestBody User user) {
        log.debug("phoneNumber:{},email:{},user:{}", phoneNumber, email, user);
        if (phoneNumber != null) {
            user.setPhoneNumber(phoneNumber);
            user.setEmail(null);
        } else if (email != null) {
            user.setEmail(email);
            user.setPhoneNumber(null);
        }
        if (phoneNumber == null && email == null) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        long id = userService.insertUser(user);
        // 生成jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put("userID", id);
        String jwt = JwtUtil.generateJwt(claims);
        return ResultUtils.success(jwt);
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
        } else {
            return ResultUtils.error(ResultCode.NOT_FOUND_ERROR);
        }
    }

    @GetMapping("/exists")
    public Result<Object> exists(@RequestParam String type, @RequestParam String value) {
        if (type.equals("phone_number") || type.equals("email")) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.exists(type, value));
    }

    @PostMapping("/modify")
    public Result<Object> modify(@GetAttribute("userID") Integer userID, @RequestBody User user) {
        log.debug("userID:{},user:{}", userID, user);
        //TODO 修改用户信息
        if (!userService.modify(userID, user)) {
            return ResultUtils.error(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtils.success();
    }



    @GetMapping("/{userID}/agents")
    public Result<Object> getAgents(@PathVariable String userID) {
        log.debug("userID:{}", userID);
        //TODO 查询用户
        return ResultUtils.success();
    }


    @PostMapping("/{userID}/agents")
    public Result<Object> postAgents(@PathVariable String userID, @RequestParam String agentID) {
        log.debug("userID:{},agentID:{}", userID, agentID);
        //TODO 用户添加agent

        return ResultUtils.success();
    }

    @DeleteMapping("/{userID}/agents")
    public Result<Object> deleteAgents(@PathVariable String userID, @RequestParam String agentID) {
        log.debug("userID:{},agentID:{}", userID, agentID);
        //TODO 用户删除agent

        return ResultUtils.success();
    }

}
