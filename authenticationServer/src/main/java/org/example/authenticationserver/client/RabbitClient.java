package org.example.authenticationserver.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.authenticationserver.model.RegistrationEmailDTO;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitClient {
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public RabbitClient(RabbitTemplate rabbitTemplate,
                        @Value("${rabbit.email-exchange}") String exchangeName,
                        @Value("${rabbit.email-routing-key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    public void sendMessage(RegistrationEmailDTO registrationEmailDTO) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, registrationEmailDTO);
        } catch (AmqpException e) {
            log.warn("Error sending email", e);
        }
    }
}
