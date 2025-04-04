package br.com.thaua.Ecommerce.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public abstract class AbstractException extends RuntimeException{
    private final Map<String, String> fields;

    public AbstractException(String message, Map<String, String> fields) {
        super(message);
        this.fields = fields;
    }
}
