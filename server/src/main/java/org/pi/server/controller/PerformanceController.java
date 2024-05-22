package org.pi.server.controller;

import com.alibaba.fastjson2.JSONObject;
import org.pi.server.common.ResultUtils;
import org.pi.server.model.dto.Performance;
import org.pi.server.repo.KafkaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;


import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;


@Slf4j
@RestController
@RequestMapping("/v1/agents/{agentID}/usage")
public class PerformanceController {
    @Autowired
    private KafkaRepo kafkaRepo;

    @GetMapping
    public Result<Object> getPerformance(@PathVariable String agentID, @DateTimeFormat LocalDateTime startTime, @DateTimeFormat LocalDateTime endTime) {
        log.debug("agentID:{},startTime:{},endTime:{}", agentID, startTime, endTime);
        // TODO 处理查询逻辑

        return ResultUtils.success();
    }

    @PostMapping
    public Result<Object> postPerformance(@PathVariable String agentID, @DateTimeFormat LocalDateTime time, @RequestBody Performance performance) {
        log.debug("agentID:{},time:{},performance:{}", agentID, time, performance);
        //TODO: 这里调用Kafka生产者，生产一条消息
        performance.setTime(time);
        performance.setAgentID(agentID);
        kafkaRepo.produce("usage", JSONObject.toJSONString(performance));
        return ResultUtils.success();
    }


}