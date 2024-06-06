package org.pi.server.repo;

import com.influxdb.client.domain.WritePrecision;
import org.junit.jupiter.api.Test;
import org.pi.server.utils.FluxQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class InfluxDBTest {

    @Autowired
    InfluxDBRepo influxDBRepo;
    @Test
    public void test() {
        FluxQueryBuilder builder = new FluxQueryBuilder();
        String build = builder.fromBucket("myBucket")
                .range(1716465555L, 1716465556L)
                .filterMeasurementByRegex("measurement-.*")
                .filterTag("agentID", "1")
                .filterField("cpu","mem")
                .build();
        System.out.println(build);
        influxDBRepo.query(build);
    }

    @Test
    public void write() {
        Map<String, String> tags = new HashMap<>();
        tags.put("metric_type", "histogram");
        Map<String, Object> fields = new HashMap<>();
        fields.put("http_request_time", List.of(426, 420).toString());
        influxDBRepo.write("agent_888888", tags, fields, 1717172605, WritePrecision.NS);
    }
}
