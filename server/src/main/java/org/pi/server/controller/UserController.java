package org.pi.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.pi.server.common.Result;
import org.pi.server.common.ResultUtils;
import org.pi.server.model.entity.User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @GetMapping("/login")
    public Result<Object> login(@RequestParam String username, @RequestParam String password) {
        log.debug("username:{},password:{}", username, password);
        //TODO 查询用户

        User user = new User();
        return ResultUtils.success(user);
    }

    @PostMapping("/register")
    public Result<Object> register(@RequestBody User user) {
        log.debug("user:{}", user);
        //TODO 注册

        return ResultUtils.success(user);
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
