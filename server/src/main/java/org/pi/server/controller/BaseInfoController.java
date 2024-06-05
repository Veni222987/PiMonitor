package org.pi.server.controller;


import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.model.entity.Host;
import org.pi.server.service.BaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.common.Result;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/v1/agents/info")
public class BaseInfoController {

    @Autowired
    private BaseInfoService baseInfoService;

    /**
     * 获取主机信息
     * @param agentID
     * @return
     */
    @GetMapping
    public Result<Object> getComputerInfo(@GetAttribute("userID") @NotNull String userID, @RequestParam String agentID) {
        log.debug("agentID: {}", agentID);
        if (agentID == null) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        }
        Host host = baseInfoService.getComputerInfo(userID, agentID);
        if (host == null) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(host);
    }

    /**
     * 注册主机
     * @param host
     * @return
     */
    @PostMapping
    public Result<Object> postComputerInfo(@GetAttribute("teamID") @NotNull Integer teamID, @NotNull @RequestBody Host host) {
        log.debug(host.toString());
        if (teamID == null) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        host.setTeamId(teamID);
        long id = baseInfoService.postComputerInfo(host);
        if (id == 0) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        } else if (id == -1) {
            return ResultUtils.error(ResultCode.OPERATION_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return ResultUtils.success(map);
    }

    /**
     * 更新主机信息
     * @param agentID
     * @param hostname
     * @return
     */
    @PutMapping
    public Result<Object> putComputerInfo(@GetAttribute("userID") @NotNull String userID, @RequestParam String agentID, @RequestParam String hostname) {
        log.debug("agentID: {}, hostName: {}", agentID, hostname);
        if (agentID == null || hostname == null) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        }
        boolean b = baseInfoService.putComputerInfo(userID ,agentID, hostname);
        if (!b) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success();
    }
}
