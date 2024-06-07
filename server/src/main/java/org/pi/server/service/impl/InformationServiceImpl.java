package org.pi.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import org.pi.server.mapper.HostMapper;
import org.pi.server.mapper.TeamUserMapper;
import org.pi.server.model.entity.Host;
import org.pi.server.model.entity.TeamUser;
import org.pi.server.model.enums.HostStatusEnum;
import org.pi.server.repo.InfluxDBRepo;
import org.pi.server.service.InformationService;
import org.pi.server.utils.FluxQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author hu1hu
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InformationServiceImpl implements InformationService {
    private final InfluxDBRepo influxDBRepo;
    private final HostMapper hostMapper;
    private final TeamUserMapper teamUserMapper;

    /**
     * 获取性能数据
     * @param userID 用户ID
     * @param agentID 主机ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 性能数据
     */
    @Override
    public Map<String, List<Map<String, Object>>> getPerformance(String userID, String agentID, Long startTime, Long endTime) {
        checkPermission(userID, agentID);
        FluxQueryBuilder builder = new FluxQueryBuilder();
        String build = builder.fromBucket("myBucket")
                .range(startTime, endTime)
                .filterMeasurementByRegex("performance_" + agentID)
                .build();
        List<FluxTable> tables = influxDBRepo.query(build);
        return influxDBRepo.parse(tables);
    }

    /**
     * 获取指标数据
     * @param userID 用户ID
     * @param agentID 主机ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 指标数据
     */
    @Override
    public Map<String, List<Map<String, Object>>> getMetric(String userID, String agentID, Long startTime, Long endTime) {
        checkPermission(userID, agentID);
        FluxQueryBuilder builder = new FluxQueryBuilder();
        String build = builder.fromBucket("myBucket")
                .range(startTime, endTime)
                .filterMeasurementByRegex("agent_" + agentID)
                .build();
        List<FluxTable> tables = influxDBRepo.query(build);
        return influxDBRepo.parse(tables);
    }

    /**
     * 更新主机状态
     * @param agentID 主机ID
     */
    @Override
    @Transactional
    public void updateTime(String agentID) {
        LocalDateTime now = LocalDateTime.now();
        Host host = hostMapper.selectById(agentID);
        host.setLastTime(now);
        hostMapper.updateById(host);
    }

    /**
     * 判断用户有无权限查询数据
     * @param userID 用户ID
     * @param agentID 主机ID
     */
    private void checkPermission(String userID, String agentID) {
        Host host = hostMapper.selectById(agentID);
        Integer teamID = host.getTeamId();
        QueryWrapper<TeamUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamID).eq("user_id", userID);
        boolean exists = teamUserMapper.exists(queryWrapper);
        if (!exists) {
            throw new RuntimeException("用户无权限");
        }
    }
}
