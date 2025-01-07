package org.example.restapi.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaClient {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaClient(KafkaTemplate<String, String> kafkaTemplate,
                       @Value("${kafka-topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendLogMessage(String message) {
        kafkaTemplate.send(topic, message);
    }
}
