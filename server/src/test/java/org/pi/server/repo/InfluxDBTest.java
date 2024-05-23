package org.pi.server.repo;

import org.junit.jupiter.api.Test;
import org.pi.server.utils.FluxQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class InfluxDBTest {

    @Autowired
    InfluxDBRepo influxDBRepo;
    @Test
    public void test() {
        FluxQueryBuilder builder = new FluxQueryBuilder();
        String build = builder.fromBucket("myBucket")
                .range(1716465531L, 1716465532L)
                .filterMeasurementByRegex("measurement-.*")
                .filterTag("agentID", "1")
                .filterField("cpu","mem")
                .build();
        System.out.println(build);
        influxDBRepo.query(build);
    }
}
