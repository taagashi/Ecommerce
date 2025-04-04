package br.com.thaua.Ecommerce.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class ClienteException extends AbstractException{
    public ClienteException(String message, Map<String, String> errors) {
        super(message, errors);
    }
}
