package org.pi.server.controller;


import org.jetbrains.annotations.NotNull;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import org.pi.server.common.Result;


@Slf4j
@RestController
@RequestMapping("/v1/agents/usage")
public class PerformanceController {
    @Autowired
    private InformationService informationService;

    @GetMapping
    public Result<Object> getPerformance(@GetAttribute("userID") @NotNull String userID, @RequestParam String agentID, @RequestParam Long startTime, @DateTimeFormat Long endTime) {
        if (startTime >= endTime) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        }
        try {
            Map<String, List<Map<String, Object>>> performances = informationService.getPerformance(userID, agentID, startTime, endTime);
            return ResultUtils.success(performances);
        } catch (Exception e) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
    }
}