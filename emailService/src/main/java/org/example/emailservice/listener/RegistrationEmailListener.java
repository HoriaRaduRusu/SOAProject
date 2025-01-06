package org.example.emailservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.emailservice.model.RegistrationEmailDTO;
import org.example.emailservice.service.EmailSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationEmailListener {

    private final ObjectMapper mapper;
    private final EmailSender emailSender;

    public RegistrationEmailListener(EmailSender emailSender) {
        this.mapper = new ObjectMapper();
        this.emailSender = emailSender;
    }

    @RabbitListener(queues = "EmailQueue")
    public void processMessage(String message) throws JsonProcessingException {
        RegistrationEmailDTO receivedEmail = mapper.readValue(message, RegistrationEmailDTO.class);
        emailSender.sendRegistrationEmail(receivedEmail.getEmail(), receivedEmail.getUsername());
    }
}
