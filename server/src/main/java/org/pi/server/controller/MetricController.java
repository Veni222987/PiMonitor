package org.pi.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.pi.server.common.ResultUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.Map;
import org.pi.server.common.Result;


@Slf4j
@RestController
@RequestMapping("/v1/agents/{agentID}/services/info")
public class MetricController {
    @GetMapping
    public Result<Object> getMetricInfo(@PathVariable String agentID, @DateTimeFormat LocalDateTime startTime, @DateTimeFormat LocalDateTime endTime) {
        log.debug("agentID:{},startTime:{},endTime:{}", agentID, startTime, endTime);
        // TODO process GET request

        return ResultUtils.success();
    }

    @PostMapping
    public Result<Object> postMetricInfo(@PathVariable String agentID, @DateTimeFormat LocalDateTime time, @RequestBody Map<String, Object> map) {
        log.debug("agentID:{},time:{},performance:{}", agentID, time, map);
        //TODO: 这里调用Kafka生产者，生产一条消息

        return ResultUtils.success();
    }
    
}
