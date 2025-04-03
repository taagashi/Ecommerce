package br.com.thaua.Ecommerce.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class AddressException extends RuntimeException{
    private final Map<String, String> fields;

    public AddressException(String message, Map<String, String> fields) {
        super(message);
        this.fields = fields;
    }
}
