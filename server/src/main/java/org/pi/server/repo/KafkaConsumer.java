package org.pi.server.repo;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "my-topic")
    public void consume(String message) {
        System.out.println("Received message: " + message);
        // TODO 消费kafka消息，这里Component会自动注册到spring容器中，不需要自己启动，写好消费逻辑就行

        // TODO 消费消息的时候写进InfluxDB
    }
}
