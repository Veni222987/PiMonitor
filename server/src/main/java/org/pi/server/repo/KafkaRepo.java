package org.pi.server.repo;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
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

    public void produce(String topic, String message) {
        template.send(topic, message);
    }

    /**
     * 消费消息
     * @param records
     * @param ack
     */
    @KafkaListener(concurrency = "2", topics = "usage")
    public void processUsageMessage(@NotNull List<ConsumerRecord<String,String>> records, Acknowledgment ack) {
        for (ConsumerRecord<String, String> record : records) {
            // TODO 处理消息

        }
        // 手动提交offset，里面的逻辑是采用的同步提交，尝试3次
        ack.acknowledge();
    }

    /**
     * 消费消息
     * @param records
     * @param ack
     */
    @KafkaListener(concurrency = "2", topics = "service")
    public void processServiceMessage(@NotNull List<ConsumerRecord<String,String>> records, Acknowledgment ack) {
        for (ConsumerRecord<String, String> record : records) {
            // TODO 处理消息

        }
        // 手动提交offset，里面的逻辑是采用的同步提交，尝试3次
        ack.acknowledge();
    }



}
