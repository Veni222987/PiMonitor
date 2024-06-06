package org.pi.server.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
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


/**
 * @author hu1hu
 */
@Slf4j
@RestController
@RequestMapping("/v1/agents/services/info")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MetricController {
    private final KafkaRepo kafkaRepo;
    private final InformationService informationService;

    /**
     * 获取监控信息
     * @param userID 用户ID
     * @param agentID 主机ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return ResultCode.SUCCESS 获取成功 ResultCode.NO_AUTH_ERROR 无权限 ResultCode.PARAMS_ERROR 参数错误
     */
    @GetMapping
    public Result<Object> getMetricInfo(@GetAttribute("userID") @NotNull String userID, @RequestParam @NotNull String agentID, @RequestParam @NotNull Long startTime, @RequestParam @NotNull Long endTime) {
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
     * @param teamID 团队ID
     * @param agentID 主机ID
     * @param list 监控信息
     * @return ResultCode.SUCCESS 提交成功
     */
    @PostMapping
    public Result<Object> postMetricInfo(@GetAttribute("teamID") @NotNull Integer teamID, @RequestParam String agentID, @NotNull @RequestBody List<InfluxDBPoints> list) {
        // 保存到kafka
        kafkaRepo.produce("metric", JSONObject.toJSONString(list));
        // 更新主机状态
        informationService.updateStatusByScanTime(agentID);
        return ResultUtils.success();
    }
}