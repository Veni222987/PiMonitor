package org.pi.server.config;


import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.influx")
public class InfluxDBConfig {
    private String url;
    private String token;
    private String org;
    private String bucket;


    @Bean
    public InfluxDBClient getInfluxDBClient() {
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }
}
