package org.pi.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.influx")
public class InfluxDBConfig {
    private String url;
    private String token;
    private String org;
    private String bucket;

}
