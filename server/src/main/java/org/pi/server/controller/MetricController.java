package org.pi.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;


@Slf4j
@RestController
@RequestMapping("/v1/agents/{agentID}/service/info")
public class MetricController {
    @GetMapping
    public Result<Object> getMethodName(@PathVariable String agentID) {
        // TODO process GET request
        return new Result<>(ResultCode.SUCCESS);
    }

    @PostMapping
    public Result<Object> postMethodName(@PathVariable String agentID, @RequestBody Map<String, Object> entity) {
        //TODO: 这里调用Kafka生产者，生产一条消息
        return new Result<>(ResultCode.SUCCESS);
    }
    
}
