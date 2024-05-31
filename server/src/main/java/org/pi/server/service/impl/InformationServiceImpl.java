package org.pi.server.service.impl;

import com.influxdb.query.FluxTable;
import org.pi.server.repo.InfluxDBRepo;
import org.pi.server.service.InformationService;
import org.pi.server.utils.FluxQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InformationServiceImpl implements InformationService {
    @Autowired
    InfluxDBRepo influxDBRepo;
    public Map<String, List<Map<String, Object>>> getPerformance(String agentID, Long startTime, Long endTime) {
        FluxQueryBuilder builder = new FluxQueryBuilder();
        String build = builder.fromBucket("myBucket")
                .range(startTime, endTime)
                .filterMeasurementByRegex("performance_" + agentID)
                .build();
        List<FluxTable> tables = influxDBRepo.query(build);
        return influxDBRepo.parse(tables);
    }

    @Override
    public Map<String, List<Map<String, Object>>> getMetric(String agentID, Long startTime, Long endTime) {
        FluxQueryBuilder builder = new FluxQueryBuilder();
        String build = builder.fromBucket("myBucket")
                .range(startTime, endTime)
                .filterMeasurementByRegex("agent_" + agentID)
                .build();
        List<FluxTable> tables = influxDBRepo.query(build);
        return influxDBRepo.parse(tables);
    }
}
