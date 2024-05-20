package org.pi.server.controller;

import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class MetricController {
    @GetMapping("/agent/{agentID}/service/info")
    public Result<Object> getMethodName(@PathVariable String agentID) {
        // TODO process GET request
        return new Result<>(ResultCode.SUCCESS);
    }
    
    @PostMapping("/agent/{agentID}/service/info")
    public Result<Object> postMethodName(@PathVariable String agentID, @RequestBody Map<String, Object> entity) {
        //TODO: 这里调用Kafka生产者，生产一条消息
        
        return new Result<>(ResultCode.SUCCESS);
    }
    
}
