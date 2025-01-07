package org.example.restapi.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.restapi.client.KafkaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Autowired
    public KafkaClient kafkaClient;

    @Around(value = "execution(* org.example.restapi.controller..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Exception exception = null;
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String logMessage = authentication.getName() + " called " + joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + joinPoint.getSignature().getName();
            if (exception != null) {
                logMessage += " with exception " + exception.getMessage();
            }
            kafkaClient.sendLogMessage(logMessage);
        }
    }
}
