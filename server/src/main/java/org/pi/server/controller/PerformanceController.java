package org.pi.server.controller;

import org.pi.server.common.ResultUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;


import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.pi.server.common.Result;


@Slf4j
@RestController
@RequestMapping("/v1/agents/usage")
public class PerformanceController {

    @GetMapping
    public Result<Object> getPerformance(@RequestParam String agentID, @RequestParam Long startTime, @DateTimeFormat Long endTime) {
        log.debug("agentID:{},startTime:{},endTime:{}", agentID, startTime, endTime);
        // TODO 处理查询逻辑

        return ResultUtils.success();
    }
}