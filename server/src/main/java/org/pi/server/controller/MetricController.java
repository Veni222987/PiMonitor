package org.pi.server.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    public Result<Object> getMetricInfo(@RequestParam String agentID, @RequestParam Long startTime, @RequestParam Long endTime) {
        log.debug("agentID:{},startTime:{},endTime:{}", agentID, startTime, endTime);
        if (startTime >= endTime) {
            return ResultUtils.error(ResultCode.PARAMS_ERROR);
        }
        Map<String, List<Map<String, Object>>> metric = informationService.getMetric(agentID, startTime, endTime);
        return ResultUtils.success(metric);
    }

    @PostMapping
    public Result<Object> postMetricInfo(@NotNull @RequestBody List<InfluxDBPoints> list) {
        log.debug("list:{}", list);
        //TODO: 这里调用Kafka生产者，生产一条消息
        kafkaRepo.produce("metric", JSONObject.toJSONString(list));
        return ResultUtils.success();
    }
}
