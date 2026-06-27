package br.com.tech.challenger.api_restaurante.application.exception;

public class UserTypeNotFoundException extends RuntimeException {
    public UserTypeNotFoundException(String message) {
        super(message);
    }
}
