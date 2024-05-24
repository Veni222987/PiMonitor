package org.pi.server.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.pi.server.common.ResultUtils;
import org.pi.server.model.dto.InfluxDBPoints;
import org.pi.server.repo.KafkaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import org.pi.server.common.Result;


@Slf4j
@RestController
@RequestMapping("/v1/agents/services/info")
public class MetricController {
    @Autowired
    private KafkaRepo kafkaRepo;

    @GetMapping
    public Result<Object> getMetricInfo(@RequestParam String agentID, @RequestParam Long startTime, @RequestParam Long endTime) {
        log.debug("agentID:{},startTime:{},endTime:{}", agentID, startTime, endTime);
        // TODO process GET request

        return ResultUtils.success();
    }

    @PostMapping
    public Result<Object> postMetricInfo(@NotNull @RequestBody List<InfluxDBPoints> list) {
        log.debug("list:{}", list);
        //TODO: 这里调用Kafka生产者，生产一条消息
        kafkaRepo.produce("metric", JSONObject.toJSONString(list));
        return ResultUtils.success();
    }
}
