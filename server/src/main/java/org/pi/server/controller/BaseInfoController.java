package org.pi.server.controller;

import org.pi.server.common.ResultUtils;
import org.pi.server.model.entity.Host;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.common.Result;


@Slf4j
@RestController
@RequestMapping("/v1/agents/info")
public class BaseInfoController {

    /**
     * 获取主机信息
     * @param agentID
     * @return
     */
    @GetMapping
    public Result<Object> getComputerInfo(@RequestParam String agentID) {
        log.debug("agentID: {}", agentID);
        // TODO 查数据库，返回CPU信息

        Host host = new Host();
        return ResultUtils.success(host);
    }

    /**
     * 提交主机信息
     * @param host
     * @return
     */
    @PostMapping
    public Result<Object> postComputerInfo(@RequestBody Host host) {
        log.debug(host.toString());
        // TODO 将body解析后写入数据库

        return ResultUtils.success(host);
    }

    /**
     * 更新主机信息
     * @param agentID
     * @param hostName
     * @return
     */
    @PutMapping
    public Result<Object> putComputerInfo(@RequestParam String agentID, @RequestParam String hostName) {
        log.debug("agentID: {}, hostName: {}", agentID, hostName);
        // TODO 写入数据库

        Host host = new Host();
        return ResultUtils.success(host);
    }
}