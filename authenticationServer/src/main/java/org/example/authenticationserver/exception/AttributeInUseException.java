package org.example.authenticationserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class AttributeInUseException extends RuntimeException {
    public AttributeInUseException(String message) {
        super(message);
    }
}
