package org.pi.server.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pi.server.annotation.GetAttribute;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.pi.server.model.dto.InfluxDBPoints;
import org.pi.server.repo.KafkaRepo;
import org.pi.server.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

import org.pi.server.common.Result;


@Slf4j
@RestController
@RequestMapping("/v1/agents/services/info")
public class MetricController {
    @Autowired
    private KafkaRepo kafkaRepo;
    @Autowired
    private InformationService informationService;

    @GetMapping
    public Result<Object> getMetricInfo(@GetAttribute("userID") @NotNull String userID, @RequestParam String agentID, @RequestParam Long startTime, @RequestParam Long endTime) {
        if (startTime >= endTime) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        }
        try {
            Map<String, List<Map<String, Object>>> metric = informationService.getMetric(userID, agentID, startTime, endTime);
            return ResultUtils.success(metric);
        } catch (Exception e) {
            return ResultUtils.error(ResultCode.NO_AUTH_ERROR);
        }
    }

    /**
     * 提交监控信息
     * @param teamID
     * @param agentID
     * @param list
     * @return
     */
    @PostMapping
    public Result<Object> postMetricInfo(@GetAttribute("teamID") @NotNull Integer teamID, @RequestParam String agentID, @NotNull @RequestBody List<InfluxDBPoints> list) {
        kafkaRepo.produce("metric", JSONObject.toJSONString(list));
        informationService.updateStatusByScanTime(agentID);
        return ResultUtils.success();
    }
}