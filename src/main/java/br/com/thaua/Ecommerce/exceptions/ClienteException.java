package br.com.thaua.Ecommerce.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class ClienteException extends RuntimeException{
    private final Map<String, String> errors;

    public ClienteException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
