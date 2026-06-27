package br.com.tech.challenger.api_restaurante.application.exception;

public class UserNotAnOwnerException extends RuntimeException {
    public UserNotAnOwnerException(String message) {
        super(message);
    }
}
