package org.pi.server.controller;

import org.springframework.web.bind.annotation.RestController;


import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Slf4j
@RestController("/api/v1")
public class PerformanceController {
    @GetMapping("/agent/{agentID}/usage")
    public Result<Object> getMethodName(@PathVariable String agentID) {
        // TODO 处理查询逻辑
        log.debug("dealing id: %s\n", agentID);
        return new Result<>(ResultCode.SUCCESS);
    }

    @PostMapping("/agent/{agentID}/usage")
    public Result<Object> postMethodName(@PathVariable String agentID, @RequestBody Map<String, Object> entity) {
        //TODO: 这里调用Kafka生产者，生产一条消息
        log.debug("dealing id: %s\n", agentID);
        return new Result<>(ResultCode.SUCCESS);
    }
    
    
}