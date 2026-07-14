package br.com.tech.challenger.api_restaurante.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserTypeNotFoundException extends RuntimeException {
    public UserTypeNotFoundException(String message) {
        super(message);
    }
}
