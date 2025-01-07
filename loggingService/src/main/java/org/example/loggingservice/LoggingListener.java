package org.example.loggingservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingListener {

    @KafkaListener(topics = "${kafka-topic}")
    public void logMessage(String message) {
        log.info(message);
    }
}
