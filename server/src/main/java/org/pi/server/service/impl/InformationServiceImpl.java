package org.pi.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.influxdb.query.FluxTable;
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

@Service
public class InformationServiceImpl implements InformationService {
    @Autowired
    private InfluxDBRepo influxDBRepo;
    @Autowired
    private HostMapper hostMapper;
    @Autowired
    private TeamUserMapper teamUserMapper;
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

    @Override
    @Transactional
    public void updateStatusByScanTime(String agentID) {
        LocalDateTime now = LocalDateTime.now();
        Host host = hostMapper.selectById(agentID);
        // 如果2分钟内没有扫描，则更新状态
        if (host.getLastTime().plusMinutes(2).isBefore(now)) {
            host.setStatus(HostStatusEnum.UNKNOWN);
            hostMapper.updateById(host);
        } else if (host.getStatus() == HostStatusEnum.UNMONITORED) {
            host.setStatus(HostStatusEnum.MONITORING);
            hostMapper.updateById(host);
        }
    }

    /**
     * 判断用户有无权限查询数据
     * @param userID
     * @param agentID
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
