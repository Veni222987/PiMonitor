package org.pi.server.repo;

import com.alibaba.fastjson2.JSONObject;
import com.influxdb.client.domain.WritePrecision;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.pi.server.model.dto.InfluxDBPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Repository;
import java.util.List;


/**
 * Kafka Repository
 */

@Repository
@Slf4j
public class KafkaRepo {

    @Autowired
    private KafkaTemplate<Object, Object> template;
    @Autowired
    private InfluxDBRepo influxDBRepo;

    /**
     * 生产消息
     * @param topic
     * @param message
     */
    public void produce(String topic, String message) {
        template.send(topic, message);
    }

    /**
     * 消费消息
     * @param records
     * @param ack
     */
    @KafkaListener(concurrency = "2", topics = "metric")
    public void processServiceMessage(@NotNull List<ConsumerRecord<String,String>> records, Acknowledgment ack) {
        for (ConsumerRecord<String, String> record : records) {
            List list = JSONObject.parseObject(record.value(), List.class);
            for(Object points : list) {
                InfluxDBPoints points_ = JSONObject.parseObject(points.toString(), InfluxDBPoints.class);
                String measurement = points_.getMeasurement();
                WritePrecision precision = points_.getPrecision();
                for(InfluxDBPoints.Point point : points_.getPoints()) {
                    influxDBRepo.write(measurement, point.getTags(), point.getFields(), point.getTimestamp(), precision);
                }
            }
        }
        // 手动提交offset，里面的逻辑是采用的同步提交，尝试3次
        ack.acknowledge();
    }
}
